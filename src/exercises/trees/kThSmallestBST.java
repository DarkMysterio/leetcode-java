package exercises.trees;

public class kThSmallestBST {
    // Instance variables to track state across recursive stack frames
    int element = -1;
    int cnt = 0;

    /**
     * Finds the k-th smallest element in a Binary Search Tree (1-indexed).
     *
     * Time Complexity: O(H + k) where H is tree height and k is the target index.
     *                  Prunes remaining execution immediately once element is found.
     * Space Complexity: O(H) for the call stack, where H = log N for balanced trees
     *                  and H = N for skewed trees.
     */
    public int kthSmallest(TreeNode root, int k) {
        // Reset state on every main method entry to prevent side-effect bugs across multiple runs
        element = -1;
        cnt = 0;

        inOrder(root, k);
        return element;
    }

    public void inOrder(TreeNode node, int k) {
        // Base Case: Stop if node is null OR if we already found our element (early break/pruning)
        if (node == null || element != -1) {
            return;
        }

        // 1. Visit Left Subtree (Smaller elements)
        inOrder(node.left, k);

        // Check again after coming back from left subtree to prevent unnecessary processing
        if (element != -1) {
            return;
        }

        // 2. Process Current Node
        cnt += 1;
        if (k == cnt) {
            element = node.val;
            return; // Found target! Trigger immediate unwind
        }

        // 3. Visit Right Subtree (Larger elements) - only executes if element is not yet found
        inOrder(node.right, k);
    }
}