package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteControllerTest {

    @Autowired
    private MockMvc api;

    @Autowired
    private ObjectMapper objectMapper;

    private Long createdNoteId;

    private String accessToken;

    @BeforeAll
    void setup() {
        accessToken = obtainAccessToken();
    }

    @Test
    @Order(1)
    void createNote_shouldReturnCreatedNote() throws Exception {
        Note note = new Note();
        note.setTitle("Mein erster Eintrag");
        note.setContent("Das ist der Inhalt");
        note.setUserId("user");

        String body = objectMapper.writeValueAsString(note);

        String response = api.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mein erster Eintrag"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        createdNoteId = objectMapper.readValue(response, Note.class).getId();
    }

    @Test
    @Order(2)
    void getNoteById_shouldReturnNote() throws Exception {
        api.perform(get("/api/notes/" + createdNoteId)
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdNoteId));
    }

    @Test
    @Order(3)
    void getAllNotes_shouldReturnList() throws Exception {
        api.perform(get("/api/notes")
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mein erster Eintrag")));
    }

    @Test
    @Order(4)
    void updateNote_shouldReturnUpdatedNote() throws Exception {
        Note updated = new Note();
        updated.setTitle("Bearbeitet");
        updated.setContent("Neuer Inhalt");
        updated.setUserId("user");

        String body = objectMapper.writeValueAsString(updated);

        api.perform(patch("/api/notes/" + createdNoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bearbeitet"));
    }

    @Test
    @Order(5)
    void deleteNote_shouldReturnDeletedNote() throws Exception {
        api.perform(delete("/api/notes/" + createdNoteId)
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdNoteId));
    }

    @Test
    @Order(6)
    void getArchivedNotes_shouldReturnEmptyList() throws Exception {
        api.perform(get("/api/notes/archived")
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void getFavouriteNotes_shouldReturnEmptyList() throws Exception {
        api.perform(get("/api/notes/favourite")
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String obtainAccessToken() {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=NotesApp&" +
                "grant_type=password&" +
                "scope=openid profile roles offline_access&" +
                "username=user&" +
                "password=user";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> resp = rest.postForEntity(
                "http://localhost:8080/realms/NotesApp/protocol/openid-connect/token",
                entity,
                String.class);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resp.getBody()).get("access_token").toString();
    }
}
