import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ProducerConsumerDemo - Main application demonstrating the Producer-Consumer pattern
 * 
 * This demo creates:
 * - A bounded shared queue with capacity
 * - Multiple producer threads reading from source containers
 * - Multiple consumer threads writing to destination containers
 * - Thread synchronization using wait/notify mechanism
 */
public class ProducerConsumerDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Producer-Consumer Pattern Demo ===\n");
        
        // Configuration
        final int QUEUE_CAPACITY = 5;
        final int TOTAL_ITEMS = 20;
        
        // Create shared queue
        SharedQueue<Integer> sharedQueue = new SharedQueue<>(QUEUE_CAPACITY);
        
        // Prepare source containers for producers
        List<Integer> source1 = new ArrayList<>();
        List<Integer> source2 = new ArrayList<>();
        
        // Split items between two producers
        for (int i = 1; i <= TOTAL_ITEMS; i++) {
            if (i % 2 == 1) {
                source1.add(i); // Odd numbers
            } else {
                source2.add(i); // Even numbers
            }
        }
        
        // Prepare destination containers for consumers (thread-safe)
        List<Integer> destination1 = Collections.synchronizedList(new ArrayList<>());
        List<Integer> destination2 = Collections.synchronizedList(new ArrayList<>());
        
        // Create producer threads
        Producer<Integer> producer1 = new Producer<>(sharedQueue, source1, "Producer-1", 100);
        Producer<Integer> producer2 = new Producer<>(sharedQueue, source2, "Producer-2", 150);
        
        // Create consumer threads
        Consumer<Integer> consumer1 = new Consumer<>(sharedQueue, destination1, "Consumer-1", 12, 200);
        Consumer<Integer> consumer2 = new Consumer<>(sharedQueue, destination2, "Consumer-2", 8, 250);
        
        // Start threads
        Thread p1 = new Thread(producer1);
        Thread p2 = new Thread(producer2);
        Thread c1 = new Thread(consumer1);
        Thread c2 = new Thread(consumer2);
        
        System.out.println("Starting threads...\n");
        
        p1.start();
        p2.start();
        c1.start();
        c2.start();
        
        // Wait for all threads to complete
        try {
            p1.join();
            p2.join();
            c1.join();
            c2.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        
        // Display results
        System.out.println("\n=== Execution Complete ===");
        System.out.println("\nFinal Statistics:");
        System.out.println("Queue capacity: " + QUEUE_CAPACITY);
        System.out.println("Total items produced: " + TOTAL_ITEMS);
        System.out.println("Consumer-1 received: " + destination1.size() + " items - " + destination1);
        System.out.println("Consumer-2 received: " + destination2.size() + " items - " + destination2);
        System.out.println("Total consumed: " + (destination1.size() + destination2.size()) + " items");
        System.out.println("\nQueue final state:");
        System.out.println("Items remaining in queue: " + sharedQueue.size());
    }
    
    /**
     * Alternative demo with String items (books)
     */
    public static void runBookStoreDemo() {
        System.out.println("=== Book Store Producer-Consumer Demo ===\n");
        
        SharedQueue<String> bookQueue = new SharedQueue<>(3);
        
        List<String> publisherBooks = Arrays.asList(
            "Uptime",
            "Crew",
            "Wants",
            "Clean",
            "Code"
        );
        
        List<String> soldBooks = Collections.synchronizedList(new ArrayList<>());
        
        Producer<String> publisher = new Producer<>(bookQueue, publisherBooks, "Publisher", 500);
        Consumer<String> bookstore = new Consumer<>(bookQueue, soldBooks, "Bookstore", 5, 700);
        
        Thread pubThread = new Thread(publisher);
        Thread storeThread = new Thread(bookstore);
        
        pubThread.start();
        storeThread.start();
        
        try {
            pubThread.join();
            storeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n=== Books Sold ===");
        soldBooks.forEach(book -> System.out.println("- " + book));
    }
}
