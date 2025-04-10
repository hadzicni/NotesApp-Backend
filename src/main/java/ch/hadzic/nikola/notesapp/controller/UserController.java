package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.security.Roles;
import ch.hadzic.nikola.notesapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed(Roles.Read)
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "Current user info")
public class UserController {

    @Operation(summary = "Returns information about the currently authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User info retrieved successfully")
    })
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", jwt.getClaimAsString("preferred_username"));
        userInfo.put("email", jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "Not set");
        userInfo.put("given_name", jwt.getClaimAsString("given_name") != null ? jwt.getClaimAsString("given_name") : "Not set");
        userInfo.put("family_name", jwt.getClaimAsString("family_name") != null ? jwt.getClaimAsString("family_name") : "Not set");
        userInfo.put("sub", jwt.getClaimAsString("sub"));
        userInfo.put("iss", jwt.getClaimAsString("iss"));
        userInfo.put("aud", jwt.getClaimAsString("aud"));

        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "Returns the current user's username")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Username retrieved successfully")
    })
    @GetMapping("/username")
    public ResponseEntity<String> getCurrentUsername() {
        return ResponseEntity.ok(UserService.getUsername());
    }

    @Operation(summary = "Returns the current user's ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User ID retrieved successfully")
    })
    @GetMapping("/id")
    public ResponseEntity<String> getCurrentUserId() {
        return ResponseEntity.ok(UserService.getCurrentUserId());
    }
}
