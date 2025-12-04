# Sales Analysis Application

## Demo Video

[![Sales Analysis Demo](https://img.shields.io/badge/Demo-Video-blue)](Design_Working/SalesAnalysis_Ritesh.mp4)

[Watch the demo video](Design_Working/SalesAnalysis_Ritesh.mp4)

## Project Overview

This application demonstrates:
- **Functional Programming** paradigms in Java
- **Stream Operations** (map, filter, reduce, collect)
- **Data Aggregation** (sum, average, count, min, max)
- **Lambda Expressions** for data processing
- **Collectors** for grouping, partitioning, and statistical analysis

## Project Structure

```
Assignment_2-Sales_Analysis/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/salesanalysis/
    │   │       ├── Main.java                    # Entry point
    │   │       ├── model/
    │   │       │   └── Sale.java                # Data model
    │   │       ├── loader/
    │   │       │   └── SalesDataLoader.java     # CSV loading
    │   │       └── analyzer/
    │   │           └── SalesAnalyzer.java       # Stream-based analytics
    │   └── resources/
    │       └── sales_data.csv                   # Sample data
    └── test/
        └── java/
            └── com/salesanalysis/
                ├── model/
                │   └── SaleTest.java
                ├── loader/
                │   └── SalesDataLoaderTest.java
                └── analyzer/
                    └── SalesAnalyzerTest.java
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/riteshsmalviya/buildChallenge.git
cd Assignment_2-Sales_Analysis
```

### 2. Build the Project

```bash
mvn clean compile
```

### 3. Run Tests

```bash
mvn test
```

### 4. Run the Application

```bash
mvn exec:java -Dexec.mainClass="com.salesanalysis.Main"
```

Or package and run as JAR:

```bash
mvn package
java -jar target/sales-analysis-1.0-SNAPSHOT.jar
```

## Features & Analysis Methods

### Aggregation Operations
| Method | Description |
|--------|-------------|
| `calculateTotalRevenue()` | Sum of all sales amounts |
| `calculateAverageOrderValue()` | Average sale amount |
| `countTotalTransactions()` | Total number of transactions |
| `calculateTotalQuantitySold()` | Total quantity sold |
| `findHighestValueSale()` | Sale with maximum amount |
| `findLowestValueSale()` | Sale with minimum amount |

### Grouping Operations
| Method | Description |
|--------|-------------|
| `calculateRevenueByCategory()` | Revenue grouped by category |
| `calculateRevenueByRegion()` | Revenue grouped by region |
| `calculateRevenueBySalesperson()` | Revenue grouped by salesperson |
| `calculateRevenueByMonth()` | Revenue grouped by month |
| `countSalesByProduct()` | Transaction count per product |
| `calculateAverageOrderValueByRegion()` | Average order value by region |
| `calculateRevenueByCategoryAndRegion()` | Multi-level grouping |

### Filtering & Searching
| Method | Description |
|--------|-------------|
| `filterSalesAboveAmount(double)` | Filter by minimum amount |
| `filterByCategory(String)` | Filter by category |
| `filterByRegion(String)` | Filter by region |
| `findSalesBySalesperson(String)` | Find sales by salesperson |

### Advanced Analytics
| Method | Description |
|--------|-------------|
| `findTopSalesperson()` | Highest revenue salesperson |
| `findBestSellingProductByQuantity()` | Top product by quantity |
| `findBestSellingCategory()` | Top category by revenue |
| `calculateCategoryRevenuePercentage()` | Revenue % by category |
| `getSaleAmountStatistics()` | Complete statistical summary |
| `partitionSalesByValue(double)` | Partition by threshold |
| `calculateMonthlyGrowthRate()` | Month-over-month growth |

## Sample Output

```
=====================================
SALES DATA ANALYSIS APPLICATION
Using Java Streams & Functional Programming
=====================================

✓ Successfully loaded 50 sales records from CSV

=====================================
1. BASIC SALES METRICS
=====================================
   Total Transactions: 50
   Total Revenue: $111,956.68
   Average Order Value: $2,239.13
   Total Quantity Sold: 832 units
   Highest Value Sale: Smartphone ($11,999.85)
   Lowest Value Sale: Coffee Table ($539.97)

=====================================
2. CATEGORY ANALYSIS
=====================================
   Revenue by Category:
------------------------------
   Electronics     $   80,179.40
   Furniture       $   31,777.28

   Category Revenue Percentage:
------------------------------
   Electronics      71.62%
   Furniture        28.38%

   ★ Best Selling Category: Electronics ($80,179.40)

=====================================
3. REGIONAL ANALYSIS
=====================================
   Revenue by Region:
------------------------------
   West       $   17,968.88
   South      $   15,358.17
   North      $   41,142.29
   East       $   37,487.34

   Average Order Value by Region:
------------------------------
   West       $  1,497.41
   South      $  1,181.40
   North      $  3,164.79
   East       $  3,123.95

   Revenue by Category and Region:
------------------------------
   Electronics:
      West     $  2,799.92
      South    $    749.75
      North    $ 41,142.29
      East     $ 35,487.44
   Furniture:
      West     $ 15,168.96
      South    $ 14,608.42
      East     $  1,999.90

=====================================
4. SALESPERSON PERFORMANCE
=====================================
   Revenue by Salesperson:
------------------------------
   Carol White        $   40,848.87
   Emma Davis         $   19,858.46
   Alice Johnson      $   17,922.30
   David Brown        $   15,049.20
   Frank Miller       $   10,409.36
   Bob Smith          $    7,868.49

   ★ Top Performer: Carol White ($40,848.87)

=====================================
5. MONTHLY ANALYSIS
=====================================
   Revenue by Month:
------------------------------
   JANUARY      $   13,699.17
   FEBRUARY     $   14,089.35
   MARCH        $   15,048.36
   APRIL        $   20,498.79
   MAY          $   12,878.74
   JUNE         $    7,899.10
   JULY         $   12,043.95
   AUGUST       $    9,499.40
   SEPTEMBER    $    6,299.82

   Monthly Growth Rate:
------------------------------
   FEBRUARY       +2.85%
   MARCH          +6.81%
   APRIL         +36.22%
   MAY           -37.17%
   JUNE          -38.67%
   JULY          +52.47%
   AUGUST        -21.13%
   SEPTEMBER     -33.68%

=====================================
6. TOP 5 SALES
=====================================
   1. Smartphone - Carol White ($11,999.85)
   2. Smart Watch - Carol White ($5,399.82)
   3. Headphones - Emma Davis ($5,249.65)
   4. Laptop - Alice Johnson ($4,999.95)
   5. Action Camera - Carol White ($4,899.86)

=====================================
7. PRODUCT ANALYSIS
=====================================
   Unique Categories: [Electronics, Furniture]
   Unique Regions: [West, South, North, East]
   Total Unique Products: 49

   All Products (sorted):
   Action Camera, Bed Frame, Bluetooth Speaker, Bookshelf, Coat Rack, Coffee Table, Cooling Pad, Desk, Desk Chair, Desk Lamp, Desk Organizer, Dining Chair, Dining Table, Docking Station, Dresser, Ergonomic Chair, Ethernet Switch, External SSD, Filing Cabinet, Floor Mat, Gaming Mouse, Graphics Card, Headphones, Keyboard, Laptop, Magazine Rack, Mechanical Keyboard, Monitor, Monitor Stand, Mouse, Nightstand, Office Chair, Portable Monitor, Power Bank, Printer, RAM Module, Shoe Rack, Side Table, Smart Watch, Smartphone, Standing Desk, Storage Box, TV Stand, Tablet, USB Hub, Wall Shelf, Wardrobe, Webcam, Wireless Charger

   ★ Best Selling Product (by quantity): USB Hub (50 units)

=====================================
8. STATISTICAL SUMMARY
=====================================
   Count: 50
   Sum: $111,956.68
   Min: $539.97
   Max: $11,999.85
   Average: $2,239.13

   Sales Partitioned by Value (threshold: $1000):
------------------------------
   High-value sales (>=$1000): 41
   Low-value sales (<$1000): 9

=====================================
ANALYSIS COMPLETE
=====================================
```

## Unit Tests

The project includes comprehensive unit tests covering:

- **SaleTest** - Model class validation
- **SalesDataLoaderTest** - CSV parsing and loading
- **SalesAnalyzerTest** - All analytical methods including:
  - Aggregation operations
  - Grouping operations
  - Filtering operations
  - Sorting operations
  - Distinct/collection operations
  - Advanced analytics
  - Edge cases (empty lists, single items)

Run tests with verbose output:
```bash
mvn test -Dtest=SalesAnalyzerTest
```

## Key Stream API Concepts Demonstrated

### Lambda Expressions
```java
sales.stream()
    .filter(sale -> sale.getTotalAmount() > 1000)
    .collect(Collectors.toList());
```

### Method References
```java
sales.stream()
    .mapToDouble(Sale::getTotalAmount)
    .sum();
```

### Collectors with Downstream Operations
```java
sales.stream()
    .collect(Collectors.groupingBy(
        Sale::getCategory,
        Collectors.summingDouble(Sale::getTotalAmount)
    ));
```

### Multi-level Grouping
```java
sales.stream()
    .collect(Collectors.groupingBy(
        Sale::getCategory,
        Collectors.groupingBy(
            Sale::getRegion,
            Collectors.summingDouble(Sale::getTotalAmount)
        )
    ));
```

### Partitioning
```java
sales.stream()
    .collect(Collectors.partitioningBy(
        sale -> sale.getTotalAmount() >= threshold
    ));
```

### Author
**Ritesh Malviya**  
Build Challenge - Assignment 2
---
