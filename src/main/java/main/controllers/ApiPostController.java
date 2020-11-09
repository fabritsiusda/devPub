package main.controllers;

import main.api.responses.PostResponse;
import main.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    private ResponseEntity<PostResponse> getPosts(
            @RequestParam int offset,
            @RequestParam int limit,
            @RequestParam String mode){
        return postService.getPosts(offset, limit, mode);
    }

}
