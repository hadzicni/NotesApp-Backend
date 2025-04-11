package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.data.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RestController
@Tag(name = "Token Controller", description = "Keycloak token management (Only for internal and development use)")
@RequestMapping("/api/auth")
public class TokenController {

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${spring.app.name}")
    private String clientId;

    @Operation(summary = "Get Keycloak access token (Only for internal and development use)")
    @ApiResponse(responseCode = "200", description = "Token received",
            content = @Content(schema = @Schema(implementation = TokenResponseDTO.class)))
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> getToken(@RequestParam String username, @RequestParam String password) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("grant_type", "password");
        body.add("scope", "openid profile roles offline_access");
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(tokenUri, request, TokenResponseDTO.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
