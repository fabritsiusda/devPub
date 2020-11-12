package main.services;

import main.api.responses.PostResponse;
import main.api.responses.TagResponse;
import main.mappers.PostMapper;
import main.mappers.TagMapper;
import main.models.ModerationStatus;
import main.models.Post;
import main.models.Tag;
import main.repositories.PostRepository;
import main.repositories.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    //TODO переделать на запрос??
    public ResponseEntity<TagResponse> getTagByQuery(String query) {
        List<Tag> tags = query!= null ?  tagRepository.findByNameContains(query)
                : tagRepository.findAll();
        long postCount = postRepository.count();
        TagResponse response = TagMapper.mapToResponse(tags, postCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<PostResponse> getPostsByTag(int offset, int limit, String tagName) {
        Optional<Tag> optionalTag = tagRepository.findByName(tagName);
        if (!optionalTag.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Tag tag = optionalTag.get();
        int count = postRepository.countValidPostsByTag(
                ModerationStatus.ACCEPTED, tag, new Date());
        if (count == 0) {
            return new ResponseEntity<>(new PostResponse(count, new ArrayList<>()), HttpStatus.OK);
        }
        List<Post> posts = postRepository.findValidPostsByTag(ModerationStatus.ACCEPTED, tag, new Date(), getPageable(offset, limit));
        return new ResponseEntity<>(PostMapper.mapToResponse(posts, count), HttpStatus.OK);
    }

    private Pageable getPageable(int offset, int limit){
        int page = offset / limit;
        return PageRequest.of(page, limit);
    }
}
