package com.salesanalysis.analyzer;

import com.salesanalysis.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SalesAnalyzer class.
 */
class SalesAnalyzerTest {

    private SalesAnalyzer analyzer;
    private List<Sale> testSales;

    @BeforeEach
    void setUp() {
        testSales = createTestSales();
        analyzer = new SalesAnalyzer(testSales);
    }

    /**
     * Creates a set of test sales data for consistent testing.
     */
    private List<Sale> createTestSales() {
        return Arrays.asList(
                new Sale("TXN001", LocalDate.of(2024, 1, 15), "Laptop", "Electronics", 2, 1000.00, "North", "Alice"),
                new Sale("TXN002", LocalDate.of(2024, 1, 20), "Mouse", "Electronics", 10, 25.00, "South", "Bob"),
                new Sale("TXN003", LocalDate.of(2024, 2, 10), "Desk", "Furniture", 3, 300.00, "North", "Alice"),
                new Sale("TXN004", LocalDate.of(2024, 2, 15), "Chair", "Furniture", 5, 150.00, "East", "Carol"),
                new Sale("TXN005", LocalDate.of(2024, 3, 5), "Monitor", "Electronics", 4, 400.00, "South", "Bob"),
                new Sale("TXN006", LocalDate.of(2024, 3, 20), "Bookshelf", "Furniture", 2, 200.00, "West", "David")
        );
    }

    @Nested
    class BasicAggregationTests {

        @Test
        void testCalculateTotalRevenue() {
            // (2*1000) + (10*25) + (3*300) + (5*150) + (4*400) + (2*200) = 5900
            assertEquals(5900.0, analyzer.calculateTotalRevenue(), 0.01);
        }

        @Test
        void testCalculateAverageOrderValue() {
            // 5900 / 6 = 983.33
            assertEquals(983.33, analyzer.calculateAverageOrderValue(), 0.01);
        }

        @Test
        @DisplayName("countTotalTransactions should return correct count")
        void testCountTotalTransactions() {
            assertEquals(6, analyzer.countTotalTransactions());
        }

        @Test
        @DisplayName("calculateTotalQuantitySold should return correct sum")
        void testCalculateTotalQuantitySold() {
            // 2 + 10 + 3 + 5 + 4 + 2 = 26
            assertEquals(26.0, analyzer.calculateTotalQuantitySold(), 0.01);
        }

        @Test
        @DisplayName("findHighestValueSale should return sale with maximum total")
        void testFindHighestValueSale() {
            Optional<Sale> highest = analyzer.findHighestValueSale();
            assertTrue(highest.isPresent());
            assertEquals("Laptop", highest.get().getProduct());
            assertEquals(2000.0, highest.get().getTotalAmount(), 0.01);
        }

        @Test
        @DisplayName("findLowestValueSale should return sale with minimum total")
        void testFindLowestValueSale() {
            Optional<Sale> lowest = analyzer.findLowestValueSale();
            assertTrue(lowest.isPresent());
            assertEquals("Mouse", lowest.get().getProduct());
            assertEquals(250.0, lowest.get().getTotalAmount(), 0.01);
        }
    }

    @Nested
    @DisplayName("Grouping Operations Tests")
    class GroupingTests {

        @Test
        @DisplayName("calculateRevenueByCategory should group correctly")
        void testCalculateRevenueByCategory() {
            Map<String, Double> byCategory = analyzer.calculateRevenueByCategory();
            
            assertEquals(2, byCategory.size());
            // Electronics: 2000 + 250 + 1600 = 3850
            assertEquals(3850.0, byCategory.get("Electronics"), 0.01);
            // Furniture: 900 + 750 + 400 = 2050
            assertEquals(2050.0, byCategory.get("Furniture"), 0.01);
        }

        @Test
        @DisplayName("calculateRevenueByRegion should group correctly")
        void testCalculateRevenueByRegion() {
            Map<String, Double> byRegion = analyzer.calculateRevenueByRegion();
            
            assertEquals(4, byRegion.size());
            // North: 2000 + 900 = 2900
            assertEquals(2900.0, byRegion.get("North"), 0.01);
            // South: 250 + 1600 = 1850
            assertEquals(1850.0, byRegion.get("South"), 0.01);
        }

