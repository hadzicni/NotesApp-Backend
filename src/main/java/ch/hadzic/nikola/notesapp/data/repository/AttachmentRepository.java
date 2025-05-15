package ch.hadzic.nikola.notesapp.data.repository;

import ch.hadzic.nikola.notesapp.data.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByNoteId(Long noteId);
}
