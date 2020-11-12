package main.mappers;

import main.api.responses.PostResponse;
import main.models.Post;
import main.models.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    private PostMapper(){}

    public static PostResponse mapToResponse(List<Post> posts, long count) {
        List<PostResponse.Post> responsePosts = posts.stream().map(PostMapper::postToPostResponse)
                .collect(Collectors.toList());
        return new PostResponse(count, responsePosts);
    }

    public static PostResponse.Post mapPostToPostResponse(Post post){
        PostResponse.Post response = postToPostResponse(post);
        response.setActive(post.isActive());
        response.setText(post.getText());
        response.setTags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        response.setComments(post.getComments().stream()
                .map(CommentsMapper::mapCommentToCommentResponse).collect(Collectors.toList())
        );
        return response;
    }

    private static PostResponse.Post postToPostResponse(Post post){
        PostResponse.User user = new PostResponse.User(post.getUser().getId(), post.getUser().getName());
        int likes =(int) post.getVotes().stream().filter(v -> v.getValue() ==1).count();
        PostResponse.Post response = new PostResponse.Post();
        response.setId(post.getId());
        response.setUser(user);
        response.setCommentCount(post.getComments().size());
        response.setLikeCount(likes);
        response.setDislikeCount(post.getVotes().size() - likes);
        response.setTimestamp(post.getTime());
        response.setTitle(post.getTitle());
        response.setViewCount(post.getViewCount());
        String text = post.getText();
        response.setAnnounce(text.replaceAll("\\<.*?\\>", "").substring(0, Integer.min(60, text.length())));
        return response;
    }
}
