package ch.hadzic.nikola.notesapp.data.dto;

public record TokenResponse(
        String access_token,
        String refresh_token,
        String id_token,
        String token_type,
        long expires_in,
        long refresh_expires_in,
        String scope,
        String session_state,
        int not_before_policy
) {}
