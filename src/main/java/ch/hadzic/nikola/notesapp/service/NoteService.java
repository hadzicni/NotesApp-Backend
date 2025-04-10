package ch.hadzic.nikola.notesapp.service;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.execptions.NoteNotFoundException;
import ch.hadzic.nikola.notesapp.data.repository.NoteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional
    public Note createNote(Note note) {
        String userId = getCurrentUserId();
        note.setUserId(userId);

        return noteRepository.save(note);
    }

    public List<Note> getNotesForCurrentUser() {
        String userId = getCurrentUserId();

        return noteRepository.findByUserId(userId);
    }

    public Note getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        validateOwnership(note);
        return note;
    }

    @Transactional
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        validateOwnership(note);
        noteRepository.delete(note);
    }

    @Transactional
    public Note updateNote(Note updatedNote) {
        Note existingNote = noteRepository.findById(updatedNote.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        validateOwnership(existingNote);

        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        existingNote.setArchived(updatedNote.isArchived());
        existingNote.setFavorite(updatedNote.isFavorite());

        return noteRepository.save(existingNote);
    }

    private void validateOwnership(Note note) {
        if (!note.getUserId().equals(getCurrentUserId())) {
            throw new SecurityException("Not authorized to access this note");
        }
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}