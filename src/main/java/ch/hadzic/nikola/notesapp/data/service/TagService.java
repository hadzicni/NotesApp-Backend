package ch.hadzic.nikola.notesapp.data.service;

import ch.hadzic.nikola.notesapp.data.entity.Tag;
import ch.hadzic.nikola.notesapp.data.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllForUser(String userId) {
        return tagRepository.findByUserId(userId);
    }

    public Optional<Tag> getById(Long id) {
        return tagRepository.findById(id);
    }

    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag update(Tag tag) {
        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}
