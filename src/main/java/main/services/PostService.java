package main.services;

import main.api.responses.PostResponse;
import main.mappers.PostMapper;
import main.models.ModerationStatus;
import main.models.Post;
import main.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Service
public class PostService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final String
            EARLY = "early",
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
        Sort sort = Sort.by("time").descending();
        if (EARLY.equalsIgnoreCase(mode))
            sort = sort.ascending();
        return sort;
    }


    public ResponseEntity<PostResponse> searchPostsByQuery(int offset, int limit, String query) {
        if (query == null){
            return getPosts(offset, limit, "");
        }
        List<Post> posts = postRepository.findAllByModerationStatusAndIsActiveInAndTimeBeforeAndTextContains(
                ModerationStatus.ACCEPTED, List.of(true), Calendar.getInstance().getTime(),
                query, getPageRequest(offset, limit, "")
        );
        int count = postRepository.countByModerationStatusAndIsActiveInAndTimeBeforeAndTextContains(
                ModerationStatus.ACCEPTED, List.of(true), Calendar.getInstance().getTime(), query);
        PostResponse response = PostMapper.mapToResponse(posts, count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<PostResponse> searchPostsByDate(int offset, int limit, String queryDate) {
        Date from;
        try {
            from = new SimpleDateFormat("yyyy-MM-dd").parse(queryDate);
            if (from.after(new Date())) {
                LOG.error("requests date after current date " + queryDate);
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (ParseException e) {
            LOG.error("error occurred parse date " + queryDate, e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Date to = getEndDayDate(from);
        int count = postRepository
                .countByModerationStatusAndIsActiveIsTrueAndTimeBetween(ModerationStatus.ACCEPTED, from, to);
        if (count == 0)
            return new ResponseEntity<>(new PostResponse(count, new ArrayList<>()), HttpStatus.OK);
        PageRequest page = getPageRequest(offset, limit, "");
        List<Post> posts = postRepository
                .findPostsByModerationStatusAndIsActiveIsTrueAndTimeBetween(ModerationStatus.ACCEPTED, from, to, page);
        return new ResponseEntity<>(PostMapper.mapToResponse(posts, count), HttpStatus.OK);
    }

    private Date getEndDayDate(Date from){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        calendar.add(Calendar.HOUR, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);
        if (calendar.getTime().after(new Date()))
            calendar.setTime(new Date());
        return calendar.getTime();
    }

    public ResponseEntity<PostResponse.Post> getPostById(int postId) {
        Optional<Post> optional = postRepository.findById(postId);
        return optional.isEmpty() ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(PostMapper.mapPostToPostResponse(optional.get()), HttpStatus.OK);
    }
}
