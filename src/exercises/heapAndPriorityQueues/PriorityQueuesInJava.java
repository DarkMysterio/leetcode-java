package exercises.heapAndPriorityQueues;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * ============================================================================
 * FAANG INTERVIEW MASTERCLASS: PriorityQueue in Java
 * ============================================================================
 *
 * 1. UNDERLYING DATA STRUCTURE:
 *    - Implemented as a complete binary tree stored inside a dynamic array (`Object[] queue`).
 *    - Default behavior: MIN-HEAP (the smallest/highest priority element is at the root).
 *    - Tree Array Indexing:
 *      - Parent Index:       (i - 1) / 2
 *      - Left Child Index:   2 * i + 1
 *      - Right Child Index:  2 * i + 2
 *
 * 2. TIME & SPACE COMPLEXITIES:
 *    +-----------------------+-------------------+-----------------------------------+
 *    | Operation             | Time Complexity   | Notes                             |
 *    +-----------------------+-------------------+-----------------------------------+
 *    | offer(e) / add(e)     | O(log N)          | Shift up (siftUp)                 |
 *    | poll() / remove()     | O(log N)          | Shift down (siftDown)             |
 *    | peek() / element()    | O(1)              | Direct lookup at array index 0    |
 *    | contains(Object o)    | O(N)              | Linear search across array        |
 *    | remove(Object o)      | O(N)              | O(N) search + O(log N) heapify    |
 *    | Heapify (Collection)  | O(N)              | Floyd's algorithm build-heap      |
 *    | Space Complexity      | O(N)              | Dynamic array storage             |
 *    +-----------------------+-------------------+-----------------------------------+
 *
 * 3. TOP 5 FAANG INTERVIEW GOTCHAS & TRAPS:
 *    - TRAP 1: NO NULLS ALLOWED. Inserting `null` throws `NullPointerException`.
 *    - TRAP 2: ITERATOR DOES NOT PRESERVE PRIORITY ORDER.
 *      - Iterating via `for (E item : pq)` or `pq.iterator()` iterates over the raw array!
 *      - To process items in priority order, you MUST consume them via `while (!pq.isEmpty()) pq.poll()`.
 *    - TRAP 3: MUTATING OBJECTS INSIDE PQ CORRUPTS HEAP STATE.
 *      - If you modify an object's field that affects its `Comparable`/`Comparator` order while it
 *        resides inside the PQ, the PQ will NOT auto-reheapify!
 *      - Fix: Remove object, mutate field, re-add; or use Lazy Deletion / Index Min-Heap.
 *    - TRAP 4: COMPARATOR OVERFLOW TRAP.
 *      - DO NOT use `(a, b) -> a - b` because `Integer.MIN_VALUE - 1` causes integer underflow.
 *      - ALWAYS use `Integer.compare(a, b)` or `Double.compare(a, b)`.
 *    - TRAP 5: CONTAINS() AND REMOVE(OBJECT) ARE O(N).
 *      - Frequent lookups or arbitrary removals on standard PQ destroy O(log N) efficiency.
 *      - Fix: Pair the PQ with a `HashSet` or `HashMap` for O(1) existence checks.
 *
 * ============================================================================
 */
public class PriorityQueuesInJava {

    public static void main(String[] args) {
        System.out.println("=== FAANG PRIORITY QUEUE DEMONSTRATIONS ===\n");

        demo1_MinAndMaxHeapBasics();
        demo2_ComparatorSafetyAndCustomObjects();
        demo3_TheIteratorTrapVsPoll();
        demo4_ObjectMutationTrapAndWorkaround();
        demo5_FAANGPattern_TopKDistinctElements();
        demo6_FAANGPattern_TwoHeapsStreamMedian();
    }

    /**
     * DEMO 1: Min-Heap vs Max-Heap Basics
     */
    private static void demo1_MinAndMaxHeapBasics() {
        System.out.println("--- 1. Min-Heap vs Max-Heap Basics ---");

        // Default: Min-Heap
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(15);
        minHeap.offer(5);
        minHeap.offer(20);
        minHeap.offer(1);

        System.out.print("Min-Heap Poll Order: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " "); // Output: 1 5 15 20
        }
        System.out.println();

        // Max-Heap using Collections.reverseOrder()
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.offer(15);
        maxHeap.offer(5);
        maxHeap.offer(20);
        maxHeap.offer(1);

        System.out.print("Max-Heap Poll Order: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " "); // Output: 20 15 5 1
        }
        System.out.println("\n");
    }

    /**
     * DEMO 2: Comparator Safety & Multi-Level Sorting
     */
    static class Task {
        String name;
        int priority; // Higher number = higher priority
        int duration; // Lower duration = tie breaker

        Task(String name, int priority, int duration) {
            this.name = name;
            this.priority = priority;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return String.format("%s(P:%d, D:%d)", name, priority, duration);
        }
    }

    private static void demo2_ComparatorSafetyAndCustomObjects() {
        System.out.println("--- 2. Custom Object Comparators (Safe Construction) ---");

        // Multi-level sorting comparator:
        // Primary key: priority DESCENDING
        // Secondary key: duration ASCENDING (tie-breaker)
        Comparator<Task> taskComparator = Comparator
                .comparingInt((Task t) -> t.priority).reversed()
                .thenComparingInt(t -> t.duration);

        PriorityQueue<Task> taskQueue = new PriorityQueue<>(taskComparator);
        taskQueue.offer(new Task("BugFix", 2, 30));
        taskQueue.offer(new Task("Refactor", 5, 120));
        taskQueue.offer(new Task("Feature", 5, 45)); // Same priority as Refactor, lower duration

        System.out.println("Tasks executed in priority order:");
        while (!taskQueue.isEmpty()) {
            System.out.println("  -> " + taskQueue.poll());
        }
        System.out.println();
    }

