package ch.hadzic.nikola.notesapp.data.service;

import ch.hadzic.nikola.notesapp.data.entity.Attachment;
import ch.hadzic.nikola.notesapp.data.repository.AttachmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    private final AttachmentRepository repository;

    public AttachmentService(AttachmentRepository repository) {
        this.repository = repository;
    }

    public List<Attachment> getByNoteId(Long noteId) {
        return repository.findByNoteId(noteId);
    }

    public Optional<Attachment> getById(Long id) {
        return repository.findById(id);
    }

    public Attachment save(Attachment attachment) {
        return repository.save(attachment);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
