package exercises.trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * APPROACH: Breadth-First Search (BFS) / Level-Order Traversal - Rightmost Selection
 *
 * Explanation:
 * Performs a standard level-order traversal using a FIFO queue.
 * For each level in the tree, we iterate through all nodes from left to right.
 * The last element processed at each level represents the rightmost visible node
 * from the right-side view.
 *
 * TIME COMPLEXITY: O(N)
 * - Every node in the binary tree is enqueued and dequeued exactly once,
 *   where N is the total number of nodes.
 *
 * SPACE COMPLEXITY: O(W) -> worst-case O(N)
 * - The queue holds at most the maximum width (W) of the binary tree at any time.
 * - In a full binary tree, the maximum width is N/2 (the bottom leaf level).
 */
public class BinaryTreeRightSideView {

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // Base case: an empty tree has no visible right-side nodes
        if (root == null) {
            return result;
        }

        // Using ArrayDeque for efficient enqueue/dequeue operations
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int currentQueueSize = queue.size(); // Freeze current level's node count

            for (int i = 0; i < currentQueueSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);

                // Standard BFS child expansion (Left then Right)
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            // The last element added to 'level' is the rightmost visible node for this depth
            result.add(level.get(level.size() - 1));

            /*
             * 💡 PRO-TIP FOR MEMORY OPTIMIZATION:
             * Instead of instantiating `List<Integer> level` every loop and calling `get(level.size() - 1)`,
             * you can completely eliminate the `level` list and write:
             *
             * if (i == currentQueueSize - 1) {
             *     result.add(node.val);
             * }
             *
             * This avoids creating unnecessary objects in memory on every level iteration!
             */
        }

        return result;
    }
}