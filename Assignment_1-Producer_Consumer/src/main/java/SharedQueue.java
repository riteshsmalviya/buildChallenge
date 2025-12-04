import java.util.LinkedList;
import java.util.Queue;

/**
 * SharedQueue class implements a thread-safe bounded buffer
 * for producer-consumer synchronization using wait/notify mechanism.
 * 
 * This class demonstrates:
 * - Thread synchronization using synchronized keyword
 * - Inter-thread communication using wait() and notify()
 * - Blocking queue behavior with capacity constraints
 */
public class SharedQueue<T> {
    private final Queue<T> queue;
    private final int capacity;
    
    /**
     * Constructor to initialize the shared queue with a given capacity
     * @param capacity Maximum number of items the queue can hold
     */
    public SharedQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }
    
    /**
     * Adds an item to the queue. Blocks if queue is full.
     * Producer threads call this method to add items.
     * 
     * @param item The item to be added to the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void put(T item) throws InterruptedException {
        // Wait while queue is full
        while (queue.size() == capacity) {
            System.out.println(Thread.currentThread().getName() + " - Queue is full. Waiting...");
            wait(); // Release lock and wait for consumer to notify
        }
        
        // Add item to queue
        queue.add(item);
        System.out.println(Thread.currentThread().getName() + " - Produced: " + item + " | Queue size: " + queue.size());
        
        // Notify waiting consumer threads
        notifyAll();
    }
    
    /**
     * Removes and returns an item from the queue. Blocks if queue is empty.
     * Consumer threads call this method to retrieve items.
     * 
     * @return The item removed from the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized T take() throws InterruptedException {
        // Wait while queue is empty
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " - Queue is empty. Waiting...");
            wait(); // Release lock and wait for producer to notify
        }
        
        // Remove item from queue
        T item = queue.poll();
        System.out.println(Thread.currentThread().getName() + " - Consumed: " + item + " | Queue size: " + queue.size());
        
        // Notify waiting producer threads
        notifyAll();
        
        return item;
    }
    
    /**
     * Returns the current size of the queue
     * @return Number of items currently in the queue
     */
    public synchronized int size() {
        return queue.size();
    }
    
    /**
     * Checks if the queue is empty
     * @return true if queue is empty, false otherwise
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * Checks if the queue is full
     * @return true if queue is at capacity, false otherwise
     */
    public synchronized boolean isFull() {
        return queue.size() == capacity;
    }
    
    /**
     * Returns the maximum capacity of the queue
     * @return The capacity of the queue
     */
    public int getCapacity() {
        return capacity;
    }
}
