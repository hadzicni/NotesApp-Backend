package ch.hadzic.nikola.notesapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    /**
     * Retrieves the current user's ID from the JWT token.
     *
     * @return The user's ID.
     * @throws RuntimeException if the user ID is not found in the JWT.
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");

        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Username not found in JWT");
        }
        return username;
    }
}
