package adoption.utility;

import adoption.exception.AdoptionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public final class FileStorage {
    private static final Path UPLOAD_FOLDER = Path.of("uploads");

    private FileStorage() {
    }

    public static String savePhoto(String sourcePath, String prefix) throws AdoptionException {
        String source = sourcePath.trim();
        String extension = source.substring(source.lastIndexOf('.')).toLowerCase();
        String fileName = prefix + "-" + UUID.randomUUID() + extension;

        try {
            Files.createDirectories(UPLOAD_FOLDER);
            Files.copy(Path.of(source), UPLOAD_FOLDER.resolve(fileName));
            return "uploads/" + fileName; // forward slash works on every system
        } catch (IOException e) {
            throw new AdoptionException("Could not save the photo: " + sourcePath, e);
        }
    }

    public static void deletePhoto(String savedPath) {
        if (savedPath == null || savedPath.isBlank()) {
            return;
        }
        try {
            Path fileName = Path.of(savedPath.trim()).getFileName();
            Files.deleteIfExists(UPLOAD_FOLDER.resolve(fileName));
        } catch (IOException | RuntimeException e) {
        }
    }
}