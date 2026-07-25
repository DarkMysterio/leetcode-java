package exercises.trees;

public class SameTree {

    /**
     * Approach: Simultaneous Pre-Order Depth-First Search (DFS)
     * Time Complexity: O(N) - Where N is the minimum number of nodes between both trees.
     *                  We visit each pair of corresponding nodes at most once.
     * Space Complexity: O(H) - Where H is the height of the tree, corresponding to the max
     *                   depth of the recursion call stack (O(N) in worst-case skewed trees).
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        // Base Case 1: Both nodes are null -> Structurally identical at this leaf
        if (p == null && q == null) {
            return true;
        }

        // Base Case 2: One node is null while the other is not -> Structural mismatch
        if (p == null || q == null) {
            return false;
        }

        // Base Case 3: Values differ -> Value mismatch
        if (p.val != q.val) {
            return false;
        }

        // Recursion: Check if both left subtrees AND right subtrees are identical
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}