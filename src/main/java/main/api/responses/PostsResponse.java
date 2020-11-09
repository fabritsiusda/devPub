package main.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostsResponse {

    private long count;
    private List<PostResponse> posts;

}
