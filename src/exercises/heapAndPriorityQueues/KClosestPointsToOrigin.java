package exercises.heapAndPriorityQueues;

import java.util.Comparator;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {
    public int[][] kClosest(int[][] points, int k) {
        // 1. Construct Min-Heap using `Comparator.comparingInt`
        // Lambda `a -> a[0]*a[0] + a[1]*a[1]` extracts the squared distance integer key.
        // The queue orders elements ascending based on this key (smallest distance at top).
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt((int[] a) -> a[0] * a[0] + a[1] * a[1])
        );

        // 2. Insert all N points into the Min-Heap: O(N log N)
        for (int[] point : points) {
            minHeap.offer(point);
        }

        // 3. Extract the top K closest points: O(K log N)
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }

        return result;
    }

    /**
     * OPTIMIZED ALTERNATIVE (MAX-HEAP OF SIZE K)
     * Time Complexity:  O(N log K)
     * Space Complexity: O(K)
     */
    public int[][] kClosestOptimized(int[][] points, int k) {
        // Max-Heap ordering points by distance DESCENDING (furthest point at root)
        // facem asa ca sa avem mereu doau elemente in heap
        // si cand adaugam al 3lea verificam care em ai mare ca el o sa fie
        // sus in heap si il scoatem, si daca facem tot asa o sa avem in heap mereu
        // cele mai mici 2 elemente in cazul asta distantele cele mai scurte
        // operatia de adaugare si extragere din queue dureaza log(size of queue)
        // in java un prriortyqueue este implementat cu un min-heap
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
                (a, b) -> Integer.compare(
                        (b[0] * b[0] + b[1] * b[1]),
                        (a[0] * a[0] + a[1] * a[1])
                )
        );
        /*
        sau putem face asa
        Comparator.comparingInt((int[] a) -> a[0] * a[0] + a[1] * a[1]).reversed
         */
        for (int[] point : points) {
            maxHeap.offer(point);
            // Maintain max size K: evict the furthest point whenever size > K
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        // The remaining K elements in the heap are guaranteed to be the K closest
        int[][] result = new int[k][2];
        while (k > 0) {
            result[--k] = maxHeap.poll();
        }

        return result;
    }
}
