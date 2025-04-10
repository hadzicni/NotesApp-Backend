package ch.hadzic.nikola.notesapp.execptions;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}