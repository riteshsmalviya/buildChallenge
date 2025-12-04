# Sales Analysis Application - Design Document

## Overview
A Java application that performs data analysis on sales records from CSV files using Java Streams API and functional programming paradigms.

## Architecture

### System Components

```
┌─────────────────┐
│      Main       │  Entry point & orchestration
└────────┬────────┘
         │
    ┌────┴────┐
    │         │
┌───▼───┐ ┌──▼──────┐
│Loader │ │Analyzer │
└───┬───┘ └──┬──────┘
    │        │
    │    ┌───▼────┐
    └────►  Sale  │  Data model
         └────────┘
```

### Package Structure

- **`com.salesanalysis`** - Main application entry point
- **`com.salesanalysis.model`** - Data models (Sale)
- **`com.salesanalysis.loader`** - CSV data loading utilities
- **`com.salesanalysis.analyzer`** - Stream-based analytics operations

## Core Classes

### 1. Sale (Model)
**Purpose:** Immutable data model representing a single sales transaction

**Key Attributes:**
- `transactionId` - Unique identifier
- `date` - Transaction date
- `product` - Product name
- `category` - Product category
- `quantity` - Units sold
- `unitPrice` - Price per unit
- `region` - Geographic region
- `salesperson` - Sales representative

**Key Methods:**
- `getTotalAmount()` - Calculated field (quantity × unitPrice)
- Standard getters, equals(), hashCode(), toString()

**Design Decisions:**
- Immutable (all fields final) for thread safety
- Calculated total amount instead of storing it
- Implements proper equals/hashCode for collection operations

### 2. SalesDataLoader
**Purpose:** Load and parse CSV data into Sale objects

**Key Methods:**
- `loadFromFile(Path)` - Load from file system
- `loadFromResource(String)` - Load from classpath resources
- `loadFromStrings(List<String>)` - Load from string list (testing)

**Stream Operations Used:**
- `skip(1)` - Skip CSV header
- `filter()` - Remove empty lines
- `map()` - Transform CSV lines to Sale objects

**Design Decisions:**
- Uses Streams for efficient file processing
- Handles both file and resource loading
- Throws descriptive exceptions on parsing errors

### 3. SalesAnalyzer
**Purpose:** Perform various analytical operations using Streams API

**Categories of Operations:**

#### Basic Aggregations
- `calculateTotalRevenue()` - Sum using mapToDouble + sum
- `calculateAverageOrderValue()` - Average with orElse fallback
- `countTotalTransactions()` - Simple count
- `findHighestValueSale()` - Max with comparator
- `findLowestValueSale()` - Min with comparator

#### Grouping Operations
- `calculateRevenueByCategory()` - groupingBy + summingDouble
- `calculateRevenueByRegion()` - Single-level grouping
- `calculateRevenueBySalesperson()` - Single-level grouping
- `calculateRevenueByMonth()` - TreeMap for sorted results
- `calculateRevenueByCategoryAndRegion()` - Multi-level grouping

#### Filtering Operations
- `filterSalesAboveAmount()` - Filter + collect
- `filterByCategory()` - Case-insensitive filtering
- `filterByRegion()` - Regional filtering
- `findSalesBySalesperson()` - Person-specific filtering

#### Sorting Operations
- `getTopNSalesByAmount()` - sorted + limit
- `getSalesSortedByDate()` - Comparator.comparing

#### Distinct Operations
- `getUniqueProducts()` - map + toSet
- `getUniqueCategories()` - Distinct categories
- `getUniqueRegions()` - Distinct regions

#### Advanced Analytics
- `findTopSalesperson()` - Grouping + max
- `findBestSellingProductByQuantity()` - Custom aggregation
- `calculateCategoryRevenuePercentage()` - collectingAndThen
- `getSaleAmountStatistics()` - summaryStatistics
- `partitionSalesByValue()` - partitioningBy
- `calculateMonthlyGrowthRate()` - Custom calculation

### 4. Main
**Purpose:** Application entry point and output formatting

**Responsibilities:**
- Load sales data
- Create analyzer instance
- Execute all analyses
- Format and display results

**Design Decisions:**
- Modular print methods for each analysis type
- Professional console output formatting
- Comprehensive error handling

## Data Flow

