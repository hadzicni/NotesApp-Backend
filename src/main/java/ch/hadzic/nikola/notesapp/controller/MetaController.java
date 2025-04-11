package ch.hadzic.nikola.notesapp.controller;

import ch.hadzic.nikola.notesapp.config.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * MetaController handles metadata and health check endpoints for the application.
 * It provides information about the application, server uptime, and database connectivity.
 * This controller is secured with JWT authentication.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed(Roles.Admin)
@RequestMapping("/api/meta")
@Tag(name = "Meta Controller", description = "App metadata and health check")
public class MetaController {

    private final DataSource dataSource;
    private final Instant startTime = Instant.now();
    @Value("${spring.app.name}")
    private String appName;
    @Value("${spring.app.version}")
    private String appVersion;

    public MetaController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Operation(summary = "Returns basic app info (name and version)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "App info retrieved successfully")
    })
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", appName);
        info.put("version", appVersion);
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "Simple ping endpoint to test API availability")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pong returned successfully")
    })
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @Operation(summary = "Returns the server uptime in HH:mm:ss.SSS format")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Uptime retrieved successfully")
    })
    @GetMapping("/uptime")
    public ResponseEntity<String> getUptime() {
        Duration uptime = Duration.between(startTime, Instant.now());
        long hours = uptime.toHours();
        long minutes = uptime.toMinutesPart();
        long seconds = uptime.toSecondsPart();
        long millis = uptime.toMillisPart();
        String formatted = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        return ResponseEntity.ok(formatted);
    }

    @Operation(summary = "Returns the current server time")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Server time retrieved successfully")
    })
    @GetMapping("/time")
    public ResponseEntity<ZonedDateTime> getServerTime() {
        return ResponseEntity.ok(ZonedDateTime.now());
    }

    @Operation(summary = "Checks database connectivity")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Database connection OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Database connection failed")
    })
    @GetMapping("/health/db")
    public ResponseEntity<String> dbHealth() {
        try (Connection conn = dataSource.getConnection()) {
            return ResponseEntity.ok("DB Connection OK " + "Connected to " + conn.getMetaData().getDatabaseProductName() + " " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("DB Connection FAILED");
        }
    }
}
