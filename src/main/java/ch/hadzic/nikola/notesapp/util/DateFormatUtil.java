package ch.hadzic.nikola.notesapp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static String format(LocalDateTime date) {
        return FORMATTER.format(date);
    }
}