```
CSV File → SalesDataLoader → List<Sale> → SalesAnalyzer → Results
   ↓                              ↓
Stream<String>              Stream operations
   ↓                              ↓
filter/map                   group/aggregate/filter
   ↓                              ↓
List<Sale>                   Various outputs
```

## Stream API Patterns Used

### 1. Map-Reduce Pattern
```java
sales.stream()
    .mapToDouble(Sale::getTotalAmount)
    .sum()
```

### 2. Grouping Pattern
```java
sales.stream()
    .collect(Collectors.groupingBy(
        Sale::getCategory,
        Collectors.summingDouble(Sale::getTotalAmount)
    ))
```

### 3. Multi-level Grouping
```java
sales.stream()
    .collect(Collectors.groupingBy(
        Sale::getCategory,
        Collectors.groupingBy(
            Sale::getRegion,
            Collectors.summingDouble(Sale::getTotalAmount)
        )
    ))
```

### 4. Partitioning Pattern
```java
sales.stream()
    .collect(Collectors.partitioningBy(
        sale -> sale.getTotalAmount() >= threshold
    ))
```

### 5. Sorting and Limiting
```java
sales.stream()
    .sorted(Comparator.comparingDouble(Sale::getTotalAmount).reversed())
    .limit(n)
    .collect(Collectors.toList())
```

## Testing Strategy

### Unit Tests Structure
- **SaleTest** - Model validation
- **SalesDataLoaderTest** - CSV parsing and error handling
- **SalesAnalyzerTest** - All analytical operations

### Test Organization
- Nested test classes for logical grouping
- Descriptive test method names
- Sample data creation in setUp
- Edge case coverage (empty lists, invalid data)

### Coverage Areas
- Happy path scenarios
- Edge cases (empty data, nulls)
- Error conditions (invalid CSV format)
- Boundary conditions

## Design Principles Applied

### 1. Immutability
- Sale class is immutable
- Defensive copying in SalesAnalyzer constructor

### 2. Single Responsibility
- Each class has one clear purpose
- Analyzer focuses only on analysis
- Loader focuses only on loading

### 3. Functional Programming
- Pure functions (no side effects)
- Method references over lambdas where possible
- Declarative over imperative style

### 4. Separation of Concerns
- Model, loading, and analysis are separate
- Main class only handles orchestration and display

## Performance Considerations

### Current Implementation
- Sequential streams for simplicity
- In-memory processing (suitable for datasets up to ~100K records)
- No caching of intermediate results

### Potential Optimizations
- Parallel streams for large datasets
- Caching frequently-used aggregations
- Lazy evaluation where appropriate

## Error Handling

### CSV Loading
- Invalid format detection
- Missing fields handling
- Date parsing errors
- Descriptive error messages

### Analysis Operations
- Optional handling for potentially empty results
- Defensive null checks
- Division by zero prevention (orElse)

## Extensibility

### Easy to Add
- New analytical methods to SalesAnalyzer
- New data fields to Sale model
- New output formats in Main
- Additional data sources to SalesDataLoader

### Design Supports
- Adding new CSV formats
- Different aggregation strategies
- Custom collectors
- Plugin-style analytics

## Dependencies

### Runtime
- Java 17+ (for Stream API features)
- No external runtime dependencies

### Build & Test
- Maven for build management
- JUnit 5 for testing
- JaCoCo for code coverage (optional)

## Assumptions

1. CSV data is well-formed and consistent
2. All sales amounts are positive
3. Dates follow yyyy-MM-dd format
4. Dataset fits in memory
5. Single-threaded execution is sufficient

## Future Enhancements

1. **Database Integration** - Replace CSV with JDBC
2. **REST API** - Expose analytics as web services
3. **Caching Layer** - Cache expensive calculations
4. **Parallel Processing** - Use parallel streams for large datasets
5. **Export Capabilities** - Export results to PDF/Excel
6. **Real-time Analysis** - Stream processing with reactive patterns
7. **Visualization** - Add charts and graphs
8. **Custom Queries** - Dynamic query builder

## Conclusion

This design demonstrates proficiency with:
- Java Streams API
- Functional programming paradigms
- Lambda expressions
- Data aggregation techniques
- Clean code architecture
- Comprehensive testing

The modular design allows for easy maintenance and extension while maintaining code clarity and performance.
