package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.data.entity.Todo;
import ch.hadzic.nikola.notesapp.data.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/todos")
@Tag(name = "Todo Controller", description = "API for managing todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Gives all todos for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No todos found")
    })
    @GetMapping
    public List<Todo> getAll() {
        return todoService.getAll();
    }

    @Operation(summary = "Gives a todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        return todoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Gives all todos for a specific note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found")
    })
    @GetMapping("/note/{noteId}")
    public List<Todo> getByNoteId(@PathVariable Long noteId) {
        return todoService.getByNoteId(noteId);
    }

    @Operation(summary = "Creates a new todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo created successfully")
    })
    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.create(todo));
    }

    @Operation(summary = "Updates a todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo updated successfully"),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todo) {
        if (todoService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        return ResponseEntity.ok(todoService.update(todo));
    }

    @Operation(summary = "Deletes a todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todo deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (todoService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
