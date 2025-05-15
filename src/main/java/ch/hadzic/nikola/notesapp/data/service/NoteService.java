package ch.hadzic.nikola.notesapp.data.service;

import ch.hadzic.nikola.notesapp.config.execptions.NoteNotFoundException;
import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.data.entity.Tag;
import ch.hadzic.nikola.notesapp.data.repository.NoteRepository;
import ch.hadzic.nikola.notesapp.data.repository.TagRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing notes.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;

    public NoteService(NoteRepository noteRepository, TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Note createNote(Note note) {
        String userId = getCurrentUserId();
        note.setUserId(userId);

        return noteRepository.save(note);
    }

    public List<Note> getNotesForCurrentUser() {
        String userId = getCurrentUserId();

        return noteRepository.findByUserIdAndArchivedIsFalse(userId);
    }

    public List<Note> getFavouriteNotesForCurrentUser() {
        String userId = getCurrentUserId();

        return noteRepository.findByUserIdAndFavoriteIsTrue(userId);
    }

    public List<Note> getArchivedNotesForCurrentUser() {
        String userId = getCurrentUserId();

        return noteRepository.findByUserIdAndArchivedIsTrue(userId);
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
        Note existing = getNoteById(updatedNote.getId());

        existing.setTitle(updatedNote.getTitle());
        existing.setContent(updatedNote.getContent());
        existing.setFavorite(updatedNote.isFavorite());
        existing.setArchived(updatedNote.isArchived());

        if (updatedNote.getNotebook() != null) {
            existing.setNotebook(updatedNote.getNotebook());
        }

        // Bidirektionale Pflege der Tags
        if (updatedNote.getTags() != null) {
            // Entferne bestehende Verknüpfungen
            if (existing.getTags() != null) {
                for (Tag oldTag : existing.getTags()) {
                    oldTag.getNotes().remove(existing);
                }
            }

            // Neue Tags setzen und bidirektional pflegen
            Set<Tag> persistentTags = new HashSet<>();
            for (Tag tag : updatedNote.getTags()) {
                Tag persistentTag = tagRepository.findById(tag.getId())
                        .orElseThrow(() -> new RuntimeException("Tag nicht gefunden: " + tag.getId()));
                persistentTags.add(persistentTag);
                persistentTag.getNotes().add(existing);
            }
            existing.setTags(persistentTags);
        }

        return noteRepository.save(existing);
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