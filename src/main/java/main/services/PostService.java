package main.services;

import main.api.responses.PostResponse;
import main.mappers.PostMapper;
import main.models.ModerationStatus;
import main.models.Post;
import main.repositories.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

@Service
public class PostService {

    private final String
            RECENT_MODE = "recent",
            POPULAR_MODE = "popular",
            BEST_MODE = "best";

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public ResponseEntity<PostResponse> getPosts(int offset, int limit, String mode) {
        List<Post> posts = postRepository.findAllByModerationStatusAndIsActiveInAndTimeBefore(
                ModerationStatus.ACCEPTED, List.of(true), Calendar.getInstance().getTime(), getPageRequest(offset, limit, mode)
        );
        long count = postRepository.count();
        PostResponse response = PostMapper.mapToResponse(posts, count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private PageRequest getPageRequest(int offset, int limit, String mode){
        int page = offset / limit;
        return PageRequest.of(page, limit, getSortByMode(mode));
    }

    private Sort getSortByMode(String mode){
        if (POPULAR_MODE.equalsIgnoreCase(mode)){
            return Sort.sort(Post.class).by((Function<Post, Integer>) post -> post.getComments().size()).descending();
        }
        if (BEST_MODE.equalsIgnoreCase(mode)){
            return Sort.sort(Post.class).by((Function<Post, Long>) post ->
                    post.getVotes().stream().filter(v -> v.getValue() == 1).count()).descending();
        }
        Sort sort = Sort.by("time");
        if (RECENT_MODE.equalsIgnoreCase(mode))
            sort = sort.descending();
        return sort;
    }


}
