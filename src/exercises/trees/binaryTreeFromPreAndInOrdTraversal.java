package exercises.trees;

/**
 * ============================================================================
 * COMPLEXITY ANALYSIS (Naive Boundary-Pointer Approach)
 * ============================================================================
 * TIME COMPLEXITY: O(N^2) worst-case / average-case
 *   - We process N total nodes (N recursive calls).
 *   - For each node, we perform a linear search over the inorder array range
 *     [inStart, inEnd] to find inIndex.
 *   - In the worst-case (a completely skewed tree), the search loop runs
 *     N + (N-1) + (N-2) + ... + 1 = O(N^2) times.
 *   - Optimization Note: Replacing the linear search loop with a pre-populated
 *     HashMap reduces the lookup time to O(1), dropping total time to O(N).
 *
 * SPACE COMPLEXITY: O(H) call stack space (where H is tree height)
 *   - Best / Average Case (Balanced Tree): O(log N) stack depth.
 *   - Worst Case (Skewed Tree): O(N) stack depth.
 *   - Auxiliary Memory: O(1) extra memory because boundary pointers are
 *     passed instead of allocating new sub-arrays (unlike array slicing).
 * ============================================================================
 */
public class binaryTreeFromPreAndInOrdTraversal {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // Entry point: pass initial boundaries covering the full array ranges
        // Time: O(1) | Space: O(1)
        return tree(preorder, inorder, 0, 0, inorder.length - 1);
    }

    public TreeNode tree(int[] preorder, int[] inorder, int preStart, int inStart, int inEnd) {
        // Base Case: If the inorder bounds cross, there are no nodes left in this branch
        // Time: O(1)
        if (inStart > inEnd) {
            return null;
        }

        // 1. Root is always at preStart in preorder array
        // Time: O(1) | Space: O(1) heap allocation for node
        TreeNode root = new TreeNode(preorder[preStart]);

        // 2. Naive Linear Search: Find root's position in inorder array
        // TIME BOTTLENECK: O(K) per call, where K = (inEnd - inStart + 1) is current subtree size.
        // Executing an O(K) loop across all N recursive frames yields O(N^2) overall time.
        int inIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == root.val) {
                inIndex = i;
                break;
            }
        }

        // 3. Calculate left subtree size
        // Time: O(1)
        int leftSubtreeSize = inIndex - inStart;

        // 4. Recursive Step:
        // Left Child:
        //   - Next preorder root is immediately next: preStart + 1
        //   - Inorder window ranges from inStart to (inIndex - 1)
        // Space: Creates a new stack frame on call stack
        root.left = tree(preorder, inorder, preStart + 1, inStart, inIndex - 1);

        // Right Child:
        //   - Next preorder root skips past all left subtree nodes: preStart + leftSubtreeSize + 1
        //   - Inorder window ranges from (inIndex + 1) to inEnd
        // Space: Creates a new stack frame on call stack
        root.right = tree(preorder, inorder, preStart + leftSubtreeSize + 1, inIndex + 1, inEnd);

        return root;
    }
}