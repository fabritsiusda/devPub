package main.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CommentResponse {

    private int id;
    private Date timestamp;
    private String text;
    private User user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User{

        private int id;
        private String name;
        private String photo;

    }

}
