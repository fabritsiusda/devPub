package main.mappers;

import main.api.responses.CommentResponse;
import main.models.PostComment;
import main.models.User;

public class CommentsMapper {

    public static CommentResponse mapCommentToCommentResponse(PostComment comment){
        CommentResponse response = new CommentResponse();
        User author = comment.getUser();
        CommentResponse.User user = new CommentResponse.User(author.getId(), author.getName(), author.getPhoto());
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setTimestamp(comment.getTime());
        response.setUser(user);
        return response;
    }

}
