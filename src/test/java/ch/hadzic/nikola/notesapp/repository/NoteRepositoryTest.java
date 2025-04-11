package ch.hadzic.nikola.notesapp.repository;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.data.repository.NoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    private Note note;

    @BeforeAll
    void setup() {
        note = new Note();
        note.setTitle("Testnote");
        note.setContent("Inhalt");
        note.setUserId("test-user");
        note.setArchived(false);
        note.setFavorite(false);
    }

    @Test
    @Order(1)
    @Rollback(false)
    void testCreateNote() {
        Note saved = noteRepository.save(note);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Testnote");
    }

    @Test
    @Order(2)
    void testReadNoteById() {
        Optional<Note> found = noteRepository.findById(note.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Testnote");
    }

    @Test
    @Order(3)
    void testReadAllNotesByUser() {
        List<Note> notes = noteRepository.findByUserIdAndArchivedIsFalse("test-user");
        assertThat(notes).isNotEmpty();
    }

    @Test
    @Order(4)
    @Rollback(false)
    void testUpdateNote() {
        Note toUpdate = noteRepository.findById(note.getId()).orElseThrow();
        toUpdate.setTitle("Updated Title");
        Note updated = noteRepository.save(toUpdate);

        assertThat(updated.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @Order(5)
    @Rollback(false)
    void testDeleteNote() {
        noteRepository.deleteById(note.getId());
        Optional<Note> deleted = noteRepository.findById(note.getId());
        assertThat(deleted).isEmpty();
    }
}
