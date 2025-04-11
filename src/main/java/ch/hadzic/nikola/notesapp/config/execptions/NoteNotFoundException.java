package ch.hadzic.nikola.notesapp.config.execptions;

/**
 * Custom exception class for handling cases where a note is not found.
 * This exception extends RuntimeException and can be thrown when a note
 * with a specific identifier cannot be located in the system.
 */
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}