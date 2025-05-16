package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.config.security.Roles;
import ch.hadzic.nikola.notesapp.data.entity.Tag;
import ch.hadzic.nikola.notesapp.data.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Gives all tags for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No tags found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping
    public ResponseEntity<List<Tag>> getAll() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(tagService.getAllForUser(userId));
    }

    @Operation(summary = "Gives a tag by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @RolesAllowed(Roles.Read)
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable Long id) {
        return tagService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creates a new tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag created successfully")
    })
    @RolesAllowed(Roles.Create)
    @PostMapping
    public ResponseEntity<Tag> create(@Valid @RequestBody Tag tag) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        tag.setUserId(userId);
        return ResponseEntity.ok(tagService.create(tag));
    }

    @Operation(summary = "Updates a tag by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @RolesAllowed(Roles.Update)
    @PatchMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Long id, @RequestBody @Valid Tag tag) {
        if (tagService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        tag.setId(id);
        return ResponseEntity.ok(tagService.update(tag));
    }

    @Operation(summary = "Deletes a tag by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @RolesAllowed(Roles.Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (tagService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
