package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.config.security.Roles;
import ch.hadzic.nikola.notesapp.data.entity.Tag;
import ch.hadzic.nikola.notesapp.data.service.TagService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Controller", description = "API for managing tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @RolesAllowed(Roles.Read)
    @GetMapping
    public ResponseEntity<List<Tag>> getAll() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(tagService.getAllForUser(userId));
    }

    @RolesAllowed(Roles.Read)
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable Long id) {
        return tagService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed(Roles.Create)
    @PostMapping
    public ResponseEntity<Tag> create(@Valid @RequestBody Tag tag) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        tag.setUserId(userId);
        return ResponseEntity.ok(tagService.create(tag));
    }

    @RolesAllowed(Roles.Update)
    @PatchMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Long id, @RequestBody @Valid Tag tag) {
        if (!tagService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        tag.setId(id);
        return ResponseEntity.ok(tagService.update(tag));
    }

    @RolesAllowed(Roles.Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!tagService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