        @Test
        @DisplayName("calculateRevenueBySalesperson should group correctly")
        void testCalculateRevenueBySalesperson() {
            Map<String, Double> bySalesperson = analyzer.calculateRevenueBySalesperson();
            
            assertEquals(4, bySalesperson.size());
            // Alice: 2000 + 900 = 2900
            assertEquals(2900.0, bySalesperson.get("Alice"), 0.01);
            // Bob: 250 + 1600 = 1850
            assertEquals(1850.0, bySalesperson.get("Bob"), 0.01);
        }

        @Test
        @DisplayName("calculateRevenueByMonth should group correctly with sorted months")
        void testCalculateRevenueByMonth() {
            Map<Month, Double> byMonth = analyzer.calculateRevenueByMonth();
            
            assertEquals(3, byMonth.size());
            // January: 2000 + 250 = 2250
            assertEquals(2250.0, byMonth.get(Month.JANUARY), 0.01);
            // February: 900 + 750 = 1650
            assertEquals(1650.0, byMonth.get(Month.FEBRUARY), 0.01);
            // March: 1600 + 400 = 2000
            assertEquals(2000.0, byMonth.get(Month.MARCH), 0.01);
        }

        @Test
        @DisplayName("countSalesByProduct should count correctly")
        void testCountSalesByProduct() {
            Map<String, Long> productCounts = analyzer.countSalesByProduct();
            
            assertEquals(6, productCounts.size());
            assertEquals(1L, productCounts.get("Laptop"));
            assertEquals(1L, productCounts.get("Mouse"));
        }

        @Test
        @DisplayName("calculateAverageOrderValueByRegion should calculate correctly")
        void testCalculateAverageOrderValueByRegion() {
            Map<String, Double> avgByRegion = analyzer.calculateAverageOrderValueByRegion();
            
            // North: (2000 + 900) / 2 = 1450
            assertEquals(1450.0, avgByRegion.get("North"), 0.01);
            // East: 750 / 1 = 750
            assertEquals(750.0, avgByRegion.get("East"), 0.01);
        }

        @Test
        @DisplayName("calculateRevenueByCategoryAndRegion should do multi-level grouping")
        void testCalculateRevenueByCategoryAndRegion() {
            Map<String, Map<String, Double>> nested = analyzer.calculateRevenueByCategoryAndRegion();
            
            assertEquals(2, nested.size());
            assertTrue(nested.containsKey("Electronics"));
            assertTrue(nested.containsKey("Furniture"));
            
            // Electronics in North: 2000
            assertEquals(2000.0, nested.get("Electronics").get("North"), 0.01);
        }
    }

    @Nested
    @DisplayName("Filtering Operations Tests")
    class FilteringTests {

        @Test
        @DisplayName("filterSalesAboveAmount should filter correctly")
        void testFilterSalesAboveAmount() {
            List<Sale> highValue = analyzer.filterSalesAboveAmount(1000);
            
            assertEquals(2, highValue.size());
            assertTrue(highValue.stream().allMatch(s -> s.getTotalAmount() > 1000));
        }

        @Test
        @DisplayName("filterByCategory should filter correctly")
        void testFilterByCategory() {
            List<Sale> electronics = analyzer.filterByCategory("Electronics");
            
            assertEquals(3, electronics.size());
            assertTrue(electronics.stream().allMatch(s -> s.getCategory().equals("Electronics")));
        }

        @Test
        @DisplayName("filterByRegion should filter correctly")
        void testFilterByRegion() {
            List<Sale> northSales = analyzer.filterByRegion("North");
            
            assertEquals(2, northSales.size());
            assertTrue(northSales.stream().allMatch(s -> s.getRegion().equals("North")));
        }

        @Test
        @DisplayName("findSalesBySalesperson should find correctly")
        void testFindSalesBySalesperson() {
            List<Sale> aliceSales = analyzer.findSalesBySalesperson("Alice");
            
            assertEquals(2, aliceSales.size());
            assertTrue(aliceSales.stream().allMatch(s -> s.getSalesperson().equals("Alice")));
        }

