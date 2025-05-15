package ch.hadzic.nikola.notesapp.data.repository;

import ch.hadzic.nikola.notesapp.data.entity.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {
    List<Notebook> findByUserId(String userId);
}
