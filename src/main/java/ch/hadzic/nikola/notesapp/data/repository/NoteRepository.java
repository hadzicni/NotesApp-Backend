package ch.hadzic.nikola.notesapp.data.repository;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // List<Note> findByUserId(String userId);
    List<Note> findByUserIdAndArchivedIsFalse(String userId);
    List<Note> findByUserIdAndArchivedIsTrue(String userId);
    List<Note> findByUserIdAndFavoriteIsTrue(String userId);
}
