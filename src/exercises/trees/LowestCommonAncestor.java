package exercises.trees;

public class LowestCommonAncestor {

    /**
     * APPROACH 1: Recursive BST Solution
     *
     * Explanation:
     * This approach leverages the properties of a Binary Search Tree (BST).
     * If both target nodes (p and q) have values greater than the current root,
     * the LCA must lie in the right subtree. If both are smaller, the LCA lies
     * in the left subtree. Otherwise, the current root is the split point (LCA).
     *
     * Time Complexity: O(H) where H is the height of the tree. In the worst case
     * (skewed tree), it visits every node down one path, resulting in O(N).
     * For a balanced tree, it is O(log N).
     *
     * Space Complexity: O(H) due to the recursive call stack storage. In the worst
     * case of a skewed tree, this takes O(N) memory.
     */
    public TreeNode lowestCommonAncestorRecursive(TreeNode root, TreeNode p, TreeNode q) {
        // Base case: if root is null, or if we find either p or q, return root
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode result = root;
        // If both p and q are greater than root, search in the right subtree
        if (root.val < p.val && root.val < q.val) {
            result = lowestCommonAncestorRecursive(root.right, p, q);
        }
        // If both p and q are smaller than root, search in the left subtree
        if (root.val > p.val && root.val > q.val) {
            result = lowestCommonAncestorRecursive(root.left, p, q);
        }
        return result;
    }

    /**
     * APPROACH 2: Iterative BST Solution (FAANG-Optimized)
     *
     * Explanation:
     * Instead of using recursion which consumes system stack space, this approach
     * uses a simple while loop to traverse down the BST. We update a pointer
     * (`current`) based on whether both nodes are to the left or right. The loop
     * stops and returns the node the moment the split point is reached.
     *
     * Time Complexity: O(H) where H is the height of the tree (O(N) worst-case
     * for a skewed tree, O(log N) for a balanced tree).
     *
     * Space Complexity: O(1) because it uses constant extra space without
     * building a recursive call stack.
     */
    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode current = root;

        while (current != null) {
            // Both nodes are in the right subtree
            if (p.val > current.val && q.val > current.val) {
                current = current.right;
            }
            // Both nodes are in the left subtree
            else if (p.val < current.val && q.val < current.val) {
                current = current.left;
            }
            // We found the split point (or current is p or q)
            else {
                return current;
            }
        }

        return null; // Fallback if tree is empty or nodes aren't found
    }
}