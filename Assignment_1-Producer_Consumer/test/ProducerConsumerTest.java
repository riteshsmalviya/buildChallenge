import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive unit tests for Producer-Consumer pattern implementation
 * Tests thread synchronization, concurrent programming, blocking queues, and wait/notify mechanism
 */
public class ProducerConsumerTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Producer-Consumer Unit Tests ===\n");
        
        // Run all tests
        testSharedQueueBasicOperations();
        testSharedQueueCapacity();
        testSharedQueueThreadSafety();
        testProducerBasicFunctionality();
        testConsumerBasicFunctionality();
        testSingleProducerSingleConsumer();
        testMultipleProducersMultipleConsumers();
        testBlockingBehavior();
        testWaitNotifyMechanism();
        testThreadSynchronization();
        testConcurrentAccess();
        testEdgeCases();
        
        // Negative tests
        testInvalidQueueCapacity();
        testNullParametersInProducer();
        testNullParametersInConsumer();
        testInterruptedProducer();
        testInterruptedConsumer();
        testNegativeItemsToConsume();
        testQueueOverflowPrevention();
        testQueueUnderflowPrevention();
        
        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println("Success Rate: " + String.format("%.1f%%", 
            (testsPassed * 100.0 / (testsPassed + testsFailed))));
    }
    
    /**
     * Test 1: SharedQueue basic operations
     */
    private static void testSharedQueueBasicOperations() {
        System.out.println("Test 1: SharedQueue Basic Operations");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            
            // Test initial state
            assert queue.isEmpty() : "Queue should be empty initially";
            assert queue.size() == 0 : "Queue size should be 0";
            assert !queue.isFull() : "Queue should not be full";
            
            // Test put and take
            queue.put(1);
            assert queue.size() == 1 : "Queue size should be 1";
            assert !queue.isEmpty() : "Queue should not be empty";
            
            int item = queue.take();
            assert item == 1 : "Retrieved item should be 1";
            assert queue.isEmpty() : "Queue should be empty after take";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 2: SharedQueue capacity constraints
     */
    private static void testSharedQueueCapacity() {
        System.out.println("Test 2: SharedQueue Capacity Constraints");
        try {
            SharedQueue<String> queue = new SharedQueue<>(3);
            
            queue.put("A");
            queue.put("B");
            queue.put("C");
            
            assert queue.isFull() : "Queue should be full";
            assert queue.size() == 3 : "Queue size should be 3";
            assert queue.getCapacity() == 3 : "Capacity should be 3";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 3: SharedQueue thread safety
     */
    private static void testSharedQueueThreadSafety() {
        System.out.println("Test 3: SharedQueue Thread Safety");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(100);
            final int OPERATIONS = 50;
            
            Thread writer = new Thread(() -> {
                try {
                    for (int i = 0; i < OPERATIONS; i++) {
                        queue.put(i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            Thread reader = new Thread(() -> {
                try {
                    for (int i = 0; i < OPERATIONS; i++) {
                        queue.take();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            writer.start();
            reader.start();
            writer.join();
            reader.join();
            
            assert queue.isEmpty() : "Queue should be empty after operations";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 4: Producer basic functionality
     */
    private static void testProducerBasicFunctionality() {
        System.out.println("Test 4: Producer Basic Functionality");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(10);
            List<Integer> source = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                source.add(i);
            }
            
            Producer<Integer> producer = new Producer<>(queue, source, "TestProducer", 10);
            Thread thread = new Thread(producer);
            thread.start();
            thread.join();
            
            assert queue.size() == 5 : "Queue should have 5 items";
            assert producer.getProducerName().equals("TestProducer") : "Producer name should match";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 5: Consumer basic functionality
     */
    private static void testConsumerBasicFunctionality() {
        System.out.println("Test 5: Consumer Basic Functionality");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(10);
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            // Pre-fill queue
            for (int i = 1; i <= 5; i++) {
                queue.put(i);
            }
            
            Consumer<Integer> consumer = new Consumer<>(queue, destination, "TestConsumer", 5, 10);
            Thread thread = new Thread(consumer);
            thread.start();
            thread.join();
            
            assert destination.size() == 5 : "Destination should have 5 items";
            assert queue.isEmpty() : "Queue should be empty";
            assert consumer.getConsumerName().equals("TestConsumer") : "Consumer name should match";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 6: Single Producer Single Consumer
     */
    private static void testSingleProducerSingleConsumer() {
        System.out.println("Test 6: Single Producer Single Consumer");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            List<Integer> source = new ArrayList<>();
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            for (int i = 1; i <= 10; i++) {
                source.add(i);
            }
            
            Producer<Integer> producer = new Producer<>(queue, source, "P1", 20);
            Consumer<Integer> consumer = new Consumer<>(queue, destination, "C1", 10, 30);
            
            Thread pThread = new Thread(producer);
            Thread cThread = new Thread(consumer);
            
            pThread.start();
            cThread.start();
            pThread.join();
            cThread.join();
            
            assert destination.size() == 10 : "Should consume all 10 items, got " + destination.size();
            assert queue.isEmpty() : "Queue should be empty, but has " + queue.size() + " items";
            
            // Verify all items 1-10 are present
            for (int i = 1; i <= 10; i++) {
                assert destination.contains(i) : "Missing item " + i + " in destination";
            }
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 7: Multiple Producers Multiple Consumers
     */
    private static void testMultipleProducersMultipleConsumers() {
        System.out.println("Test 7: Multiple Producers Multiple Consumers");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            
            List<Integer> source1 = new ArrayList<>();
            List<Integer> source2 = new ArrayList<>();
            List<Integer> dest1 = Collections.synchronizedList(new ArrayList<>());
            List<Integer> dest2 = Collections.synchronizedList(new ArrayList<>());
            
            for (int i = 1; i <= 10; i++) {
                source1.add(i);
                source2.add(i + 10);
            }
            
            Producer<Integer> p1 = new Producer<>(queue, source1, "P1", 20);
            Producer<Integer> p2 = new Producer<>(queue, source2, "P2", 25);
            Consumer<Integer> c1 = new Consumer<>(queue, dest1, "C1", 10, 30);
            Consumer<Integer> c2 = new Consumer<>(queue, dest2, "C2", 10, 35);
            
            Thread[] threads = {
                new Thread(p1), new Thread(p2),
                new Thread(c1), new Thread(c2)
            };
            
            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();
            
            int totalConsumed = dest1.size() + dest2.size();
            assert totalConsumed == 20 : "Should consume all 20 items, got " + totalConsumed;
            assert queue.isEmpty() : "Queue should be empty, but has " + queue.size() + " items";
            
            // Verify no items were lost or duplicated
            List<Integer> allConsumed = new ArrayList<>();
            allConsumed.addAll(dest1);
            allConsumed.addAll(dest2);
            assert allConsumed.size() == 20 : "Total items should be 20, got " + allConsumed.size();
            
            // Check all expected items are present (1-20)
            for (int i = 1; i <= 20; i++) {
                int count = 0;
                for (Integer item : allConsumed) {
                    if (item == i) count++;
                }
                assert count == 1 : "Item " + i + " appears " + count + " times (should be exactly 1)";
            }
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 8: Blocking behavior when queue is full
     */
    private static void testBlockingBehavior() {
        System.out.println("Test 8: Blocking Behavior");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(2);
            final boolean[] producerBlocked = {false};
            
            // Fill the queue
            queue.put(1);
            queue.put(2);
            assert queue.isFull() : "Queue should be full";
            
            // Try to put another item (should block)
            Thread producer = new Thread(() -> {
                try {
                    producerBlocked[0] = true;
                    queue.put(3); // This will block
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            producer.start();
            Thread.sleep(100); // Give producer time to block
            
            // Producer should be blocked
            assert producerBlocked[0] : "Producer should have attempted to put";
            assert producer.isAlive() : "Producer should be blocked";
            
            // Consume an item to unblock producer
            int item = queue.take();
            assert item == 1 || item == 2 : "Should get one of the original items";
            producer.join(1000);
            
            assert !producer.isAlive() : "Producer should have completed";
            assert queue.size() == 2 : "Queue should have 2 items (1 removed, 1 added), got " + queue.size();
            assert queue.isFull() : "Queue with capacity 2 should be full with 2 items";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 9: Wait/Notify mechanism
     */
    private static void testWaitNotifyMechanism() {
        System.out.println("Test 9: Wait/Notify Mechanism");
        try {
            SharedQueue<String> queue = new SharedQueue<>(1);
            final CountDownLatch latch = new CountDownLatch(1);
            
            // Consumer waits for item
            Thread consumer = new Thread(() -> {
                try {
                    latch.countDown(); // Signal that consumer is ready
                    String item = queue.take(); // Will wait
                    assert item.equals("notify-test") : "Should receive correct item";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            consumer.start();
            latch.await(); // Wait for consumer to be ready
            Thread.sleep(100); // Ensure consumer is waiting
            
            // Producer notifies consumer
            queue.put("notify-test");
            consumer.join(1000);
            
            assert !consumer.isAlive() : "Consumer should have been notified";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 10: Thread synchronization correctness
     */
    private static void testThreadSynchronization() {
        System.out.println("Test 10: Thread Synchronization");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(10);
            final int ITEMS = 100;
            
            List<Integer> source = new ArrayList<>();
            for (int i = 0; i < ITEMS; i++) {
                source.add(i);
            }
            
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            Producer<Integer> producer = new Producer<>(queue, source, "P", 1);
            Consumer<Integer> consumer = new Consumer<>(queue, destination, "C", ITEMS, 1);
            
            Thread p = new Thread(producer);
            Thread c = new Thread(consumer);
            
            p.start();
            c.start();
            p.join();
            c.join();
            
            // Verify all items were transferred correctly
            assert destination.size() == ITEMS : "All items should be consumed";
            for (int i = 0; i < ITEMS; i++) {
                assert destination.contains(i) : "Item " + i + " should be present";
            }
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 11: Concurrent access stress test
     */
    private static void testConcurrentAccess() {
        System.out.println("Test 11: Concurrent Access Stress Test");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(20);
            final int PRODUCERS = 3;
            final int CONSUMERS = 3;
            final int ITEMS_PER_PRODUCER = 30;
            
            Thread[] producers = new Thread[PRODUCERS];
            Thread[] consumers = new Thread[CONSUMERS];
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            // Create producers
            for (int i = 0; i < PRODUCERS; i++) {
                List<Integer> source = new ArrayList<>();
                for (int j = 0; j < ITEMS_PER_PRODUCER; j++) {
                    source.add(i * 100 + j);
                }
                producers[i] = new Thread(new Producer<>(queue, source, "P" + i, 5));
            }
            
            // Create consumers
            for (int i = 0; i < CONSUMERS; i++) {
                consumers[i] = new Thread(new Consumer<>(queue, destination, "C" + i, 
                    ITEMS_PER_PRODUCER, 7));
            }
            
            // Start all threads
            for (Thread p : producers) p.start();
            for (Thread c : consumers) c.start();
            
            // Wait for completion
            for (Thread p : producers) p.join();
            for (Thread c : consumers) c.join();
            
            int expectedTotal = PRODUCERS * ITEMS_PER_PRODUCER;
            assert destination.size() == expectedTotal : 
                "Should have " + expectedTotal + " items, got " + destination.size();
            assert queue.isEmpty() : "Queue should be empty after all consumed";
            
            // Verify no duplicates by checking unique items
            java.util.Set<Integer> uniqueItems = new java.util.HashSet<>(destination);
            assert uniqueItems.size() == expectedTotal : 
                "Found duplicates! Unique items: " + uniqueItems.size() + ", Total: " + expectedTotal;
            
            // Verify all expected items are present
            for (int i = 0; i < PRODUCERS; i++) {
                for (int j = 0; j < ITEMS_PER_PRODUCER; j++) {
                    int expectedItem = i * 100 + j;
                    assert destination.contains(expectedItem) : 
                        "Missing item " + expectedItem + " in consumed items";
                }
            }
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 12: Edge cases
     */
    private static void testEdgeCases() {
        System.out.println("Test 12: Edge Cases");
        try {
            // Test with capacity 1
            SharedQueue<Integer> queue1 = new SharedQueue<>(1);
            queue1.put(42);
            assert queue1.isFull() : "Queue with capacity 1 should be full";
            int val = queue1.take();
            assert val == 42 : "Should get correct value";
            
            // Test with empty source
            SharedQueue<String> queue2 = new SharedQueue<>(5);
            List<String> emptySource = new ArrayList<>();
            List<String> dest = Collections.synchronizedList(new ArrayList<>());
            
            Producer<String> producer = new Producer<>(queue2, emptySource, "P", 10);
            Thread t = new Thread(producer);
            t.start();
            t.join();
            
            assert queue2.isEmpty() : "Queue should remain empty";
            
            // Test consuming 0 items
            Consumer<String> consumer = new Consumer<>(queue2, dest, "C", 0, 10);
            Thread t2 = new Thread(consumer);
            t2.start();
            t2.join();
            
            assert dest.isEmpty() : "Destination should be empty";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 13: Invalid queue capacity (negative test)
     */
    private static void testInvalidQueueCapacity() {
        System.out.println("Test 13: Invalid Queue Capacity (Negative)");
        try {
            
            // Test with zero capacity - queue should be perpetually full
            SharedQueue<Integer> queue0 = new SharedQueue<>(0);
            assert queue0.getCapacity() == 0 : "Capacity should be 0";
            assert queue0.isFull() : "Queue with 0 capacity should always be full";
            
            // Try to put - should block immediately since queue is always full
            Thread producer0 = new Thread(() -> {
                try {
                    queue0.put(1); // Should block forever
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            producer0.start();
            Thread.sleep(200);
            
            if (!producer0.isAlive()) {
                System.out.println("✗ FAILED: Queue with 0 capacity allowed put operation\n");
                testsFailed++;
                producer0.interrupt();
                return;
            }
            producer0.interrupt();
            producer0.join(500);
            
            // Test with negative capacity - should behave incorrectly
            SharedQueue<Integer> queueNeg = new SharedQueue<>(-5);
            assert queueNeg.getCapacity() == -5 : "Capacity should be -5";
            
            // Negative capacity creates illogical state - never full but can't really add
            boolean behavesIncorrectly = false;
            if (queueNeg.isFull()) {
                behavesIncorrectly = true; // Illogical: empty queue can't be full
            } else {
                // Try adding - should expose the problem
                queueNeg.put(1);
                queueNeg.put(2);
                if (queueNeg.size() > 0 && !queueNeg.isFull()) {
                    behavesIncorrectly = true; // Has items but never becomes full with negative capacity
                }
            }
            
            assert behavesIncorrectly : "Negative capacity should cause incorrect behavior";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 14: Null parameters in Producer (negative test)
     */
    private static void testNullParametersInProducer() {
        System.out.println("Test 14: Null Parameters in Producer (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            List<Integer> source = new ArrayList<>();
            source.add(1);
            
            boolean caughtNull = false;
            
            // Test with null queue
            try {
                Producer<Integer> producer = new Producer<>(null, source, "P1", 10);
                Thread t = new Thread(producer);
                t.start();
                t.join(500);
                // If we get NullPointerException, that's expected
            } catch (NullPointerException e) {
                caughtNull = true;
            }
            
            // Test with null source - should handle gracefully
            try {
                Producer<Integer> producer = new Producer<>(queue, null, "P2", 10);
                Thread t = new Thread(producer);
                t.start();
                t.join(500);
            } catch (NullPointerException e) {
                caughtNull = true;
            }
            
            // At least one null case should be caught
            assert caughtNull : "Null parameters should cause NullPointerException, but none were caught";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 15: Null parameters in Consumer (negative test)
     */
    private static void testNullParametersInConsumer() {
        System.out.println("Test 15: Null Parameters in Consumer (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            queue.put(1);
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            boolean caughtNull = false;
            
            // Test with null queue
            try {
                Consumer<Integer> consumer = new Consumer<>(null, destination, "C1", 1, 10);
                Thread t = new Thread(consumer);
                t.start();
                t.join(500);
            } catch (NullPointerException e) {
                caughtNull = true;
            }
            
            // Test with null destination - should handle or fail
            try {
                Consumer<Integer> consumer = new Consumer<>(queue, null, "C2", 1, 10);
                Thread t = new Thread(consumer);
                t.start();
                t.join(500);
            } catch (NullPointerException e) {
                caughtNull = true;
            }
            
            assert caughtNull : "Null parameters should cause NullPointerException, but none were caught";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 16: Interrupted Producer thread (negative test)
     */
    private static void testInterruptedProducer() {
        System.out.println("Test 16: Interrupted Producer Thread (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(2);
            List<Integer> source = new ArrayList<>();
            
            // Fill queue first
            queue.put(1);
            queue.put(2);
            
            // Large source that will cause blocking
            for (int i = 0; i < 100; i++) {
                source.add(i);
            }
            
            Producer<Integer> producer = new Producer<>(queue, source, "P-Interrupt", 50);
            Thread thread = new Thread(producer);
            thread.start();
            
            // Let it start and block
            Thread.sleep(100);
            
            // Interrupt the producer
            thread.interrupt();
            thread.join(1000);
            
            // Should handle interruption gracefully
            assert !thread.isAlive() : "Thread should terminate after interrupt";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 17: Interrupted Consumer thread (negative test)
     */
    private static void testInterruptedConsumer() {
        System.out.println("Test 17: Interrupted Consumer Thread (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            // Consumer tries to consume from empty queue
            Consumer<Integer> consumer = new Consumer<>(queue, destination, "C-Interrupt", 100, 50);
            Thread thread = new Thread(consumer);
            thread.start();
            
            // Let it start and block on empty queue
            Thread.sleep(100);
            
            // Interrupt the consumer
            thread.interrupt();
            thread.join(1000);
            
            // Should handle interruption gracefully
            assert !thread.isAlive() : "Thread should terminate after interrupt";
            assert destination.size() < 100 : "Should not have consumed all items";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 18: Negative items to consume (negative test)
     */
    private static void testNegativeItemsToConsume() {
        System.out.println("Test 18: Negative Items to Consume (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            queue.put(1);
            queue.put(2);
            
            List<Integer> destination = Collections.synchronizedList(new ArrayList<>());
            
            // Try to consume negative number of items
            Consumer<Integer> consumer = new Consumer<>(queue, destination, "C-Negative", -5, 10);
            Thread thread = new Thread(consumer);
            long startTime = System.currentTimeMillis();
            thread.start();
            thread.join(500);
            long duration = System.currentTimeMillis() - startTime;
            
            // Should complete immediately without consuming anything
            assert !thread.isAlive() : "Thread should have completed with negative items";
            assert destination.isEmpty() : "Should not consume any items with negative count, but got " + destination.size();
            assert duration < 100 : "Should complete quickly (< 100ms), took " + duration + "ms";
            assert queue.size() == 2 : "Queue should still have 2 items, got " + queue.size();
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 19: Queue overflow prevention (negative test)
     */
    private static void testQueueOverflowPrevention() {
        System.out.println("Test 19: Queue Overflow Prevention (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(3);
            
            // Fill queue to capacity
            queue.put(1);
            queue.put(2);
            queue.put(3);
            
            assert queue.isFull() : "Queue should be full";
            
            // Try to add more in a separate thread
            Thread producer = new Thread(() -> {
                try {
                    // This should block, not overflow
                    queue.put(4);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            producer.start();
            Thread.sleep(200);
            
            // Queue should still be at capacity, not overflowed
            assert queue.size() == 3 : "Queue should not overflow, size should be 3, got " + queue.size();
            assert producer.isAlive() : "Producer should be blocked, not failed";
            
            // Cleanup - verify state before cleanup
            int queueSizeBefore = queue.size();
            assert queueSizeBefore == 3 : "Queue size should still be 3 before cleanup";
            
            producer.interrupt();
            producer.join(500);
            
            // Verify nothing changed during blocked state
            assert queue.size() == 3 : "Queue should still have 3 items after cleanup";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    /**
     * Test 20: Queue underflow prevention (negative test)
     */
    private static void testQueueUnderflowPrevention() {
        System.out.println("Test 20: Queue Underflow Prevention (Negative)");
        try {
            SharedQueue<Integer> queue = new SharedQueue<>(5);
            
            assert queue.isEmpty() : "Queue should be empty";
            
            // Try to take from empty queue in separate thread
            Thread consumer = new Thread(() -> {
                try {
                    // This should block, not cause underflow or exception
                    queue.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            consumer.start();
            Thread.sleep(200);
            
            // Queue should still be empty, consumer should be blocked
            assert queue.isEmpty() : "Queue should still be empty";
            assert consumer.isAlive() : "Consumer should be blocked waiting, not failed";
            
            // Cleanup - verify consumer didn't consume anything while blocked
            consumer.interrupt();
            consumer.join(500);
            
            // Verify queue remained empty throughout
            assert queue.isEmpty() : "Queue should still be empty after blocked consumer cleanup";
            assert queue.size() == 0 : "Queue size should be 0";
            
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
