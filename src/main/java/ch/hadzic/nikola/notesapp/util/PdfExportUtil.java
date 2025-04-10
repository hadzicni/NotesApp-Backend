package ch.hadzic.nikola.notesapp.util;

import ch.hadzic.nikola.notesapp.data.entity.Note;
import ch.hadzic.nikola.notesapp.service.UserService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfExportUtil {

    public static byte[] exportNotesToPdf(List<Note> notes) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Exported Notes", titleFont));
            document.add(new Paragraph(" "));

            for (Note note : notes) {
                document.add(new Paragraph("Title: " + note.getTitle(), titleFont));
                document.add(new Paragraph("Created by: " + UserService.getUsername(), bodyFont));
                document.add(new Paragraph("Content: " + note.getContent(), bodyFont));
                document.add(new Paragraph("Created at: " + DateFormatUtil.format(note.getCreatedAt()), bodyFont));
                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error during creation", e);
        }

        return out.toByteArray();
    }

    public static byte[] exportNoteToPdf(Note note) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Note", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Title: " + note.getTitle(), titleFont));
            document.add(new Paragraph("Created by: " + UserService.getUsername(), bodyFont));
            document.add(new Paragraph("Content: " + note.getContent(), bodyFont));
            document.add(new Paragraph("Created at " + DateFormatUtil.format(note.getCreatedAt()), bodyFont));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error during creation", e);
        }

        return out.toByteArray();
    }
}
