package main.api.responses;

import lombok.Data;

import java.util.List;

@Data
public class TagsResponse {

    private final List<TagResponse> tags;

}
