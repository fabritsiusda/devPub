package main.mappers;

import main.api.responses.TagResponse;
import main.models.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagMapper {

    private TagMapper(){}

    public static TagResponse mapToResponse(List<Tag> tags, long postCount) {
        return new TagResponse(tagListToTagResponseList(tags, postCount));
    }

    private static TagResponse.Tag tagToTagResponse(Tag tag, long count){
        int size = tag.getPosts().size();
        double weight = (double) size / count;
        return new TagResponse.Tag(tag.getName(), weight);
    }

    private static List<TagResponse.Tag> tagListToTagResponseList(List<Tag> tags, long count){
        List<TagResponse.Tag> result = new ArrayList<>();
        tags.stream().map(tag -> tagToTagResponse(tag, count)).forEach(result::add);
        normalizeWeights(result);
        return result;
    }

    private static void normalizeWeights(List<TagResponse.Tag> list){
        Optional<Double> optional = list.stream().map(TagResponse.Tag::getWeight).max(Double::compareTo);
        if (optional.isPresent()){
            double max = optional.get();
            list.forEach(t -> t.setWeight(t.getWeight() / max));
        }
    }
}
