import java.util.List;

/**
 * Consumer class represents a consumer thread that reads items from
 * a shared queue and stores them in a destination container.
 * 
 * Demonstrates:
 * - Thread implementation via Runnable interface
 * - Consumer side of producer-consumer pattern
 * - Blocking queue operations
 */
public class Consumer<T> implements Runnable {
    private final SharedQueue<T> sharedQueue;
    private final List<T> destinationContainer;
    private final String consumerName;
    private final int itemsToConsume;
    private final long consumptionDelay; // milliseconds
    
    /**
     * Constructor for Consumer
     * 
     * @param sharedQueue The shared queue to consume items from
     * @param destinationContainer The destination container to store consumed items
     * @param consumerName Name identifier for this consumer
     * @param itemsToConsume Number of items this consumer should consume
     * @param consumptionDelay Delay in milliseconds between consumptions (for simulation)
     */
    public Consumer(SharedQueue<T> sharedQueue, List<T> destinationContainer,
                   String consumerName, int itemsToConsume, long consumptionDelay) {
        this.sharedQueue = sharedQueue;
        this.destinationContainer = destinationContainer;
        this.consumerName = consumerName;
        this.itemsToConsume = itemsToConsume;
        this.consumptionDelay = consumptionDelay;
    }
    
    /**
     * Run method executed when thread starts.
     * Consumes items from shared queue and stores them in destination container.
     */
    @Override
    public void run() {
        Thread.currentThread().setName(consumerName);
        
        try {
            System.out.println(consumerName + " started consuming...");
            
            // Consume specified number of items
            for (int i = 0; i < itemsToConsume; i++) {
                T item = sharedQueue.take(); // Blocking call
                
                // Store item in destination container (synchronized for thread safety)
                synchronized (destinationContainer) {
                    destinationContainer.add(item);
                }
                
                // Simulate consumption time
                if (consumptionDelay > 0) {
                    Thread.sleep(consumptionDelay);
                }
            }
            
            System.out.println(consumerName + " finished consuming all items.");
            
        } catch (InterruptedException e) {
            System.err.println(consumerName + " was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
    
    /**
     * Gets the name of this consumer
     * @return Consumer name
     */
    public String getConsumerName() {
        return consumerName;
    }
}
