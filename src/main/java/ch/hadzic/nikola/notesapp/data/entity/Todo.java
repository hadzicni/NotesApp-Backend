package ch.hadzic.nikola.notesapp.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean done;

    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;
}
