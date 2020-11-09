package main.repositories;

import main.models.ModerationStatus;
import main.models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    List<Post> findAllByModerationStatusAndIsActiveInAndTimeBefore(ModerationStatus moderationStatus, List<Boolean> isActives, Date time, Pageable pageable);

}
