package main.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse {

    private long count;
    private List<Post> posts;

    @Data
    @NoArgsConstructor
    public static class Post{

        private int id;
        private long timestamp;
        private User user;
        private String title;
        private String announce;
        private int likeCount;
        private int dislikeCount;
        private int commentCount;
        private int viewCount;

    }

    @Data
    @AllArgsConstructor
    public static class User {

        private int id;
        private String name;

    }

}
