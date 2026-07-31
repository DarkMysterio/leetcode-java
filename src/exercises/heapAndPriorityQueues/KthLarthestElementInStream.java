package exercises.heapAndPriorityQueues;

import java.util.PriorityQueue;

/**
 * ============================================================================
 * PROBLEM: Kth Largest Element in a Stream (LeetCode 703)
 * PATTERN: Capped Min-Heap
 * ============================================================================
 *
 * CORE INTUITION:
 * - We maintain a Min-Heap that holds ONLY the K largest elements seen so far.
 * - In a Min-Heap of size K, the element at the top (peek) is the MINIMUM
 *   of the K largest elements, which is precisely the K-th largest element!
 *
 * TIME COMPLEXITY:
 * - Constructor: O(N log K) where N = nums.length
 * - add(val):    O(log K) per stream element
 *
 * SPACE COMPLEXITY:
 * - O(K) memory space stored in the PriorityQueue.
 * ============================================================================
 */
public class KthLarthestElementInStream {

    // Min-Heap to maintain top K largest elements
    private final PriorityQueue<Integer> priorityQueue;
    private final int k;

    /**
     * Initializes the stream processor with rank k and initial numbers array.
     */
    public KthLarthestElementInStream(int k, int[] nums) {
        this.k = k;
        // Default PriorityQueue in Java is a Min-Heap
        this.priorityQueue = new PriorityQueue<>(k);

        // Process initial numbers while keeping heap size <= k
        for (int num : nums) {
            add(num);
        }
    }

    /**
     * Adds a new value to the stream and returns the current K-th largest element.
     *
     * @param val New incoming number from the stream.
     * @return The K-th largest element overall.
     */
    public int add(int val) {
        // 1. Offer new value into the heap: O(log K)
        priorityQueue.offer(val);

        // 2. If heap size exceeds K, evict the smallest element: O(log K)
        if (priorityQueue.size() > k) {
            priorityQueue.poll();
        }

        // 3. The root of our Min-Heap is guaranteed to be the K-th largest element: O(1)
        return priorityQueue.peek();
    }
}