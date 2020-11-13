package main.mappers;

import main.api.responses.PostResponse;
import main.models.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    private PostMapper(){}

    public static PostResponse.Post postToPostResponse(Post post){
        PostResponse.User user = new PostResponse.User(post.getUser().getId(), post.getUser().getName());
        int likes =(int) post.getVotes().stream().filter(v -> v.getValue() ==1).count();
        PostResponse.Post response = new PostResponse.Post();
        response.setId(post.getId());
        response.setUser(user);
        response.setCommentCount(post.getComments().size());
        response.setLikeCount(likes);
        response.setDislikeCount(post.getVotes().size() - likes);
        response.setTimestamp(post.getTime().getTime() / 1000);
        response.setTitle(post.getTitle());
        response.setViewCount(post.getViewCount());
        response.setAnnounce(post.getText().replaceAll("\\<.*?\\>", "").substring(0, 60));
        return response;

    }

    public static PostResponse mapToResponse(List<Post> posts, long count) {
        List<PostResponse.Post> responsePosts = posts.stream().map(PostMapper::postToPostResponse)
                .collect(Collectors.toList());
        return new PostResponse(count, responsePosts);
    }
}
