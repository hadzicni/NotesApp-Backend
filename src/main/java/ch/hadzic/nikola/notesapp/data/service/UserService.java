package ch.hadzic.nikola.notesapp.data.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 */
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

    /**
     * Retrieves the current user's ID from the JWT token.
     *
     * @return The user's ID.
     * @throws RuntimeException if the user ID is not found in the JWT.
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        if (userId == null || userId.isEmpty()) {
            throw new RuntimeException("User ID not found in JWT");
        }
        return userId;
    }
}
