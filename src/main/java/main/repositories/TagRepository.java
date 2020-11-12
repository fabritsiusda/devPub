package main.repositories;

import main.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag> findByNameContains(String query);

    Optional<Tag> findByName(String tag);
}
