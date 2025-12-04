package com.salesanalysis.analyzer;

import com.salesanalysis.model.Sale;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Performs various analytical operations on sales data using Java Streams API.
 */
public class SalesAnalyzer {

    private final List<Sale> sales;

    /**
     * Constructs a SalesAnalyzer with the given sales data.
     */
    public SalesAnalyzer(List<Sale> sales) {
        this.sales = new ArrayList<>(sales);
    }

    // Basic aggregations

    /**
     * Calculates the total revenue from all sales.
     */
    public double calculateTotalRevenue() {
        return sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();
    }

    /**
     * Calculates the average sale amount.
     */
    public double calculateAverageOrderValue() {
        return sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .average()
                .orElse(0.0);
    }

    /**
     * Counts total transactions.
     */
    public long countTotalTransactions() {
        return sales.stream().count();
    }

    /**
     * Gets total quantity sold.
     */
    public double calculateTotalQuantitySold() {
        return sales.stream()
                .mapToDouble(Sale::getQuantity)
                .sum();
    }

    /**
     * Finds the biggest sale.
     */
    public Optional<Sale> findHighestValueSale() {
        return sales.stream()
                .max(Comparator.comparingDouble(Sale::getTotalAmount));
    }

    /**
     * Finds the smallest sale.
     */
    public Optional<Sale> findLowestValueSale() {
        return sales.stream()
                .min(Comparator.comparingDouble(Sale::getTotalAmount));
    }

    // Grouping stuff

