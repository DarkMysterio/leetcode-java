package exercises.heapAndPriorityQueues;

import java.util.Arrays;

public class Heap {

    //the left and right child of the current node will be at :
    int heap[];
    int pos;

    Heap(){
        heap = new int[10];
        pos=-1;
    }
    void insert(int element){
        if(heap.length - 1 == pos){
            resize();
        }
        heap[++pos] = element;
        fixUpward(pos);
    }

    int getRoot(){
        if(pos == -1){
            return -1;
        }
        int root = heap[0];
        fixDownward();

        return root;
    }

    void heapSort(){
        
    }

    void swap(int p1,int p2){
        int aux = heap[p1];
        heap[p1] = heap[p2];
        heap[p2] = aux;
    }

    void fixDownward(){
        if (pos <= 0) { // Empty or single-element heap
            if (pos == 0) {
                heap[pos--] = 0;
            }
            return;
        }

        // Move last element to root
        heap[0] = heap[pos];
        heap[pos--] = 0;

        int currentIndex = 0;

        while (true) {
            int leftIndex = currentIndex * 2 + 1;
            int rightIndex = currentIndex * 2 + 2;

            // If no left child exists, we reached a leaf
            if (leftIndex > pos) break;

            // Find the larger child
            int largerChildIndex = leftIndex;
            if (rightIndex <= pos && heap[rightIndex] > heap[leftIndex]) {
                largerChildIndex = rightIndex;
            }

            // Swap if child is larger than parent
            if (heap[largerChildIndex] > heap[currentIndex]) {
                swap(currentIndex, largerChildIndex);
                currentIndex = largerChildIndex; // Move down the tree
            } else {
                break; // Heap property satisfied
            }
        }
    }

    void fixUpward(int currentPos){
        while (currentPos > 0) {
            int parentIndex = (currentPos - 1) / 2;
            if (heap[currentPos] > heap[parentIndex]) {
                swap(currentPos, parentIndex);
                currentPos = parentIndex; // Move up the tree
            } else {
                break; // Heap property satisfied
            }
        }
    }

    void resize(){
        heap = Arrays.copyOf(heap,2*heap.length);
    }

}
