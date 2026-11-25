package adoption.repository;

import adoption.exception.AdoptionException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/* Base class for every repository that stores its data in a CSV file. */

public abstract class CsvRepository<T> {
    private final Path filePath;

    protected CsvRepository(Path filePath) {
        this.filePath = filePath;
    }

    protected abstract String[] getHeaders();
    protected abstract String[] toRow(T item);
    protected abstract T fromRow(String[] row);

    protected List<T> loadAll() throws AdoptionException {
        List<T> items = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return items;
        }
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) { // line 0 is the header
                if (!lines.get(i).isBlank()) {
                    items.add(fromRow(parseLine(lines.get(i))));
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new AdoptionException("Could not read " + filePath.getFileName() + ".", e);
        }
        return items;
    }

    protected void saveAll(List<T> items) throws AdoptionException {
        System.out.println("DEBUG: saving to " + filePath.toAbsolutePath());
        List<String> lines = new ArrayList<>();
        lines.add(toLine(getHeaders()));
        for (T item : items) {
            lines.add(toLine(toRow(item)));
        }
        try {
            Path folder = filePath.getParent();
            if (folder != null) {
                Files.createDirectories(folder); // creates the data/ folder if missing
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AdoptionException("Could not save " + filePath.getFileName() + ".", e);
        }
    }

    private static String toLine(String[] columns) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < columns.length; i ++) {
            if (i > 0) {
                line.append(',');
            }
            line.append(escape(columns[i]));
        }
        return line.toString();
    }

    /* So it cannot break if the value has comma/quote */
    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        String clean = value.replace("\r", " ").replace("\n", " "); // one record = one line
        if (clean.contains(",") || clean.contains("\"")) {
            return "\"" + clean.replace("\"", "\"\"") + "\"";
        }
        return clean;
    }

    /* Splits one CSV line into columns */
    private static String[] parseLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (insideQuotes) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else if (c == '"') {
                    insideQuotes = false;
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                insideQuotes = true;
            } else if (c == ',') {
                columns.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        columns.add(current.toString());
        return columns.toArray(new String[0]);
    }
}