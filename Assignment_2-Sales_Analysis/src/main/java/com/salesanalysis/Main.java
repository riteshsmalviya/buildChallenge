package com.salesanalysis;

import com.salesanalysis.analyzer.SalesAnalyzer;
import com.salesanalysis.loader.SalesDataLoader;
import com.salesanalysis.model.Sale;

import java.io.IOException;
import java.time.Month;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

/**
 * Main application class for Sales Analysis.
 */
public class Main {

    private static final String SEPARATOR = "=====================================";
    private static final String SUBSEPARATOR = "------------------------------";

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("SALES DATA ANALYSIS APPLICATION");
        System.out.println("Using Java Streams & Functional Programming");
        System.out.println(SEPARATOR);
        System.out.println();

        try {
            // Load sales data from CSV
            SalesDataLoader loader = new SalesDataLoader();
            List<Sale> sales = loader.loadFromResource("sales_data.csv");

            System.out.println("✓ Successfully loaded " + sales.size() + " sales records from CSV");
            System.out.println();

            // Create analyzer
            SalesAnalyzer analyzer = new SalesAnalyzer(sales);

            // Run all analyses
            printBasicMetrics(analyzer);
            printCategoryAnalysis(analyzer);
            printRegionalAnalysis(analyzer);
            printSalespersonAnalysis(analyzer);
            printMonthlyAnalysis(analyzer);
            printTopSalesAnalysis(analyzer);
            printProductAnalysis(analyzer);
            printStatisticalSummary(analyzer);

            System.out.println(SEPARATOR);
            System.out.println("ANALYSIS COMPLETE");
            System.out.println(SEPARATOR);

        } catch (IOException e) {
            System.err.println("Error loading sales data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prints basic sales metrics.
     */
    private static void printBasicMetrics(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("1. BASIC SALES METRICS");
        System.out.println(SEPARATOR);

        System.out.printf("   Total Transactions: %d%n", analyzer.countTotalTransactions());
        System.out.printf("   Total Revenue: $%,.2f%n", analyzer.calculateTotalRevenue());
        System.out.printf("   Average Order Value: $%,.2f%n", analyzer.calculateAverageOrderValue());
        System.out.printf("   Total Quantity Sold: %,.0f units%n", analyzer.calculateTotalQuantitySold());

        // Show highest/lowest sales - kinda interesting
        analyzer.findHighestValueSale().ifPresent(sale ->
            System.out.printf("   Highest Value Sale: %s ($%,.2f)%n",
                sale.getProduct(), sale.getTotalAmount()));

        analyzer.findLowestValueSale().ifPresent(sale ->
            System.out.printf("   Lowest Value Sale: %s ($%,.2f)%n",
                sale.getProduct(), sale.getTotalAmount()));

        System.out.println();
    }

    /**
     * Prints category-wise analysis.
     */
    private static void printCategoryAnalysis(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("2. CATEGORY ANALYSIS");
        System.out.println(SEPARATOR);
        
        System.out.println("   Revenue by Category:");
        System.out.println(SUBSEPARATOR);
        Map<String, Double> categoryRevenue = analyzer.calculateRevenueByCategory();
        categoryRevenue.forEach((category, revenue) -> 
            System.out.printf("   %-15s $%,12.2f%n", category, revenue));
        
        System.out.println();
        System.out.println("   Category Revenue Percentage:");
        System.out.println(SUBSEPARATOR);
        Map<String, Double> categoryPercentage = analyzer.calculateCategoryRevenuePercentage();
        categoryPercentage.forEach((category, percentage) -> 
            System.out.printf("   %-15s %6.2f%%%n", category, percentage));
        
        analyzer.findBestSellingCategory().ifPresent(entry ->
            System.out.printf("%n   ★ Best Selling Category: %s ($%,.2f)%n", 
                entry.getKey(), entry.getValue()));
        
        System.out.println();
    }

    /**
     * Prints regional analysis.
     */
    private static void printRegionalAnalysis(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("3. REGIONAL ANALYSIS");
        System.out.println(SEPARATOR);
        
        System.out.println("   Revenue by Region:");
        System.out.println(SUBSEPARATOR);
        Map<String, Double> regionRevenue = analyzer.calculateRevenueByRegion();
        regionRevenue.forEach((region, revenue) -> 
            System.out.printf("   %-10s $%,12.2f%n", region, revenue));
        
        System.out.println();
        System.out.println("   Average Order Value by Region:");
        System.out.println(SUBSEPARATOR);
        Map<String, Double> regionAvg = analyzer.calculateAverageOrderValueByRegion();
        regionAvg.forEach((region, avg) -> 
            System.out.printf("   %-10s $%,10.2f%n", region, avg));
        
        System.out.println();
        System.out.println("   Revenue by Category and Region:");
        System.out.println(SUBSEPARATOR);
        Map<String, Map<String, Double>> categoryRegion = analyzer.calculateRevenueByCategoryAndRegion();
        categoryRegion.forEach((category, regions) -> {
            System.out.printf("   %s:%n", category);
            regions.forEach((region, revenue) -> 
                System.out.printf("      %-8s $%,10.2f%n", region, revenue));
        });
        
        System.out.println();
    }

    /**
     * Prints salesperson performance analysis.
     */
    private static void printSalespersonAnalysis(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("4. SALESPERSON PERFORMANCE");
        System.out.println(SEPARATOR);
        
        System.out.println("   Revenue by Salesperson:");
        System.out.println(SUBSEPARATOR);
        Map<String, Double> salespersonRevenue = analyzer.calculateRevenueBySalesperson();
        salespersonRevenue.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("   %-18s $%,12.2f%n", entry.getKey(), entry.getValue()));
        
        analyzer.findTopSalesperson().ifPresent(entry ->
            System.out.printf("%n   ★ Top Performer: %s ($%,.2f)%n", 
                entry.getKey(), entry.getValue()));
        
        System.out.println();
    }

    /**
     * Prints monthly sales analysis.
     */
    private static void printMonthlyAnalysis(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("5. MONTHLY ANALYSIS");
        System.out.println(SEPARATOR);
        
        System.out.println("   Revenue by Month:");
        System.out.println(SUBSEPARATOR);
        Map<Month, Double> monthlyRevenue = analyzer.calculateRevenueByMonth();
        monthlyRevenue.forEach((month, revenue) -> 
            System.out.printf("   %-12s $%,12.2f%n", month, revenue));
        
        System.out.println();
        System.out.println("   Monthly Growth Rate:");
        System.out.println(SUBSEPARATOR);
        Map<Month, Double> growthRates = analyzer.calculateMonthlyGrowthRate();
        growthRates.forEach((month, rate) -> 
            System.out.printf("   %-12s %+7.2f%%%n", month, rate));
        
        System.out.println();
    }

    /**
     * Prints top sales analysis.
     */
    private static void printTopSalesAnalysis(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("6. TOP 5 SALES");
        System.out.println(SEPARATOR);
        
        List<Sale> topSales = analyzer.getTopNSalesByAmount(5);
        int rank = 1;
        for (Sale sale : topSales) {
            System.out.printf("   %d. %s - %s ($%,.2f)%n", 
                rank++, sale.getProduct(), sale.getSalesperson(), sale.getTotalAmount());
        }
        
        System.out.println();
    }

    /**
     * Prints product analysis.
     */
    private static void printProductAnalysis(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("7. PRODUCT ANALYSIS");
        System.out.println(SEPARATOR);
        
        System.out.println("   Unique Categories: " + analyzer.getUniqueCategories());
        System.out.println("   Unique Regions: " + analyzer.getUniqueRegions());
        System.out.println("   Total Unique Products: " + analyzer.getUniqueProducts().size());
        
        System.out.println();
        System.out.println("   All Products (sorted):");
        System.out.println("   " + analyzer.getAllProductsAsString());
        
        analyzer.findBestSellingProductByQuantity().ifPresent(entry ->
            System.out.printf("%n   ★ Best Selling Product (by quantity): %s (%.0f units)%n", 
                entry.getKey(), entry.getValue()));
        
        System.out.println();
    }

    /**
     * Prints statistical summary.
     */
    private static void printStatisticalSummary(SalesAnalyzer analyzer) {
        System.out.println(SEPARATOR);
        System.out.println("8. STATISTICAL SUMMARY");
        System.out.println(SEPARATOR);
        
        DoubleSummaryStatistics stats = analyzer.getSaleAmountStatistics();
        System.out.printf("   Count: %d%n", stats.getCount());
        System.out.printf("   Sum: $%,.2f%n", stats.getSum());
        System.out.printf("   Min: $%,.2f%n", stats.getMin());
        System.out.printf("   Max: $%,.2f%n", stats.getMax());
        System.out.printf("   Average: $%,.2f%n", stats.getAverage());
        
        System.out.println();
        System.out.println("   Sales Partitioned by Value (threshold: $1000):");
        System.out.println(SUBSEPARATOR);
        Map<Boolean, List<Sale>> partitioned = analyzer.partitionSalesByValue(1000);
        System.out.printf("   High-value sales (>=$1000): %d%n", partitioned.get(true).size());
        System.out.printf("   Low-value sales (<$1000): %d%n", partitioned.get(false).size());
        
        System.out.println();
    }
}
