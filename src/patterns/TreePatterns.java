package patterns;

import java.util.*;

/**
 * ============================================================
 * TREE PATTERNS — Interview Reference
 * ============================================================
 * When to use DFS (recursive):
 *  - Need to explore entire subtrees before making decisions
 *  - Inorder/preorder/postorder traversal
 *  - Path problems, subtree matching
 *
 * When to use BFS (iterative with queue):
 *  - Level-order traversal
 *  - Shortest path in a tree
 *  - Processing nodes level by level
 *
 * CRITICAL: Most tree problems use recursion.
 * Think: "what should this function do for one node?"
 *        Then trust it works for left and right subtrees.
 *
 * TreeNode definition is at the bottom of this file.
 *
 * Time: O(n) for most tree traversals
 * Space: O(h) where h = height (O(log n) balanced, O(n) skewed)
 */
public class TreePatterns {

    // ─────────────────────────────────────────────
    // TREENODE DEFINITION (used in all problems)
    // ─────────────────────────────────────────────
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    // Helper to build a simple tree for testing
    //       3
    //      / \
    //     9  20
    //       /  \
    //      15   7
    static TreeNode buildSampleTree() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        return root;
    }

    public static void main(String[] args) {
        TreeNode root = buildSampleTree();

        System.out.println("=== Max Depth ===");
        System.out.println(maxDepthSolved(root)); // 3

        System.out.println("\n=== Invert Binary Tree ===");
        TreeNode inverted = invertTreeSolved(buildSampleTree());
        // Just check left/right are swapped at root
        System.out.println("root.left.val: " + inverted.left.val);  // 20
        System.out.println("root.right.val: " + inverted.right.val); // 9

        System.out.println("\n=== Same Tree ===");
        System.out.println(sameTreeSolved(buildSampleTree(), buildSampleTree())); // true
        System.out.println(sameTreeSolved(buildSampleTree(), null));              // false

        System.out.println("\n=== Level Order Traversal ===");
        System.out.println(levelOrderSolved(root)); // [[3],[9,20],[15,7]]

        System.out.println("\n=== Diameter ===");
        // Build a line tree: 1-2-3-4-5
        TreeNode diamTree = new TreeNode(1,
            new TreeNode(2,
                new TreeNode(4), new TreeNode(5)),
            new TreeNode(3));
        System.out.println(diameterSolved(diamTree)); // 3
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Recursive DFS
    // ─────────────────────────────────────────────
    static int dfsTemplate(TreeNode node) {
        if (node == null) return 0; // base case: empty tree

        int left = dfsTemplate(node.left);   // recurse left
        int right = dfsTemplate(node.right); // recurse right

        // Process current node using left and right results
        return 1 + Math.max(left, right);    // example: max depth
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Iterative DFS (preorder using stack)
    // ─────────────────────────────────────────────
    static void iterativeDFS(TreeNode root) {
        if (root == null) return;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            System.out.print(node.val + " "); // process node

            // Push right first so left is processed first (LIFO)
            if (node.right != null) stack.push(node.right);
            if (node.left  != null) stack.push(node.left);
        }
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — BFS Level Order
    // ─────────────────────────────────────────────
    static List<List<Integer>> bfsTemplate(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root); // start BFS from root

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // number of nodes at current level
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll(); // dequeue front
                level.add(node.val);

                if (node.left  != null) queue.offer(node.left);  // enqueue children
                if (node.right != null) queue.offer(node.right);
            }

            result.add(level); // done with this level
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: MAXIMUM DEPTH OF BINARY TREE
    // ─────────────────────────────────────────────
    // LeetCode 104 — Easy
    // Return the maximum depth (number of nodes along the longest path from root to leaf).
    //
    // Example: tree [3,9,20,null,null,15,7] → 3
    //
    // Think: depth = 1 + max(depth(left), depth(right))
    //        base case: null node has depth 0

    // TODO VERSION
    static int maxDepthTODO(TreeNode root) {
        // TODO: base case — if root is null, return 0
        // TODO: recurse left and right
        // TODO: return 1 + max of left and right depths
        return 0;
    }

    // SOLVED VERSION
    static int maxDepthSolved(TreeNode root) {
        if (root == null) return 0;                         // base case: empty → depth 0

        int leftDepth  = maxDepthSolved(root.left);         // depth of left subtree
        int rightDepth = maxDepthSolved(root.right);        // depth of right subtree

        return 1 + Math.max(leftDepth, rightDepth);         // +1 for current node
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: INVERT BINARY TREE
    // ─────────────────────────────────────────────
    // LeetCode 226 — Easy
    // Flip the tree: left subtree and right subtree are swapped at every node.
    //
    // Approach:
    //  - Swap left and right children of current node
    //  - Recursively invert both subtrees

    // TODO VERSION
    static TreeNode invertTreeTODO(TreeNode root) {
        if (root == null) return null; // base case

        // TODO: swap root.left and root.right
        // TODO: recursively invert both subtrees
        // TODO: return root

        return root;
    }

    // SOLVED VERSION
    static TreeNode invertTreeSolved(TreeNode root) {
        if (root == null) return null; // nothing to invert

        // Swap left and right children
        TreeNode temp = root.left;
        root.left  = root.right;
        root.right = temp;

        // Recursively invert both subtrees
        invertTreeSolved(root.left);
        invertTreeSolved(root.right);

        return root;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: SAME TREE
    // ─────────────────────────────────────────────
    // LeetCode 100 — Easy
    // Given two trees p and q, return true if they are identical.
    //
    // Base cases:
    //  - Both null → true (same empty tree)
    //  - One null → false (different structure)
    //  - Values differ → false
    // Recursive: both sides must also be same

    // TODO VERSION
    static boolean sameTreeTODO(TreeNode p, TreeNode q) {
        // TODO: if both null → return true
        // TODO: if one null, or values differ → return false
        // TODO: return sameTree(p.left, q.left) && sameTree(p.right, q.right)
        return false;
    }

    // SOLVED VERSION
    static boolean sameTreeSolved(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;  // both empty → same
        if (p == null || q == null) return false; // one empty → different
        if (p.val != q.val) return false;         // values differ → different

        // Both sides must be the same
        return sameTreeSolved(p.left, q.left) && sameTreeSolved(p.right, q.right);
    }

    // ─────────────────────────────────────────────
    // PROBLEM 4: LEVEL ORDER TRAVERSAL
    // ─────────────────────────────────────────────
    // LeetCode 102 — Medium
    // Return node values level by level as List<List<Integer>>.
    //
    // Example: [3,9,20,null,null,15,7] → [[3],[9,20],[15,7]]
    //
    // Approach: BFS with queue; process one entire level per iteration

    // TODO VERSION
    static List<List<Integer>> levelOrderTODO(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            // TODO: get the current level size = queue.size()
            // TODO: loop levelSize times:
            //   poll a node
            //   add its value to the current level list
            //   offer its non-null children to queue
            // TODO: add current level list to result
        }

        return result;
    }

    // SOLVED VERSION
    static List<List<Integer>> levelOrderSolved(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root); // start with root

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // how many nodes are on this level
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();   // process one node
                level.add(node.val);

                if (node.left  != null) queue.offer(node.left);  // enqueue children
                if (node.right != null) queue.offer(node.right); //   for next level
            }

            result.add(level); // finished this level
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 5: DIAMETER OF BINARY TREE
    // ─────────────────────────────────────────────
    // LeetCode 543 — Easy
    // Return the diameter = length of longest path between any two nodes
    // (path length = number of edges).
    //
    // Key insight: the diameter through any node = leftHeight + rightHeight
    //              (doesn't have to pass through root!)
    //
    // Approach: DFS that returns height; update global max diameter as we go
    //
    // ⚠️ Need a class-level or array field to carry the max across recursive calls

    // TODO VERSION
    static int diameterTODO(TreeNode root) {
        int[] maxDiameter = {0}; // use array to pass by reference into lambda/helper

        // TODO: define a helper that returns height of subtree
        //   at each node: diameter through it = leftH + rightH
        //   update maxDiameter[0] if leftH + rightH > maxDiameter[0]

        diameterHelperTODO(root, maxDiameter);
        return maxDiameter[0];
    }

    static int diameterHelperTODO(TreeNode node, int[] maxDiameter) {
        if (node == null) return 0;
        // TODO: leftH = recurse left
        // TODO: rightH = recurse right
        // TODO: maxDiameter[0] = Math.max(maxDiameter[0], leftH + rightH)
        // TODO: return 1 + Math.max(leftH, rightH)  (height of this node)
        return 0;
    }

    // SOLVED VERSION
    static int diameterSolved(TreeNode root) {
        int[] maxDiameter = {0}; // holds the running maximum diameter
        diameterHelper(root, maxDiameter);
        return maxDiameter[0];
    }

    static int diameterHelper(TreeNode node, int[] maxDiameter) {
        if (node == null) return 0; // height of null node = 0

        int leftH  = diameterHelper(node.left, maxDiameter);  // height of left subtree
        int rightH = diameterHelper(node.right, maxDiameter); // height of right subtree

        // Path through current node = leftH edges + rightH edges
        maxDiameter[0] = Math.max(maxDiameter[0], leftH + rightH); // update global max

        return 1 + Math.max(leftH, rightH); // return height of this subtree
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH TREES
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  node.left.val without null check → NullPointerException
     * ✅ RIGHT:  always check node == null at the start of recursive method
     *
     * ❌ WRONG:  using a global field for answer in a public static class during interviews
     * ✅ RIGHT:  use int[] ans = {0} and pass it into the helper (array is pass-by-reference)
     *
     * ❌ WRONG:  queue.add(node.left) without null check → adds null, causes NPE later
     * ✅ RIGHT:  if (node.left != null) queue.offer(node.left);
     *
     * ❌ WRONG:  using Stack for BFS → wrong traversal order
     * ✅ RIGHT:  BFS uses Queue (ArrayDeque with offer/poll)
     *           DFS uses Stack (ArrayDeque with push/pop) or recursion
     *
     * ❌ WRONG:  forgetting the levelSize snapshot before the inner for-loop
     * ✅ RIGHT:  int levelSize = queue.size();  ← capture BEFORE the loop modifies queue
     */
}
