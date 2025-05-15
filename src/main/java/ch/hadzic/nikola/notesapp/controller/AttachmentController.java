package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.config.security.Roles;
import ch.hadzic.nikola.notesapp.data.entity.Attachment;
import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.data.service.AttachmentService;
import ch.hadzic.nikola.notesapp.data.service.NoteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/attachments")
@Tag(name = "Attachment Controller", description = "API for managing note attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final NoteService noteService;

    public AttachmentController(AttachmentService attachmentService, NoteService noteService) {
        this.attachmentService = attachmentService;
        this.noteService = noteService;
    }

    @RolesAllowed(Roles.Read)
    @GetMapping("/note/{noteId}")
    public ResponseEntity<List<Attachment>> getByNoteId(@PathVariable Long noteId) {
        return ResponseEntity.ok(attachmentService.getByNoteId(noteId));
    }

    @RolesAllowed(Roles.Read)
    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getById(@PathVariable Long id) {
        return attachmentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed(Roles.Create)
    @PostMapping("/note/{noteId}")
    public ResponseEntity<Attachment> upload(@PathVariable Long noteId,
                                             @RequestParam("file") MultipartFile file) throws IOException {
        Note note = noteService.getNoteById(noteId);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        Attachment attachment = Attachment.builder()
                .note(note)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .data(file.getBytes())
                .build();

        return ResponseEntity.ok(attachmentService.save(attachment));
    }

    @RolesAllowed(Roles.Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!attachmentService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
