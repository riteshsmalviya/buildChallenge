# Producer-Consumer Pattern Implementation

## Assignment 1: Producer-Consumer with Thread Synchronization

### Demo Video
[![Producer-Consumer Demo](https://img.shields.io/badge/Watch-Demo%20Video-red?style=for-the-badge&logo=youtube)](Working/ProducerConsumer_Ritesh.mp4)

📹 **[View Demo Video: ProducerConsumer_Ritesh.mp4](Working/ProducerConsumer_Ritesh.mp4)**

### Overview
This project implements the classic **Producer-Consumer pattern** demonstrating thread synchronization and communication in Java. The program simulates concurrent data transfer between producer threads that place items into a shared queue and consumer threads that retrieve items from the queue.

### Key Features
- Thread-safe shared queue with bounded capacity
- Producer threads that read from source containers
- Consumer threads that write to destination containers
- Thread synchronization using `synchronized` keyword
- Inter-thread communication using `wait()` and `notify()`
- Blocking queue behavior with capacity constraints
- Unit tests (20 test cases)

### Files Included

#### Core Implementation
1. **SharedQueue.java** - Thread-safe bounded buffer with blocking operations
2. **Producer.java** - Producer thread that adds items to the queue
3. **Consumer.java** - Consumer thread that removes items from the queue
4. **ProducerConsumerDemo.java** - Main Java Class

#### Testing
5. **ProducerConsumerTest.java** - Unit test

### Testing Objectives Covered
1. **Thread Synchronization** - Used synchronized methods and wait/notify
2. **Concurrent Programming** - Multiple producers and consumers working simultaneously
3. **Blocking Queues** - Queue blocks when full or empty
4. **Wait/Notify Mechanism** - Proper inter-thread communication

### How to Compile and Run

#### Compile All Files
```cmd
javac -d . src/main/java/*.java test/*.java
```

#### Run the Demo Application
```cmd
java ProducerConsumerDemo
```

Expected output shows:
- Producers adding items to the queue
- Consumers removing items from the queue
- Queue blocking when full or empty
- Final Results

#### Run Unit Tests
```cmd
java ProducerConsumerTest
```

Expected output shows:
- 20 Unit tests
- Thread synchronization tests
- Concurrent access tests
- Edge case validation
- Test success rate

### Architecture

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Producer   │ ──put──>│ SharedQueue  │──take──>│   Consumer   │
│   Thread 1   │         │  (Bounded)   │         │   Thread 1   │
└──────────────┘         │              │         └──────────────┘
                         │   Capacity   │
┌──────────────┐         │   Blocking   │         ┌──────────────┐
│   Producer   │ ──put──>│ wait/notify  │──take──>│   Consumer   │
│   Thread 2   │         │              │         │   Thread 2   │
└──────────────┘         └──────────────┘         └──────────────┘
```

### Key Concepts Demonstrated

#### 1. Thread Synchronization
- All critical sections are properly synchronized
- Prevents race conditions and data corruption
- Uses `synchronized` keyword on methods

#### 2. Wait/Notify Mechanism
- Producers wait when queue is full
- Consumers wait when queue is empty
- Threads notify each other when conditions change

#### 3. Blocking Queue Behavior
- `put()` blocks if queue is full
- `take()` blocks if queue is empty
- Automatic thread coordination

#### 4. Concurrent Programming
- Multiple threads access shared resources safely
- Demonstrates proper thread lifecycle management
- Thread-safe collections used where needed

### Example Usage

```java
// Create shared queue with capacity 5
SharedQueue<Integer> queue = new SharedQueue<>(5);

// Create source and destination
List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> destination = Collections.synchronizedList(new ArrayList<>());

// Create and start producer
Producer<Integer> producer = new Producer<>(queue, source, "Producer-1", 100);
Thread producerThread = new Thread(producer);
producerThread.start();

// Create and start consumer
Consumer<Integer> consumer = new Consumer<>(queue, destination, "Consumer-1", 5, 200);
Thread consumerThread = new Thread(consumer);
consumerThread.start();

// Wait for completion
producerThread.join();
consumerThread.join();
```

### Test Coverage

The test suite includes 20 comprehensive tests (12 positive + 8 negative):

**Positive Test Cases:**
1. SharedQueue basic operations
2. SharedQueue capacity constraints
3. SharedQueue thread safety
4. Producer basic functionality
5. Consumer basic functionality
6. Single producer single consumer
7. Multiple producers multiple consumers
8. Blocking behavior when queue is full
9. Wait/Notify mechanism
10. Thread synchronization correctness
11. Concurrent access stress test
12. Edge cases

**Negative Test Cases:**
13. Invalid queue capacity (negative/zero values)
14. Null parameters in Producer
15. Null parameters in Consumer
16. Interrupted Producer thread handling
17. Interrupted Consumer thread handling
18. Negative items to consume
19. Queue overflow prevention
20. Queue underflow prevention

### Technical Details

**Synchronization Strategy:**
- Method-level synchronization on SharedQueue
- `wait()` releases lock and suspends thread
- `notifyAll()` wakes up all waiting threads
- Proper handling of InterruptedException

**Thread Safety:**
- SharedQueue operations are atomic
- Destination containers use synchronized collections
- No race conditions or deadlocks

**Performance Considerations:**
- Bounded queue prevents memory overflow
- Configurable production/consumption delays
- Efficient wait/notify instead of busy waiting

### Requirements Met
Implements producer-consumer pattern  
Demonstrates thread synchronization  
Uses blocking queues  
Implements wait/notify mechanism  
Includes Unit tests  
Fully documented with comments  
Shows concurrent programming concepts  

### Sample Output

```
=== Producer-Consumer Pattern Demo ===

Starting threads...

Producer-1 started producing...
Consumer-2 started consuming...
Producer-2 started producing...
Consumer-1 started consuming...
Producer-1 - Produced: 1 | Queue size: 1
Consumer-1 - Consumed: 1 | Queue size: 0
Producer-2 - Produced: 2 | Queue size: 1
Consumer-2 - Consumed: 2 | Queue size: 0
Producer-1 - Produced: 3 | Queue size: 1
Producer-2 - Produced: 4 | Queue size: 2
Consumer-1 - Consumed: 3 | Queue size: 1
Producer-1 - Produced: 5 | Queue size: 2
...
Producer-1 - Queue is full. Waiting...
Consumer-2 - Consumed: 9 | Queue size: 4
Producer-1 - Produced: 15 | Queue size: 5
...
=== Execution Complete ===

Final Statistics:
Queue capacity: 5
Total items produced: 20
Consumer-1 received: 12 items - [1, 3, 5, 7, 8, 11, 13, 12, 14, 19, 18, 20]
Consumer-2 received: 8 items - [2, 4, 6, 9, 10, 15, 17, 16]
Total consumed: 20 items

Queue final state:
Items remaining in queue: 0
```

### Project Structure
```
Assignment_1-Producer_Consumer/
│
├── src/
│   └── main/
│       └── java/
│           ├── SharedQueue.java              # Thread-safe bounded queue
│           ├── Producer.java                 # Producer thread
│           ├── Consumer.java                 # Consumer thread
│           └── ProducerConsumerDemo.java     # Main demo application
├── test/
│   └── ProducerConsumerTest.java             # Unit tests (20 tests)
└── README.md                                 # Project documentation
```

### Key Learnings
1. **Thread Synchronization**: Proper use of `synchronized` prevents race conditions
2. **Wait/Notify Pattern**: Efficient inter-thread communication without busy-waiting
3. **Blocking Queues**: Automatic coordination between producers and consumers
4. **Concurrent Design**: Managing multiple threads accessing shared resources safely
5. **Testing Concurrent Code**: Strategies for testing multi-threaded applications

### Author
**Ritesh Malviya**  
Build Challenge - Assignment 1  


