import java.util.List;

/**
 * Producer class represents a producer thread that reads items from
 * a source container and places them into a shared queue.
 * 
 * Demonstrates:
 * - Thread implementation via Runnable interface
 * - Producer side of producer-consumer pattern
 * - Blocking queue operations
 */
public class Producer<T> implements Runnable {
    private final SharedQueue<T> sharedQueue;
    private final List<T> sourceContainer;
    private final String producerName;
    private final long productionDelay; // milliseconds
    
    /**
     * Constructor for Producer
     * 
     * @param sharedQueue The shared queue to produce items into
     * @param sourceContainer The source container to read items from
     * @param producerName Name identifier for this producer
     * @param productionDelay Delay in milliseconds between productions (for simulation)
     */
    public Producer(SharedQueue<T> sharedQueue, List<T> sourceContainer, 
                   String producerName, long productionDelay) {
        this.sharedQueue = sharedQueue;
        this.sourceContainer = sourceContainer;
        this.producerName = producerName;
        this.productionDelay = productionDelay;
    }
    
    /**
     * Run method executed when thread starts.
     * Reads items from source container and produces them to shared queue.
     */
    @Override
    public void run() {
        Thread.currentThread().setName(producerName);
        
        try {
            System.out.println(producerName + " started producing...");
            
            // Iterate through source container and produce each item
            for (T item : sourceContainer) {
                sharedQueue.put(item); // Blocking call
                
                // Simulate production time
                if (productionDelay > 0) {
                    Thread.sleep(productionDelay);
                }
            }
            
            System.out.println(producerName + " finished producing all items.");
            
        } catch (InterruptedException e) {
            System.err.println(producerName + " was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
    
    /**
     * Gets the name of this producer
     * @return Producer name
     */
    public String getProducerName() {
        return producerName;
    }
}
