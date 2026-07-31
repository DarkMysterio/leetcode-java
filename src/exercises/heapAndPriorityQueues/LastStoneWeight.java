package exercises.heapAndPriorityQueues;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * ============================================================================
 * PROBLEM: Last Stone Weight (LeetCode 1046)
 * PATTERN: Max-Heap (Priority Queue) Simulation
 * ============================================================================
 *
 * TIME COMPLEXITY:  O(N log N)
 * SPACE COMPLEXITY: O(N)
 * ============================================================================
 */
public class LastStoneWeight {

    public int lastStoneWeight(int[] stones) {
        // 1. Initialize a Max-Heap to keep the heaviest stones at the top.
        // In Java, default PriorityQueue is a Min-Heap, so we use Comparator.reverseOrder().
        PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.reverseOrder());

        // 2. Populate the Max-Heap: O(N log N)
        for (int stone : stones) {
            heap.offer(stone);
        }

        // 3. Simulate smashing until at most 1 stone remains: O(N log N) total
        while (heap.size() >= 2) {
            // Retrieve and remove the two heaviest stones: O(log N) each
            int firstStone = heap.poll();  // Heaviest
            int secondStone = heap.poll(); // Second heaviest

            // If they are not identical in weight, smash them and offer the remainder back
            if (firstStone != secondStone) {
                // Since firstStone >= secondStone in a Max-Heap, firstStone - secondStone is always >= 0
                heap.offer(firstStone - secondStone);
            }
            // If firstStone == secondStone, both are destroyed, so no re-insertion needed.
        }

        // 4. Edge case handling: If all stones destroyed each other, return 0;
        // otherwise return the last remaining stone.
        return heap.isEmpty() ? 0 : heap.peek();
    }
}