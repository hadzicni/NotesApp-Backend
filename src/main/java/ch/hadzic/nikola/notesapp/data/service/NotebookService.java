package ch.hadzic.nikola.notesapp.data.service;

import ch.hadzic.nikola.notesapp.data.entity.Notebook;
import ch.hadzic.nikola.notesapp.data.repository.NotebookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;

    public NotebookService(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    public List<Notebook> getAllForUser(String userId) {
        return notebookRepository.findByUserId(userId);
    }

    public Optional<Notebook> getById(Long id) {
        return notebookRepository.findById(id);
    }

    public Notebook create(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public Notebook update(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public void delete(Long id) {
        notebookRepository.deleteById(id);
    }
}