        @Test
        @DisplayName("filterByCategory should be case-insensitive")
        void testFilterByCategoryIgnoreCase() {
            List<Sale> electronics = analyzer.filterByCategory("ELECTRONICS");
            
            assertEquals(3, electronics.size());
        }
    }

    @Nested
    @DisplayName("Sorting Operations Tests")
    class SortingTests {

        @Test
        @DisplayName("getTopNSalesByAmount should return correct top N sales")
        void testGetTopNSalesByAmount() {
            List<Sale> top3 = analyzer.getTopNSalesByAmount(3);
            
            assertEquals(3, top3.size());
            assertEquals("Laptop", top3.get(0).getProduct()); // 2000
            assertEquals("Monitor", top3.get(1).getProduct()); // 1600
            assertEquals("Desk", top3.get(2).getProduct()); // 900
        }

        @Test
        @DisplayName("getSalesSortedByDate should return sales in date order")
        void testGetSalesSortedByDate() {
            List<Sale> sorted = analyzer.getSalesSortedByDate();
            
            assertEquals(6, sorted.size());
            assertEquals(LocalDate.of(2024, 1, 15), sorted.get(0).getDate());
            assertEquals(LocalDate.of(2024, 3, 20), sorted.get(5).getDate());
        }
    }

    @Nested
    @DisplayName("Distinct and Collection Operations Tests")
    class DistinctTests {

        @Test
        @DisplayName("getUniqueProducts should return all unique products")
        void testGetUniqueProducts() {
            Set<String> products = analyzer.getUniqueProducts();
            
            assertEquals(6, products.size());
            assertTrue(products.contains("Laptop"));
            assertTrue(products.contains("Mouse"));
            assertTrue(products.contains("Desk"));
        }

        @Test
        @DisplayName("getUniqueCategories should return all unique categories")
        void testGetUniqueCategories() {
            Set<String> categories = analyzer.getUniqueCategories();
            
            assertEquals(2, categories.size());
            assertTrue(categories.contains("Electronics"));
            assertTrue(categories.contains("Furniture"));
        }

        @Test
        @DisplayName("getUniqueRegions should return all unique regions")
        void testGetUniqueRegions() {
            Set<String> regions = analyzer.getUniqueRegions();
            
            assertEquals(4, regions.size());
            assertTrue(regions.containsAll(Arrays.asList("North", "South", "East", "West")));
        }

        @Test
        @DisplayName("getUniqueSalespersons should return all unique salespersons")
        void testGetUniqueSalespersons() {
            Set<String> salespersons = analyzer.getUniqueSalespersons();
            
            assertEquals(4, salespersons.size());
            assertTrue(salespersons.containsAll(Arrays.asList("Alice", "Bob", "Carol", "David")));
        }
    }

    @Nested
    @DisplayName("Advanced Analytics Tests")
    class AdvancedAnalyticsTests {

        @Test
        @DisplayName("findTopSalesperson should return highest revenue salesperson")
        void testFindTopSalesperson() {
            Optional<Map.Entry<String, Double>> top = analyzer.findTopSalesperson();
            
            assertTrue(top.isPresent());
            assertEquals("Alice", top.get().getKey());
            assertEquals(2900.0, top.get().getValue(), 0.01);
        }

        @Test
        @DisplayName("findBestSellingProductByQuantity should return correct product")
        void testFindBestSellingProductByQuantity() {
            Optional<Map.Entry<String, Double>> best = analyzer.findBestSellingProductByQuantity();
            
            assertTrue(best.isPresent());
            assertEquals("Mouse", best.get().getKey());
            assertEquals(10.0, best.get().getValue(), 0.01);
        }

        @Test
        @DisplayName("findBestSellingCategory should return highest revenue category")
        void testFindBestSellingCategory() {
            Optional<Map.Entry<String, Double>> best = analyzer.findBestSellingCategory();
            
            assertTrue(best.isPresent());
            assertEquals("Electronics", best.get().getKey());
            assertEquals(3850.0, best.get().getValue(), 0.01);
        }

