package main.mappers;

import main.api.responses.PostResponse;
import main.api.responses.UserResponse;
import main.models.Post;

public class PostMapper {

    private PostMapper(){}

    public static PostResponse postToPostResponse(Post post){
        UserResponse user = new UserResponse(post.getUser().getId(), post.getUser().getName());
        int likes =(int) post.getVotes().stream().filter(v -> v.getValue() ==1).count();

        return new PostResponse(
                post.getId(),
                post.getTime(),
                user,
                post.getTitle(),
                post.getText().substring(0, 60),
                likes,
                post.getVotes().size() - likes,
                post.getComments().size(),
                post.getViewCount()
        );
    }


}
