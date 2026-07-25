package exercises.trees;

public class DiameterOfBinaryTree {

    /**
     * Current Approach: Top-Down Recursion
     * Time Complexity: O(N^2) - Height is recalculated repeatedly for child subtrees.
     * Space Complexity: O(H) - Call stack depth proportional to tree height H.
     */
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Longest path passing directly through the current root (measured in edges)
        int currentMax = computePath(root.left) + computePath(root.right);

        // Return the max path found at current root, OR in the left subtree, OR in the right subtree
        return Math.max(currentMax, Math.max(diameterOfBinaryTree(root.left), diameterOfBinaryTree(root.right)));
    }

    // Helper: Calculates the max depth/height of a subtree (returns 0 for null)
    public int computePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(computePath(root.right), computePath(root.left));
    }


    /* =========================================================================
     * OPTIMIZED APPROACH: Single-Pass Bottom-Up (Post-Order Traversal)
     * Time Complexity: O(N) - Every node is visited exactly once.
     * Space Complexity: O(H) - Call stack depth proportional to tree height H.
     * ========================================================================= */

    private int maxDiameter = 0;

    public int diameterOfBinaryTreeOptimized(TreeNode root) {
        maxDiameter = 0;
        computePathAndDiameter(root);

        return maxDiameter;
    }

    private int computePathAndDiameter(TreeNode root) {
        if(root == null){
            return 0;
        }
        int leftHeight = computePathAndDiameter(root.left);
        int rightHeight = computePathAndDiameter(root.right);

        maxDiameter = Math.max(maxDiameter, leftHeight + rightHeight);

        return 1 + Math.max(leftHeight,rightHeight);

    }
}