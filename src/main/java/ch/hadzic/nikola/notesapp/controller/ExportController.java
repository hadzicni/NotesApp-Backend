package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.data.repository.NoteRepository;
import ch.hadzic.nikola.notesapp.util.PdfExportUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/export")
public class ExportController {

    private final NoteRepository noteRepository;

    public ExportController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/notes/pdf")
    public ResponseEntity<byte[]> exportNotesAsPdf() {
        List<Note> notes = noteRepository.findAll();
        byte[] pdfBytes = PdfExportUtil.exportNotesToPdf(notes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=notes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/notes/pdf/{id}")
    public ResponseEntity<byte[]> exportNoteAsPdf(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        byte[] pdfBytes = PdfExportUtil.exportNoteToPdf(note);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=note_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
