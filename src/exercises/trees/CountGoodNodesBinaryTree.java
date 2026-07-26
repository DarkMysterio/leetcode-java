package exercises.trees;

public class CountGoodNodesBinaryTree {
    public int goodNodes(TreeNode root) {
        if (root == null)
            return 0;
        return dfsGoodNodes(root, root.val); // 1. Start with root value as maxSoFar
    }

    public int dfsGoodNodes(TreeNode node, int maxSoFar) {
        if (node == null) {
            return 0; // 2. Base case: Null nodes contribute 0 good nodes
        }

        int goodNodes = 0;
        if (node.val >= maxSoFar) {
            goodNodes += 1;     // 3. Current node is good!
            maxSoFar = node.val; // 4. Update max for children
        }

        // 5. Aggregate result: current node + left subtree + right subtree
        return goodNodes + dfsGoodNodes(node.left, maxSoFar) + dfsGoodNodes(node.right, maxSoFar);
    }
}
