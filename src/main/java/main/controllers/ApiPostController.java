package main.controllers;

import main.api.responses.PostResponse;
import main.services.PostService;
import main.services.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<PostResponse> getPosts(
            @RequestParam int offset,
            @RequestParam int limit,
            @RequestParam String mode) {
        return postService.getPosts(offset, limit, mode);
    }

    @GetMapping("/search")
    public ResponseEntity<PostResponse> searchByQuery(
            @RequestParam int offset,
            @RequestParam int limit,
            @RequestParam(required = false) String query
    ){
      return postService.searchPostsByQuery(offset, limit, query);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostResponse> searchByDate(
            @RequestParam int offset,
            @RequestParam int limit,
            @RequestParam String date
    ){
        return postService.searchPostsByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostResponse> searchByTag(
            @RequestParam int offset,
            @RequestParam int limit,
            @RequestParam String tag
    ){
        return tagService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse.Post> getPostById(@PathVariable int id){
        return postService.getPostById(id);
    }


}
