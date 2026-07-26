package exercises.trees;

public class ValidBST {

    /**
     * Validates whether a given binary tree is a valid Binary Search Tree (BST).
     * Uses a Top-Down Depth-First Search (DFS) range-validation approach.
     *
     * @param root The root node of the binary tree
     * @return true if the tree is a valid BST, false otherwise
     */
    public boolean isValidBST(TreeNode root) {
        // Kick off top-down traversal passing initial lower and upper interval bounds
        return traverseWithInterval(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Recursively checks if all nodes in the subtree fall strictly within (lower, upper).
     *
     * @param node  The current node being validated
     * @param lower The strict lower numerical limit allowed for node.val
     * @param upper The strict upper numerical limit allowed for node.val
     * @return true if the current node and all its descendants satisfy the BST properties
     */
    public boolean traverseWithInterval(TreeNode node, Integer lower, Integer upper) {
        // Base Case: An empty node or reached leaf child is inherently a valid BST
        if (node == null) {
            return true;
        }

        // Constraint Check: Ensure current node's value strictly lies inside (lower, upper)
        // If node.val <= lower or node.val >= upper, the BST ordering property is violated
        if (node.val <= lower || node.val >= upper) {
            return false;
        }

        // Recursive Step (Top-Down Constraint Propagation):
        // 1. Left Child: Must be strictly smaller than node.val -> node.val becomes the new upper bound.
        // 2. Right Child: Must be strictly larger than node.val -> node.val becomes the new lower bound.
        // The '&&' operator short-circuits: if the left subtree fails, execution stops immediately.
        return traverseWithInterval(node.left, lower, node.val)
                && traverseWithInterval(node.right, node.val, upper);
    }
}