    /**
     * Groups revenue by category.
     */
    public Map<String, Double> calculateRevenueByCategory() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.summingDouble(Sale::getTotalAmount)
                ));
    }

    /**
     * Groups revenue by region.
     */
    public Map<String, Double> calculateRevenueByRegion() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getRegion,
                        Collectors.summingDouble(Sale::getTotalAmount)
                ));
    }

    /**
     * Groups revenue by salesperson.
     */
    public Map<String, Double> calculateRevenueBySalesperson() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getSalesperson,
                        Collectors.summingDouble(Sale::getTotalAmount)
                ));
    }

    /**
     * Monthly revenue breakdown.
     */
    public Map<Month, Double> calculateRevenueByMonth() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> Month.of(sale.getMonth()),
                        TreeMap::new,
                        Collectors.summingDouble(Sale::getTotalAmount)
                ));
    }

    /**
     * Groups sales by product and counts occurrences.
     *
     * @return Map of product to count
     */
    public Map<String, Long> countSalesByProduct() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getProduct,
                        Collectors.counting()
                ));
    }

    /**
     * Groups sales by region and calculates average order value for each.
     *
     * @return Map of region to average order value
     */
    public Map<String, Double> calculateAverageOrderValueByRegion() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getRegion,
                        Collectors.averagingDouble(Sale::getTotalAmount)
                ));
    }

    /**
     * Multi-level grouping: category then region.
     */
    public Map<String, Map<String, Double>> calculateRevenueByCategoryAndRegion() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.groupingBy(
                                Sale::getRegion,
                                Collectors.summingDouble(Sale::getTotalAmount)
                        )
                ));
    }

    // Filtering methods

    /**
     * Get sales above a certain amount.
     */
    public List<Sale> filterSalesAboveAmount(double minAmount) {
        return sales.stream()
                .filter(sale -> sale.getTotalAmount() > minAmount)
                .collect(Collectors.toList());
    }

    /**
     * Filters sales by category.
     *
     * @param category Category to filter by
     * @return List of sales in the specified category
     */
    public List<Sale> filterByCategory(String category) {
        return sales.stream()
                .filter(sale -> sale.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filters sales by region.
     *
     * @param region Region to filter by
     * @return List of sales in the specified region
     */
    public List<Sale> filterByRegion(String region) {
        return sales.stream()
                .filter(sale -> sale.getRegion().equalsIgnoreCase(region))
                .collect(Collectors.toList());
    }

    /**
     * Finds sales by a specific salesperson.
     *
     * @param salesperson Name of the salesperson
     * @return List of sales by the salesperson
     */
    public List<Sale> findSalesBySalesperson(String salesperson) {
        return sales.stream()
                .filter(sale -> sale.getSalesperson().equalsIgnoreCase(salesperson))
                .collect(Collectors.toList());
    }

    // ==================== SORTING OPERATIONS ====================

    /**
     * Gets top N sales by total amount.
     *
     * @param n Number of top sales to return
     * @return List of top N sales sorted by amount descending
     */
    public List<Sale> getTopNSalesByAmount(int n) {
        return sales.stream()
                .sorted(Comparator.comparingDouble(Sale::getTotalAmount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Gets sales sorted by date.
     *
     * @return List of sales sorted by date ascending
     */
    public List<Sale> getSalesSortedByDate() {
        return sales.stream()
                .sorted(Comparator.comparing(Sale::getDate))
                .collect(Collectors.toList());
    }

    // ==================== DISTINCT AND COLLECTION OPERATIONS ====================

    /**
     * Gets all unique product names.
     *
     * @return Set of unique product names
     */
    public Set<String> getUniqueProducts() {
        return sales.stream()
                .map(Sale::getProduct)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all unique categories.
     *
     * @return Set of unique categories
     */
    public Set<String> getUniqueCategories() {
        return sales.stream()
                .map(Sale::getCategory)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all unique regions.
     *
     * @return Set of unique regions
     */
    public Set<String> getUniqueRegions() {
        return sales.stream()
                .map(Sale::getRegion)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all unique salespersons.
     *
     * @return Set of unique salesperson names
     */
    public Set<String> getUniqueSalespersons() {
        return sales.stream()
                .map(Sale::getSalesperson)
                .collect(Collectors.toSet());
    }

    // ==================== ADVANCED ANALYTICS ====================

    /**
     * Finds the top performing salesperson by total revenue.
     *
     * @return Optional containing entry of top salesperson and their revenue
     */
    public Optional<Map.Entry<String, Double>> findTopSalesperson() {
        return calculateRevenueBySalesperson().entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    /**
     * Finds the best-selling product by quantity.
     *
     * @return Optional containing entry of best-selling product and quantity
     */
    public Optional<Map.Entry<String, Double>> findBestSellingProductByQuantity() {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getProduct,
                        Collectors.summingDouble(Sale::getQuantity)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    /**
     * Finds the best-selling category by revenue.
     *
     * @return Optional containing entry of best category and revenue
     */
    public Optional<Map.Entry<String, Double>> findBestSellingCategory() {
        return calculateRevenueByCategory().entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    /**
     * Calculates the percentage contribution of each category to total revenue.
     *
     * @return Map of category to percentage contribution
     */
    public Map<String, Double> calculateCategoryRevenuePercentage() {
        double totalRevenue = calculateTotalRevenue();
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.summingDouble(Sale::getTotalAmount),
                                categoryRevenue -> (categoryRevenue / totalRevenue) * 100
                        )
                ));
    }

    /**
     * Calculates statistics (count, sum, min, max, avg) for sale amounts.
     *
     * @return DoubleSummaryStatistics for sale amounts
     */
    public DoubleSummaryStatistics getSaleAmountStatistics() {
        return sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .summaryStatistics();
    }

    /**
     * Partitions sales into high-value and low-value based on a threshold.
     *
     * @param threshold The amount threshold for partitioning
     * @return Map with true key for high-value sales, false for low-value
     */
    public Map<Boolean, List<Sale>> partitionSalesByValue(double threshold) {
        return sales.stream()
                .collect(Collectors.partitioningBy(
                        sale -> sale.getTotalAmount() >= threshold
                ));
    }

    /**
     * Creates a summary string of all products sold, concatenated.
     *
     * @return Comma-separated string of all products
     */
    public String getAllProductsAsString() {
        return sales.stream()
                .map(Sale::getProduct)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
    }

    /**
     * Monthly growth rate calculation.
     * TODO: This could be optimized with parallel streams for large datasets
     */
    public Map<Month, Double> calculateMonthlyGrowthRate() {
        Map<Month, Double> monthlyRevenue = calculateRevenueByMonth();
        Map<Month, Double> growthRates = new TreeMap<>();

        Double previousRevenue = null;
        for (Map.Entry<Month, Double> entry : monthlyRevenue.entrySet()) {
            if (previousRevenue != null && previousRevenue != 0) {
                double growthRate = ((entry.getValue() - previousRevenue) / previousRevenue) * 100;
                growthRates.put(entry.getKey(), growthRate);
            }
            previousRevenue = entry.getValue();
        }

        return growthRates;
    }

    /**
     * Gets the number of sales in the analyzer.
     *
     * @return Number of sales
     */
    public int getSalesCount() {
        return sales.size();
    }
}
