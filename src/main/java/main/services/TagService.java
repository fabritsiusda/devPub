package main.services;

import main.api.responses.TagResponse;
import main.api.responses.TagsResponse;
import main.mappers.TagMapper;
import main.models.Tag;
import main.repositories.PostRepository;
import main.repositories.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    //TODO переделать на запрос??
    public ResponseEntity<TagsResponse> getTagByQuery(String query) {
        List<Tag> tags = query!= null ?  tagRepository.findByNameContains(query)
                : tagRepository.findAll();
        long postCount = postRepository.count();
        List<TagResponse> tResponses = TagMapper.tagListToTagResponseList(tags, postCount);
        TagsResponse response = new TagsResponse(tResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
