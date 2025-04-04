package ch.hadzic.nikola.notesapp.repository;

import ch.hadzic.nikola.notesapp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(String userId);
}
