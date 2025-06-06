package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.config.security.Roles;
import ch.hadzic.nikola.notesapp.data.entity.Notebook;
import ch.hadzic.nikola.notesapp.data.service.NotebookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/notebooks")
@Tag(name = "Notebook Controller", description = "API for managing notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @Operation(summary = "Gives all notebooks for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notebooks retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No notebooks found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping
    public ResponseEntity<List<Notebook>> getAll() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notebookService.getAllForUser(userId));
    }

    @Operation(summary = "Gives a notebook by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notebook retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Notebook not found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping("/{id}")
    public ResponseEntity<Notebook> getById(@PathVariable Long id) {
        return notebookService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creates a new notebook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notebook created successfully")
    })
    @RolesAllowed(Roles.Create)
    @PostMapping
    public ResponseEntity<Notebook> create(@Valid @RequestBody Notebook notebook) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        notebook.setUserId(userId);
        return ResponseEntity.ok(notebookService.create(notebook));
    }

    @Operation(summary = "Updates a notebook by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notebook updated successfully"),
            @ApiResponse(responseCode = "404", description = "Notebook not found")
    })
    @RolesAllowed(Roles.Update)
    @PatchMapping("/{id}")
    public ResponseEntity<Notebook> update(@PathVariable Long id, @RequestBody @Valid Notebook notebook) {
        if (notebookService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        notebook.setId(id);
        return ResponseEntity.ok(notebookService.update(notebook));
    }

    @Operation(summary = "Deletes a notebook by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notebook deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Notebook not found")
    })
    @RolesAllowed(Roles.Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (notebookService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        notebookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
