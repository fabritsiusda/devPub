package main.services;

import main.api.responses.BlogInfo;
import main.api.responses.CalendarResponse;
import main.models.ModerationStatus;
import main.repositories.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApiService {

    private final BlogInfo info;
    private final PostRepository postRepository;

    public ApiService(BlogInfo info, PostRepository postService) {
        this.info = info;
        this.postRepository = postService;
    }

    public ResponseEntity<BlogInfo> getBlogInfo(){
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    public ResponseEntity<CalendarResponse> getCalendar(Integer year){
        Calendar calendar = Calendar.getInstance();
        if (year == null){
            year = calendar.get(Calendar.YEAR);
        }
        List<Integer> years = getYears(calendar);

        if (!years.contains(year))
            return new ResponseEntity(new CalendarResponse(years, new HashMap<>()), HttpStatus.OK);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        List<Date> days = postRepository.getDatesByYear(calendar.getTime(), new Date(), ModerationStatus.ACCEPTED);
        List<Integer> counts = postRepository.getPostsCount(calendar.getTime(), new Date(), ModerationStatus.ACCEPTED);
        Map<String, Integer> posts = getPostsCount(days, counts, calendar);
        return new ResponseEntity<>(new CalendarResponse(years, posts), HttpStatus.OK);
    }

    private List<Integer> getYears(Calendar calendar){
        return postRepository.getYears(new Date(), ModerationStatus.ACCEPTED).stream()
                .map(d -> {
                    calendar.setTime(d);
                    return calendar.get(Calendar.YEAR);
                }).collect(Collectors.toList());
    }

    private Map<String, Integer> getPostsCount(List<Date> days, List<Integer> counts, Calendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> posts = new HashMap<>();
        if (counts.size() == days.size()){
            for (int i = 0; i < days.size(); i++) {
                posts.put(format.format(days.get(i)), counts.get(i));
            }
        } else{
            for (Date date : days) {
                calendar.setTime(date);
                Integer count = postRepository.getPostsCountByDay(date);
                if (count != null)
                    posts.put(format.format(date), count);
            }
        }
        return posts;
    }

}
