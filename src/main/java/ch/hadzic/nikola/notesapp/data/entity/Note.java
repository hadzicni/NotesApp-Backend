package ch.hadzic.nikola.notesapp.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Note entity representing a note in the application.
 * It contains fields for title, content, favorite status, archived status,
 * creation timestamp, update timestamp, and user ID.
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @NotEmpty
    @Size(max = 255)
    private String title;

    @Size(max = 2500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;

    @ManyToMany
    @JsonIgnore
    private Set<Tag> tags;

    @Schema(defaultValue = "false")
    @Column(name = "is_favorite", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean favorite = false;

    @Schema(defaultValue = "false")
    @Column(name = "is_archived", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean archived = false;

    @Schema(hidden = true)
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Schema(hidden = true)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Schema(hidden = true)
    @Column(name = "user_id", nullable = false)
    private String userId;
}
