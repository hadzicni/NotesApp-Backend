package ch.hadzic.nikola.notesapp.config.security;

/**
 * This class defines the roles used in the application.
 * Each role is represented as a public static final String constant.
 * These roles can be used for authorization and access control within the application.
 */
public class Roles {
    public static final String Admin = "admin";
    public static final String Delete = "delete";
    public static final String Read = "read";
    public static final String Update = "update";
    public static final String Create = "write";
}