        @Test
        @DisplayName("calculateCategoryRevenuePercentage should calculate correct percentages")
        void testCalculateCategoryRevenuePercentage() {
            Map<String, Double> percentages = analyzer.calculateCategoryRevenuePercentage();
            
            // Electronics: 3850 / 5900 * 100 = 65.25%
            assertEquals(65.25, percentages.get("Electronics"), 0.1);
            // Furniture: 2050 / 5900 * 100 = 34.75%
            assertEquals(34.75, percentages.get("Furniture"), 0.1);
        }

        @Test
        @DisplayName("getSaleAmountStatistics should return correct statistics")
        void testGetSaleAmountStatistics() {
            DoubleSummaryStatistics stats = analyzer.getSaleAmountStatistics();
            
            assertEquals(6, stats.getCount());
            assertEquals(5900.0, stats.getSum(), 0.01);
            assertEquals(250.0, stats.getMin(), 0.01);
            assertEquals(2000.0, stats.getMax(), 0.01);
            assertEquals(983.33, stats.getAverage(), 0.01);
        }

        @Test
        @DisplayName("partitionSalesByValue should partition correctly")
        void testPartitionSalesByValue() {
            Map<Boolean, List<Sale>> partitioned = analyzer.partitionSalesByValue(1000);
            
            // High value (>= 1000): Laptop (2000), Monitor (1600)
            assertEquals(2, partitioned.get(true).size());
            // Low value (< 1000): Mouse (250), Desk (900), Chair (750), Bookshelf (400)
            assertEquals(4, partitioned.get(false).size());
        }

        @Test
        @DisplayName("getAllProductsAsString should return sorted comma-separated string")
        void testGetAllProductsAsString() {
            String products = analyzer.getAllProductsAsString();
            
            // Should be alphabetically sorted and comma-separated
            assertEquals("Bookshelf, Chair, Desk, Laptop, Monitor, Mouse", products);
        }

        @Test
        @DisplayName("calculateMonthlyGrowthRate should calculate growth correctly")
        void testCalculateMonthlyGrowthRate() {
            Map<Month, Double> growth = analyzer.calculateMonthlyGrowthRate();
            
            // February: (1650 - 2250) / 2250 * 100 = -26.67%
            assertEquals(-26.67, growth.get(Month.FEBRUARY), 0.1);
            // March: (2000 - 1650) / 1650 * 100 = 21.21%
            assertEquals(21.21, growth.get(Month.MARCH), 0.1);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Empty sales list should handle gracefully")
        void testEmptySalesList() {
            SalesAnalyzer emptyAnalyzer = new SalesAnalyzer(Collections.emptyList());
            
            assertEquals(0.0, emptyAnalyzer.calculateTotalRevenue());
            assertEquals(0.0, emptyAnalyzer.calculateAverageOrderValue());
            assertEquals(0, emptyAnalyzer.countTotalTransactions());
            assertTrue(emptyAnalyzer.findHighestValueSale().isEmpty());
            assertTrue(emptyAnalyzer.findLowestValueSale().isEmpty());
            assertTrue(emptyAnalyzer.getUniqueProducts().isEmpty());
        }

        @Test
        @DisplayName("Single sale should be handled correctly")
        void testSingleSale() {
            Sale single = new Sale("TXN001", LocalDate.of(2024, 1, 1), "Product", "Category", 
                    1, 100.0, "North", "Seller");
            SalesAnalyzer singleAnalyzer = new SalesAnalyzer(Collections.singletonList(single));
            
            assertEquals(100.0, singleAnalyzer.calculateTotalRevenue(), 0.01);
            assertEquals(100.0, singleAnalyzer.calculateAverageOrderValue(), 0.01);
            assertEquals(1, singleAnalyzer.countTotalTransactions());
        }

        @Test
        @DisplayName("getSalesCount should return correct count")
        void testGetSalesCount() {
            assertEquals(6, analyzer.getSalesCount());
        }

        @Test
        @DisplayName("filterByCategory with non-existent category should return empty list")
        void testFilterByNonExistentCategory() {
            List<Sale> result = analyzer.filterByCategory("NonExistent");
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("getTopNSalesByAmount with N larger than list should return all sales")
        void testGetTopNLargerThanList() {
            List<Sale> top100 = analyzer.getTopNSalesByAmount(100);
            assertEquals(6, top100.size());
        }
    }
}
