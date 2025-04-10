package ch.hadzic.nikola.notesapp.data.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @NotEmpty
    private String title;

    @Size(max = 400)
    @Schema(description = "The content of the note")
    private String content;

    @Column(name = "is_favorite")
    private boolean favorite;

    @Column(name = "is_archived")
    private boolean archived;

    @Schema(hidden = true)
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Schema(hidden = true)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Schema(hidden = true)
    @Column(name = "user_id")
    private String userId;
}
