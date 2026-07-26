package exercises.trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * APPROACH: Breadth-First Search (BFS) / Level-Order Traversal
 *
 * Explanation:
 * Uses a Queue (FIFO structure) to process nodes level by level.
 * At the start of each level, we snapshot the number of elements in the queue (`size`).
 * We then dequeue exactly `size` elements, add their values to the current level list,
 * and enqueue their left and right children for the next level.
 *
 * TIME COMPLEXITY: O(N)
 * - Every node is added and removed from the Queue exactly once, where N is
 *   the total number of nodes in the binary tree.
 *
 * SPACE COMPLEXITY: O(W) -> worst-case O(N)
 * - The auxiliary queue holds at most the maximum width (W) of the binary tree.
 * - In a perfect binary tree, the leaf level contains N/2 nodes, making peak memory O(N).
 * - Excellent choice using ArrayDeque instead of LinkedList, as ArrayDeque
 *   has better memory locality and lower overhead in Java.
 */
public class BinaryTreeLevelOrderTraversal {

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();

            /*
             * ⚠️ THE QUEUE.SIZE() TRAP:
             * Storing `queue.size()` in a local variable before starting the loop is CRITICAL.
             *
             * Why it fails if written directly as `for (int i = 0; i < queue.size(); i++)`:
             * As you process nodes inside the loop, you `poll()` existing nodes out and `offer()`
             * new child nodes in. If `queue.size()` is dynamically re-evaluated in the loop condition,
             * its value keeps changing! This causes the loop to process children from the
             * NEXT level prematurely, completely destroying level boundary separation.
             */
            int size = queue.size(); // Freeze snapshot of current level count

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            result.add(level);
        }

        return result;
    }
}