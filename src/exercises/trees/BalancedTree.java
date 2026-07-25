package exercises.trees;

public class BalancedTree {

    private static boolean balance = true;

    // Minimal TreeNode definition for quick testing
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
    }

    /**
     * Time Complexity: O(N) - Every node is visited once (when using pre-computed heights).
     * Space Complexity: O(H) - Recursion stack depth equal to tree height H.
     */
    public static boolean isBalanced(TreeNode root) {
        balance = true;
        calculateBalance(root);
        return balance;
    }

    public static int calculateBalance(TreeNode node){
        if(node == null){
            return 0;
        }

        // 1. Pre-compute subtree heights
        int leftHeight = calculateBalance(node.left);
        int rightHeight = calculateBalance(node.right);

        // 2. Check balance condition for current node
        if(Math.abs(leftHeight - rightHeight) > 1){
            balance = false;
            return 0;
        }

        // PROBLEM: Calling calculateBalance() again re-traverses subtrees, turning O(N) into O(N^2).
        // BEFORE (Slow - O(N^2)):
        // return 1 + Math.max(calculateBalance(node.left), calculateBalance(node.right));

        // FIX: Reuse the pre-computed leftHeight and rightHeight variables in O(1) time.
        // AFTER (Fast - O(N)):
        return 1 + Math.max(leftHeight, rightHeight);
    }

    public static void main(String[] args) {
        // Test Case 1: Balanced Tree
        //      3
        //     / \
        //    9  20
        //      /  \
        //     15   7
        TreeNode balancedRoot = new TreeNode(3);
        balancedRoot.left = new TreeNode(9);
        balancedRoot.right = new TreeNode(20);
        balancedRoot.right.left = new TreeNode(15);
        balancedRoot.right.right = new TreeNode(7);

        System.out.println("Tree 1 is balanced: " + isBalanced(balancedRoot)); // Expected: true

        // Test Case 2: Unbalanced Tree
        //        1
        //       /
        //      2
        //     /
        //    3
        TreeNode unbalancedRoot = new TreeNode(1);
        unbalancedRoot.left = new TreeNode(2);
        unbalancedRoot.left.left = new TreeNode(3);

        System.out.println("Tree 2 is balanced: " + isBalanced(unbalancedRoot)); // Expected: false
    }
}