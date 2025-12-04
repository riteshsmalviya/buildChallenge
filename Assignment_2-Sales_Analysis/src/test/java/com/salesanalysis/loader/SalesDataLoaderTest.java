package com.salesanalysis.loader;

import com.salesanalysis.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SalesDataLoader class.
 */
class SalesDataLoaderTest {

    private SalesDataLoader loader;

    @BeforeEach
    void setUp() {
        loader = new SalesDataLoader();
    }

    @Test
    @DisplayName("loadFromStrings should correctly parse CSV data")
    void testLoadFromStrings() {
        List<String> csvLines = Arrays.asList(
                "transactionId,date,product,category,quantity,unitPrice,region,salesperson",
                "TXN001,2024-01-15,Laptop,Electronics,5,999.99,North,Alice Johnson",
                "TXN002,2024-02-20,Desk Chair,Furniture,10,199.99,South,Bob Smith"
        );

        List<Sale> sales = loader.loadFromStrings(csvLines);

        assertEquals(2, sales.size());
    }

    @Test
    @DisplayName("loadFromStrings should correctly parse first sale record")
    void testLoadFromStringsFirstRecord() {
        List<String> csvLines = Arrays.asList(
                "transactionId,date,product,category,quantity,unitPrice,region,salesperson",
                "TXN001,2024-01-15,Laptop,Electronics,5,999.99,North,Alice Johnson"
        );

        List<Sale> sales = loader.loadFromStrings(csvLines);
        Sale sale = sales.get(0);

        assertEquals("TXN001", sale.getTransactionId());
        assertEquals(LocalDate.of(2024, 1, 15), sale.getDate());
        assertEquals("Laptop", sale.getProduct());
        assertEquals("Electronics", sale.getCategory());
        assertEquals(5, sale.getQuantity());
        assertEquals(999.99, sale.getUnitPrice(), 0.01);
        assertEquals("North", sale.getRegion());
        assertEquals("Alice Johnson", sale.getSalesperson());
    }

    @Test
    @DisplayName("loadFromStrings should skip empty lines")
    void testLoadFromStringsSkipsEmptyLines() {
        List<String> csvLines = Arrays.asList(
                "transactionId,date,product,category,quantity,unitPrice,region,salesperson",
                "TXN001,2024-01-15,Laptop,Electronics,5,999.99,North,Alice Johnson",
                "",
                "   ",
                "TXN002,2024-02-20,Desk Chair,Furniture,10,199.99,South,Bob Smith"
        );

        List<Sale> sales = loader.loadFromStrings(csvLines);

        assertEquals(2, sales.size());
    }

    @Test
    @DisplayName("loadFromStrings should throw exception for invalid format")
    void testLoadFromStringsInvalidFormat() {
        List<String> csvLines = Arrays.asList(
                "transactionId,date,product,category,quantity,unitPrice,region,salesperson",
                "TXN001,2024-01-15,Laptop"  // Missing fields
        );

        assertThrows(IllegalArgumentException.class, () -> loader.loadFromStrings(csvLines));
    }

    @Test
    @DisplayName("loadFromStrings should handle decimal quantities")
    void testLoadFromStringsDecimalQuantity() {
        List<String> csvLines = Arrays.asList(
                "transactionId,date,product,category,quantity,unitPrice,region,salesperson",
                "TXN001,2024-01-15,Laptop,Electronics,2.5,999.99,North,Alice Johnson"
        );

        List<Sale> sales = loader.loadFromStrings(csvLines);

        assertEquals(2.5, sales.get(0).getQuantity(), 0.01);
    }

    @Test
    @DisplayName("loadFromStrings should handle empty list after header")
    void testLoadFromStringsOnlyHeader() {
        List<String> csvLines = Arrays.asList(
                "transactionId,date,product,category,quantity,unitPrice,region,salesperson"
        );

        List<Sale> sales = loader.loadFromStrings(csvLines);

        assertTrue(sales.isEmpty());
    }

    @Test
    @DisplayName("loadFromResource should load sales_data.csv successfully")
    void testLoadFromResource() throws Exception {
        List<Sale> sales = loader.loadFromResource("sales_data.csv");

        assertFalse(sales.isEmpty());
        assertTrue(sales.size() >= 50); // We created 50 records
    }

    @Test
    @DisplayName("loadFromResource should throw exception for non-existent resource")
    void testLoadFromResourceNotFound() {
        assertThrows(Exception.class, () -> loader.loadFromResource("non_existent.csv"));
    }
}
