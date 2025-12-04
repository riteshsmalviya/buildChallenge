package com.salesanalysis.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a single sale transaction record.
 * This class is immutable and serves as the data model for sales analysis.
 */
public class Sale {
    private final String transactionId;
    private final LocalDate date;
    private final String product;
    private final String category;
    private final double quantity;
    private final double unitPrice;
    private final String region;
    private final String salesperson;

    /**
     * Constructs a new Sale instance.
     *
     * @param transactionId Unique identifier for the transaction
     * @param date          Date of the sale
     * @param product       Name of the product sold
     * @param category      Category of the product
     * @param quantity      Quantity sold
     * @param unitPrice     Price per unit
     * @param region        Geographic region of the sale
     * @param salesperson   Name of the salesperson
     */
    public Sale(String transactionId, LocalDate date, String product, String category,
                double quantity, double unitPrice, String region, String salesperson) {
        this.transactionId = transactionId;
        this.date = date;
        this.product = product;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.region = region;
        this.salesperson = salesperson;
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProduct() {
        return product;
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getRegion() {
        return region;
    }

    public String getSalesperson() {
        return salesperson;
    }

    /**
     * Calculates the total amount for this sale.
     *
     * @return The total amount (quantity * unitPrice)
     */
    public double getTotalAmount() {
        return quantity * unitPrice;
    }

    /**
     * Gets the month of the sale.
     *
     * @return The month value (1-12)
     */
    public int getMonth() {
        return date.getMonthValue();
    }

    /**
     * Gets the year of the sale.
     *
     * @return The year value
     */
    public int getYear() {
        return date.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Double.compare(sale.quantity, quantity) == 0 &&
                Double.compare(sale.unitPrice, unitPrice) == 0 &&
                Objects.equals(transactionId, sale.transactionId) &&
                Objects.equals(date, sale.date) &&
                Objects.equals(product, sale.product) &&
                Objects.equals(category, sale.category) &&
                Objects.equals(region, sale.region) &&
                Objects.equals(salesperson, sale.salesperson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, date, product, category, quantity, unitPrice, region, salesperson);
    }

    @Override
    public String toString() {
        return String.format("Sale{id='%s', date=%s, product='%s', category='%s', qty=%.0f, price=%.2f, region='%s', salesperson='%s'}",
                transactionId, date, product, category, quantity, unitPrice, region, salesperson);
    }
}
