package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.config.security.Roles;
import ch.hadzic.nikola.notesapp.data.entity.Notebook;
import ch.hadzic.nikola.notesapp.data.service.NotebookService;
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

    @RolesAllowed(Roles.Read)
    @GetMapping
    public ResponseEntity<List<Notebook>> getAll() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notebookService.getAllForUser(userId));
    }

    @RolesAllowed(Roles.Read)
    @GetMapping("/{id}")
    public ResponseEntity<Notebook> getById(@PathVariable Long id) {
        return notebookService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed(Roles.Create)
    @PostMapping
    public ResponseEntity<Notebook> create(@Valid @RequestBody Notebook notebook) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        notebook.setUserId(userId);
        return ResponseEntity.ok(notebookService.create(notebook));
    }

    @RolesAllowed(Roles.Update)
    @PatchMapping("/{id}")
    public ResponseEntity<Notebook> update(@PathVariable Long id, @RequestBody @Valid Notebook notebook) {
        if (!notebookService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        notebook.setId(id);
        return ResponseEntity.ok(notebookService.update(notebook));
    }

    @RolesAllowed(Roles.Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!notebookService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        notebookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
