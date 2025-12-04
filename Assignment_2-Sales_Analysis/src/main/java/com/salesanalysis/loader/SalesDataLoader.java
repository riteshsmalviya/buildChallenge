package com.salesanalysis.loader;

import com.salesanalysis.model.Sale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for loading sales data from CSV files.
 */
public class SalesDataLoader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String CSV_DELIMITER = ",";

    /**
     * Loads sales data from a CSV file path.
     *
     * @param filePath Path to the CSV file
     * @return List of Sale objects parsed from the CSV
     * @throws IOException if there's an error reading the file
     */
    public List<Sale> loadFromFile(Path filePath) throws IOException {
        try (Stream<String> lines = Files.lines(filePath)) {
            return parseLines(lines);
        }
    }

    /**
     * Loads sales data from a resource file in the classpath.
     *
     * @param resourceName Name of the resource file
     * @return List of Sale objects parsed from the CSV
     * @throws IOException if there's an error reading the resource
     */
    public List<Sale> loadFromResource(String resourceName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourceName);
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return parseLines(reader.lines());
        }
    }

    /**
     * Parses a stream of CSV lines into Sale objects.
     */
    private List<Sale> parseLines(Stream<String> lines) {
        return lines
                .skip(1) // Skip header row
                .filter(line -> !line.trim().isEmpty()) // Filter empty lines
                .map(this::parseLine) // Transform each line to Sale object
                .collect(Collectors.toList());
    }

    /**
     * Parses a single CSV line into a Sale object.
     *
     * @param line CSV line to parse
     * @return Sale object created from the line data
     * @throws IllegalArgumentException if the line format is invalid
     */
    private Sale parseLine(String line) {
        String[] fields = line.split(CSV_DELIMITER);
        
        if (fields.length != 8) {
            throw new IllegalArgumentException("Invalid CSV line format: expected 8 fields, got " + fields.length);
        }

        try {
            String transactionId = fields[0].trim();
            LocalDate date = LocalDate.parse(fields[1].trim(), DATE_FORMATTER);
            String product = fields[2].trim();
            String category = fields[3].trim();
            double quantity = Double.parseDouble(fields[4].trim());
            double unitPrice = Double.parseDouble(fields[5].trim());
            String region = fields[6].trim();
            String salesperson = fields[7].trim();

            return new Sale(transactionId, date, product, category, quantity, unitPrice, region, salesperson);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing CSV line: " + line, e);
        }
    }

    /**
     * Loads sales data from a list of strings (useful for testing).
     *
     * @param csvLines List of CSV lines including header
     * @return List of Sale objects
     */
    public List<Sale> loadFromStrings(List<String> csvLines) {
        return parseLines(csvLines.stream());
    }
}