    /**
     * DEMO 3: The Iterator Trap (Array Traversal vs Priority Retrieval)
     */
    private static void demo3_TheIteratorTrapVsPoll() {
        System.out.println("--- 3. The Iterator Trap ---");

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.addAll(Arrays.asList(10, 2, 15, 1, 7, 20));

        System.out.print("TRAP: Iterating via for-each (Internal Array Order): ");
        for (int val : pq) {
            System.out.print(val + " "); // NOT GUARANTEED TO BE SORTED!
        }
        System.out.println();

        System.out.print("CORRECT: Consuming via poll() (Strict Heap Order):   ");
        PriorityQueue<Integer> copy = new PriorityQueue<>(pq);
        while (!copy.isEmpty()) {
            System.out.print(copy.poll() + " "); // Guaranteed sorted: 1 2 7 10 15 20
        }
        System.out.println("\n");
    }

    /**
     * DEMO 4: Object Mutation Trap & Safe Workaround
     */
    private static void demo4_ObjectMutationTrapAndWorkaround() {
        System.out.println("--- 4. Object Mutation Trap ---");

        class Player {
            String name;
            int score;
            Player(String name, int score) { this.name = name; this.score = score; }
        }

        PriorityQueue<Player> leaderBoard = new PriorityQueue<>(
                Comparator.comparingInt((Player p) -> p.score).reversed()
        );

        Player p1 = new Player("Alice", 100);
        Player p2 = new Player("Bob", 80);
        leaderBoard.offer(p1);
        leaderBoard.offer(p2);

        // MUTATION BUG: Modifying Bob's score while he is inside the PQ
        p2.score = 500; // Bob should now be #1, but PQ does not know!

        System.out.println("Top player according to corrupted PQ: " + leaderBoard.peek().name +
                " (Score: " + leaderBoard.peek().score + ")");
        System.out.println("  Notice: Alice is still reported first even though Bob has 500 points!");

        // WORKAROUND: Explicit re-insertion
        leaderBoard.remove(p2); // O(N) search and removal
        leaderBoard.offer(p2);  // O(log N) insert with new score

        System.out.println("Top player after remove-and-reinsert fix: " + leaderBoard.peek().name +
                " (Score: " + leaderBoard.peek().score + ")");
        System.out.println();
    }

    /**
     * DEMO 5: FAANG Pattern 1 — Top K Frequent / K-th Largest Elements
     * Time: O(N log K) using a Min-Heap capped at size K!
     */
    private static void demo5_FAANGPattern_TopKDistinctElements() {
        System.out.println("--- 5. FAANG Pattern: Top K Largest Elements ---");

        int[] nums = {3, 2, 1, 5, 6, 4, 10, 8, 7, 9};
        int k = 3;

        // Space-Optimized Min-Heap capped at size K
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll(); // Evict smallest element, leaving K largest elements
            }
        }

        System.out.println("Top " + k + " largest elements in array " + Arrays.toString(nums) + ":");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println("\n");
    }

    /**
     * DEMO 6: FAANG Pattern 2 — Dynamic Median Tracking (Two-Heaps Pattern)
     * Time: O(log N) insertion, O(1) median lookup.
     */
    static class MedianFinder {
        private final PriorityQueue<Integer> maxHeapForLowerHalf; // Holds smaller half of numbers
        private final PriorityQueue<Integer> minHeapForUpperHalf; // Holds larger half of numbers

        public MedianFinder() {
            maxHeapForLowerHalf = new PriorityQueue<>(Collections.reverseOrder());
            minHeapForUpperHalf = new PriorityQueue<>();
        }

        public void addNum(int num) {
            // 1. Always insert into maxHeap first
            maxHeapForLowerHalf.offer(num);

            // 2. Balance property: every element in maxHeap must be <= minHeap root
            minHeapForUpperHalf.offer(maxHeapForLowerHalf.poll());

            // 3. Size balance property: maxHeap can have at most 1 more element than minHeap
            if (maxHeapForLowerHalf.size() < minHeapForUpperHalf.size()) {
                maxHeapForLowerHalf.offer(minHeapForUpperHalf.poll());
            }
        }

        public double findMedian() {
            if (maxHeapForLowerHalf.size() > minHeapForUpperHalf.size()) {
                return maxHeapForLowerHalf.peek();
            } else {
                return (maxHeapForLowerHalf.peek() + minHeapForUpperHalf.peek()) / 2.0;
            }
        }
    }

    private static void demo6_FAANGPattern_TwoHeapsStreamMedian() {
        System.out.println("--- 6. FAANG Pattern: Continuous Stream Median (Two-Heaps) ---");

        MedianFinder medianFinder = new MedianFinder();
        int[] stream = {5, 15, 1, 3};

        for (int val : stream) {
            medianFinder.addNum(val);
            System.out.printf("  Added %2d -> Current Median: %.1f%n", val, medianFinder.findMedian());
        }
        System.out.println();
    }
}