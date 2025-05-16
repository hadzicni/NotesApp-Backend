package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.data.service.NoteService;
import ch.hadzic.nikola.notesapp.util.PdfExportUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ExportController handles the export of notes as PDF files.
 * It provides endpoints to export all notes or a specific note as a PDF file.
 * This controller is secured with JWT authentication.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/export")
@Tag(name = "Export Controller", description = "Export notes as PDF")
public class ExportController {

    private final NoteService noteService;

    public ExportController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "Export all notes as PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF file generated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/notes/pdf")
    public ResponseEntity<byte[]> exportNotesAsPdf() {
        List<Note> notes = noteService.getNotesForCurrentUser();
        byte[] pdfBytes = PdfExportUtil.exportNotesToPdf(notes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=notes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @Operation(summary = "Export a specific note as PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF file generated successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/notes/pdf/{id}")
    public ResponseEntity<byte[]> exportNoteAsPdf(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes = PdfExportUtil.exportNoteToPdf(note);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=note_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
