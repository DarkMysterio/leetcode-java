package exercises.heapAndPriorityQueues;

import java.util.PriorityQueue;

public class KthLargestElementinanArray {
    public int findKthLargest(int[] nums, int k) {
        if(k > nums.length || nums.length == 0){
            return 0;
        }
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for(int num : nums){
            priorityQueue.offer(num);
            if(priorityQueue.size() > k){
                priorityQueue.poll();
            }
        }
        return  priorityQueue.peek();

    }
}
