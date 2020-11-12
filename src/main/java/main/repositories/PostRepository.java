package main.repositories;

import main.models.ModerationStatus;
import main.models.Post;
import main.models.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    List<Post> findAllByModerationStatusAndIsActiveInAndTimeBefore(
            ModerationStatus moderationStatus,
            List<Boolean> isActives, Date time, Pageable pageable);

    List<Post> findAllByModerationStatusAndIsActiveInAndTimeBeforeAndTextContains(
            ModerationStatus moderationStatus,
            List<Boolean> isActives, Date time, String text, Pageable pageable);

    int countByModerationStatusAndIsActiveInAndTimeBeforeAndTextContains(
            ModerationStatus moderationStatus,
            List<Boolean> isActives, Date time, String text);

    @Query(nativeQuery = true, value = "SELECT date_trunc('year', p.time) FROM posts p " +
            "WHERE p.time <= :date AND p.moderation_status = :#{#status.name} AND p.is_active = 1" +
            "GROUP BY 1 ORDER BY count(1);")
    List<Date> getYears(@Param("date") Date date, @Param("status") ModerationStatus status);

    @Query(nativeQuery = true, value = "SELECT date_trunc('day', p.time) FROM posts p " +
            "WHERE date_trunc('year', p.time) = :year AND p.time <= :date " +
            "AND p.moderation_status = :#{#status.name} AND p.is_active = 1" +
            "GROUP BY 1 ORDER BY count(1);")
    List<Date> getDatesByYear(@Param("year") Date year, @Param("date") Date date,
                              @Param("status") ModerationStatus status);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM posts p " +
            "WHERE date_trunc('year', p.time) = :year AND p.time <= :date " +
            "AND p.moderation_status = :#{#status.name} AND p.is_active = 1" +
            "GROUP BY date_trunc('day', p.time) ORDER BY count(1);")
    List<Integer> getPostsCount(@Param("year") Date year, @Param("date") Date date,
                                @Param("status") ModerationStatus status);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM posts p " +
            "WHERE date_trunc('day', p.time) =:year " +
            "GROUP BY date_trunc('day', p.time)" )
    Integer getPostsCountByDay(@Param("year") Date date);

    int countByModerationStatusAndIsActiveIsTrueAndTimeBetween(ModerationStatus status, Date from, Date to);

    List<Post> findPostsByModerationStatusAndIsActiveIsTrueAndTimeBetween(ModerationStatus status, Date from, Date to, Pageable page);

    @Query("select p from Post p join p.tags t where t.id = :#{#tag.id} and p.moderationStatus = :status and p.isActive = true and p.time <= :date")
    List<Post> findValidPostsByTag(@Param("status") ModerationStatus status, @Param("tag") Tag tag, @Param("date") Date date, Pageable pageable);

    @Query("select count(p) from Post p join p.tags t where t.id = :#{#tag.id} and p.moderationStatus = :status and p.isActive = true and p.time <= :date")
    int countValidPostsByTag(@Param("status") ModerationStatus status, @Param("tag") Tag tag, @Param("date") Date date);
}
