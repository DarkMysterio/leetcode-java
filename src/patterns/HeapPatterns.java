package patterns;

import java.util.*;

/**
 * ============================================================
 * HEAP / PRIORITY QUEUE PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Need to repeatedly access the min or max element
 *  - "Top K" elements problems (K largest, K most frequent, K closest)
 *  - Streaming data where you need running median, etc.
 *
 * Java PriorityQueue:
 *  - Default = MIN-HEAP (smallest at top)
 *  - MAX-HEAP = new PriorityQueue<>(Collections.reverseOrder())
 *  - Custom comparator for int[] or objects
 *
 * Key methods:
 *  offer(x)  — add element           O(log n)
 *  poll()    — remove + return min   O(log n)
 *  peek()    — view min, no remove   O(1)
 *  size()    — count                 O(1)
 *  isEmpty() — check empty           O(1)
 *
 * Time: O(n log k) for Top K patterns
 * Space: O(k) for the heap
 */
public class HeapPatterns {

    public static void main(String[] args) {
        System.out.println("=== Kth Largest Element ===");
        System.out.println(findKthLargestSolved(new int[]{3,2,1,5,6,4}, 2)); // 5
        System.out.println(findKthLargestSolved(new int[]{3,2,3,1,2,4,5,5,6}, 4)); // 4

        System.out.println("\n=== Top K Frequent Elements ===");
        int[] topK = topKFrequentSolved(new int[]{1,1,1,2,2,3}, 2);
        System.out.println(Arrays.toString(topK)); // [1, 2]

        System.out.println("\n=== K Closest Points to Origin ===");
        int[][] closest = kClosestSolved(new int[][]{{1,3},{-2,2}}, 1);
        System.out.println(Arrays.deepToString(closest)); // [[-2,2]]
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Min-Heap of size K (Top K Largest)
    // ─────────────────────────────────────────────
    // Strategy: Keep a min-heap of size k.
    //   - If heap has < k elements → add freely
    //   - If new element > heap top → pop the min, push new element
    //   - Result: heap contains the k LARGEST elements; top = Kth largest
    static void topKLargestTemplate(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // default: min at top

        for (int num : nums) {
            minHeap.offer(num); // add to heap

            if (minHeap.size() > k) {
                minHeap.poll(); // remove smallest → only k largest remain
            }
        }

        // minHeap.peek() = Kth largest
        System.out.println("Kth largest: " + minHeap.peek());
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Max-Heap
    // ─────────────────────────────────────────────
    static void maxHeapTemplate(int[] nums) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        for (int num : nums) maxHeap.offer(num);

        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " "); // prints in descending order
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Heap of int[] (custom comparator)
    // ─────────────────────────────────────────────
    static void heapOfArraysTemplate() {
        // Min-heap sorted by first element of each int[]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        minHeap.offer(new int[]{3, 100});
        minHeap.offer(new int[]{1, 200});
        minHeap.offer(new int[]{2, 300});

        int[] top = minHeap.poll(); // returns [1, 200] — smallest first element
        System.out.println("[" + top[0] + ", " + top[1] + "]");
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: KTH LARGEST ELEMENT IN AN ARRAY
    // ─────────────────────────────────────────────
    // LeetCode 215 — Medium
    // Find the Kth largest element (not Kth distinct).
    //
    // Example: [3,2,1,5,6,4], k=2 → 5
    //          [3,2,3,1,2,4,5,5,6], k=4 → 4
    //
    // Approach (min-heap of size k):
    //  - Maintain a min-heap of exactly k elements
    //  - If size > k: remove the minimum (it's not in top k)
    //  - At the end, top of heap = Kth largest
    //
    // Time: O(n log k)  Space: O(k)

    // TODO VERSION
    static int findKthLargestTODO(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min at top

        for (int num : nums) {
            // TODO: add num to heap
            // TODO: if heap size > k, remove the smallest (poll)
        }

        // TODO: return heap top (peek) — it's the Kth largest
        return 0;
    }

    // SOLVED VERSION
    static int findKthLargestSolved(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min element on top

        for (int num : nums) {
            minHeap.offer(num);              // add current number

            if (minHeap.size() > k) {
                minHeap.poll();              // kick out the smallest — not in top k
            }
        }

        return minHeap.peek();               // top of min-heap = Kth largest
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: TOP K FREQUENT ELEMENTS (Heap version)
    // ─────────────────────────────────────────────
    // LeetCode 347 — Medium
    // Return the k most frequent elements in any order.
    //
    // Example: [1,1,1,2,2,3], k=2 → [1,2]
    //
    // Approach:
    //  1. Count frequencies with HashMap
    //  2. Use a min-heap of size k keyed by frequency
    //     → heap top is the LEAST frequent among top-k
    //     → when heap size > k, remove least frequent
    //  3. Remaining k elements are the most frequent
    //
    // Time: O(n log k)  Space: O(n)

    // TODO VERSION
    static int[] topKFrequentTODO(int[] nums, int k) {
        // Step 1: count frequencies
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);

        // Step 2: min-heap sorted by frequency (least frequent at top)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(freq.get(a), freq.get(b)) // sort by frequency
        );

        for (int key : freq.keySet()) {
            // TODO: add key to heap
            // TODO: if heap size > k, poll (removes least frequent)
        }

        // Step 3: extract k elements from heap into result
        int[] result = new int[k];
        // TODO: fill result array from heap
        return result;
    }

    // SOLVED VERSION
    static int[] topKFrequentSolved(int[] nums, int k) {
        // Step 1: build frequency map
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);

        // Step 2: min-heap ordered by frequency (least frequent at top)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(freq.get(a), freq.get(b))
        );

