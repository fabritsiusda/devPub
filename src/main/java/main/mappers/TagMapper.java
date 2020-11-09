package main.mappers;

import main.api.responses.TagResponse;
import main.models.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagMapper {

    private TagMapper(){}

    public static TagResponse tagToTagResponse(Tag tag, long count){
        int size = tag.getPosts().size();
        double weight = (double) size / count;
        return new TagResponse(tag.getName(), weight);
    }

    public static List<TagResponse> tagListToTagResponseList(List<Tag> tags, long count){
        List<TagResponse> result = new ArrayList<>();
        tags.stream().map(tag -> tagToTagResponse(tag, count)).forEach(result::add);
        normalizeWeights(result);
        return result;
    }

    private static void normalizeWeights(List<TagResponse> list){
        Optional<Double> optional = list.stream().map(TagResponse::getWeight).max(Double::compareTo);
        if (optional.isPresent()){
            double max = optional.get();
            list.forEach(t -> t.setWeight(t.getWeight() / max));
        }
    }

}
