package com.salesanalysis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Sale model class.
 */
class SaleTest {

    private Sale sale;

    @BeforeEach
    void setUp() {
        sale = new Sale(
                "TXN001",
                LocalDate.of(2024, 3, 15),
                "Laptop",
                "Electronics",
                5,
                999.99,
                "North",
                "Alice Johnson"
        );
    }

    @Test
    @DisplayName("Sale should store and return correct transaction ID")
    void testGetTransactionId() {
        assertEquals("TXN001", sale.getTransactionId());
    }

    @Test
    @DisplayName("Sale should store and return correct date")
    void testGetDate() {
        assertEquals(LocalDate.of(2024, 3, 15), sale.getDate());
    }

    @Test
    @DisplayName("Sale should store and return correct product name")
    void testGetProduct() {
        assertEquals("Laptop", sale.getProduct());
    }

    @Test
    @DisplayName("Sale should store and return correct category")
    void testGetCategory() {
        assertEquals("Electronics", sale.getCategory());
    }

    @Test
    @DisplayName("Sale should store and return correct quantity")
    void testGetQuantity() {
        assertEquals(5, sale.getQuantity());
    }

    @Test
    @DisplayName("Sale should store and return correct unit price")
    void testGetUnitPrice() {
        assertEquals(999.99, sale.getUnitPrice(), 0.01);
    }

    @Test
    @DisplayName("Sale should store and return correct region")
    void testGetRegion() {
        assertEquals("North", sale.getRegion());
    }

    @Test
    @DisplayName("Sale should store and return correct salesperson")
    void testGetSalesperson() {
        assertEquals("Alice Johnson", sale.getSalesperson());
    }

    @Test
    @DisplayName("getTotalAmount should correctly calculate quantity * unitPrice")
    void testGetTotalAmount() {
        assertEquals(4999.95, sale.getTotalAmount(), 0.01);
    }

    @Test
    @DisplayName("getMonth should return correct month value")
    void testGetMonth() {
        assertEquals(3, sale.getMonth());
    }

    @Test
    @DisplayName("getYear should return correct year value")
    void testGetYear() {
        assertEquals(2024, sale.getYear());
    }

    @Test
    @DisplayName("equals should return true for identical sales")
    void testEqualsTrue() {
        Sale sameSale = new Sale(
                "TXN001",
                LocalDate.of(2024, 3, 15),
                "Laptop",
                "Electronics",
                5,
                999.99,
                "North",
                "Alice Johnson"
        );
        assertEquals(sale, sameSale);
    }

    @Test
    @DisplayName("equals should return false for different sales")
    void testEqualsFalse() {
        Sale differentSale = new Sale(
                "TXN002",
                LocalDate.of(2024, 3, 15),
                "Mouse",
                "Electronics",
                10,
                29.99,
                "South",
                "Bob Smith"
        );
        assertNotEquals(sale, differentSale);
    }

    @Test
    @DisplayName("hashCode should be consistent with equals")
    void testHashCode() {
        Sale sameSale = new Sale(
                "TXN001",
                LocalDate.of(2024, 3, 15),
                "Laptop",
                "Electronics",
                5,
                999.99,
                "North",
                "Alice Johnson"
        );
        assertEquals(sale.hashCode(), sameSale.hashCode());
    }

    @Test
    @DisplayName("toString should contain relevant sale information")
    void testToString() {
        String str = sale.toString();
        assertTrue(str.contains("TXN001"));
        assertTrue(str.contains("Laptop"));
        assertTrue(str.contains("Electronics"));
        assertTrue(str.contains("North"));
        assertTrue(str.contains("Alice Johnson"));
    }
}
