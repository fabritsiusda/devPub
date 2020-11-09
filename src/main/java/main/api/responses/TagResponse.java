package main.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class TagResponse {

    private final List<Tag> tags;

    @Data
    @AllArgsConstructor
    public static class Tag{
        private String name;
        private double weight;
    }

}
