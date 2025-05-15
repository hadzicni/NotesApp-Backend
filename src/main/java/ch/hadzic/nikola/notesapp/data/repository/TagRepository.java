package ch.hadzic.nikola.notesapp.data.repository;

import ch.hadzic.nikola.notesapp.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByUserId(String userId);
}
