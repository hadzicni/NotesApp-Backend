package ch.hadzic.nikola.notesapp.data.dto;

public record TokenResponseDTO(
        String access_token,
        String refresh_token,
        long expires_in,
        long refresh_expires_in
) {}