        for (int key : freq.keySet()) {
            minHeap.offer(key);              // add this number

            if (minHeap.size() > k) {
                minHeap.poll();              // remove least frequent — not in top k
            }
        }

        // Step 3: collect the k elements left in heap
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();      // order doesn't matter per problem
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: K CLOSEST POINTS TO ORIGIN (Heap version)
    // ─────────────────────────────────────────────
    // LeetCode 973 — Medium
    // Return the k closest points to origin (0,0).
    // Distance = sqrt(x²+y²), but we compare x²+y² (skip sqrt).
    //
    // Example: [[1,3],[-2,2]], k=1 → [[-2,2]]
    //
    // Approach (max-heap of size k):
    //  - Max-heap keyed by distance²
    //  - Keep at most k points
    //  - If size > k: remove the farthest → only k closest remain
    //
    // Time: O(n log k)  Space: O(k)

    // TODO VERSION
    static int[][] kClosestTODO(int[][] points, int k) {
        // Max-heap: farthest point at top (so we can remove it when size > k)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(
                b[0]*b[0] + b[1]*b[1],  // dist² of b
                a[0]*a[0] + a[1]*a[1]   // dist² of a
            )
        );

        for (int[] point : points) {
            // TODO: add point to maxHeap
            // TODO: if size > k, poll (removes farthest)
        }

        // TODO: extract remaining k points into result
        return new int[0][];
    }

    // SOLVED VERSION
    static int[][] kClosestSolved(int[][] points, int k) {
        // Max-heap: farthest at top (largest distance² = first to be kicked out)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(
                b[0]*b[0] + b[1]*b[1], // dist² of b
                a[0]*a[0] + a[1]*a[1]  // dist² of a
            )
        );

        for (int[] point : points) {
            maxHeap.offer(point);            // add this point

            if (maxHeap.size() > k) {
                maxHeap.poll();              // remove the farthest — not in k closest
            }
        }

        // Collect the k closest points from heap
        int[][] result = new int[k][];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH HEAPS
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  new PriorityQueue<>(Collections.reverseOrder()) for int[]
     *            → doesn't know how to compare int[]
     * ✅ RIGHT:  new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]))
     *            explicit comparator for custom types
     *
     * ❌ WRONG:  (a, b) -> b[0] - a[0]   → can overflow for large negative values
     * ✅ RIGHT:  (a, b) -> Integer.compare(b[0], a[0])
     *
     * ❌ WRONG:  Using maxHeap for "Kth Largest" → you'd remove correct elements accidentally
     * ✅ RIGHT:  Use minHeap of size k for "Kth Largest"
     *            Use maxHeap of size k for "Kth Smallest"
     *            → min-heap top = Kth Largest; max-heap top = Kth Smallest
     *
     * ❌ WRONG:  heap.peek() on empty heap → NoSuchElementException
     * ✅ RIGHT:  check !heap.isEmpty() before peek/poll
     *
     * ❌ WRONG:  freq.get(a) inside comparator if key was removed → NPE
     * ✅ RIGHT:  build the freq map fully before creating the heap comparator
     */
}
