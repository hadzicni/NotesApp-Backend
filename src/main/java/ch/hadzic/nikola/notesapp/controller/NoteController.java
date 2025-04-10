package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.security.Roles;
import ch.hadzic.nikola.notesapp.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "Notes Controller", description = "API for managing notes")
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "Creates a new note")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Note created successfully")
    })
    @RolesAllowed(Roles.Create)
    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        note.setUserId(userId);
        return ResponseEntity.ok(noteService.createNote(note));
    }


    @Operation(summary = "Gives all not archived notes for the current user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notes retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No notes found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping
    public ResponseEntity<List<Note>> getNotes() {
        List<Note> notes = noteService.getNotesForCurrentUser();
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Gives all archived notes for the current user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Archived notes retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No archived notes found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping("/archived")
    public ResponseEntity<List<Note>> getArchivedNotes() {
        List<Note> notes = noteService.getArchivedNotesForCurrentUser();
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Gives all favourite notes for the current user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Favourite notes retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No favourite notes found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping("/favourite")
    public ResponseEntity<List<Note>> getFavouriteNotes() {
        List<Note> notes = noteService.getFavouriteNotesForCurrentUser();
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Gives a note by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Note retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Note not found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);

        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(note);
    }

    @Operation(summary = "Updates a note by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Note updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Note not found")
    })
    @RolesAllowed(Roles.Update)
    @PatchMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody @Valid Note note) {
        note.setId(id);
        Note updated = noteService.updateNote(note);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletes a note by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Note deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Note not found")
    })
    @RolesAllowed(Roles.Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Note> deleteNote(@PathVariable Long id) {
        Note deletedNote = noteService.getNoteById(id);
        noteService.deleteNote(id);
        return ResponseEntity.ok(deletedNote);
    }



}
