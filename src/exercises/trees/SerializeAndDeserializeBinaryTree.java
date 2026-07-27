package exercises.trees;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * ============================================================================
 * ALGORITHM OVERVIEW & LESSONS LEARNED
 * ============================================================================
 * 1. STRATEGY: Pre-Order DFS Traversal (Root -> Left -> Right).
 *    - Serialization turns the tree structure into a linear comma-separated string.
 *    - Deserialization reconstructs the tree by reading tokens in the exact same
 *      Pre-Order sequence using a Queue.
 *
 * 2. KEY UNCERTAINTIES & PREVIOUS MISTAKES ADDRESSED:
 *    - ASCII BUG FIX: In earlier iterations, using `data.charAt(index)` caused Java
 *      to convert characters to their ASCII integer equivalents (e.g., character '1'
 *      became integer 49). Using `Integer.parseInt(val)` on string tokens parses
 *      the literal value 1 correctly.
 *    - MULTI-DIGIT & NEGATIVE NUMBERS: `charAt()` only reads 1 character at a time,
 *      failing on numbers like "12" or "-5". Tokenizing via `split(",")` captures
 *      the entire number token intact.
 *    - QUEUE VS. MANUAL INDEXING: Using a Queue (`queue.poll()`) sequentially consumes
 *      tokens in O(1) time without needing to manually mutate or synchronize
 *      a fragile global pointer variable (`index`).
 * ============================================================================
 */
public class SerializeAndDeserializeBinaryTree {

    // Note: Previously used for manual array indexing. Kept for reference, but
    // left unused here because we pass a self-advancing Queue instead.
    static int index = 0;

    /**
     * Encodes a tree to a single string using Pre-Order DFS.
     *
     * STEP-BY-STEP EXPLANATION:
     * 1. Base Case: If the current node is null, return marker "N".
     * 2. Process current root value.
     * 3. Recursively serialize left subtree, then right subtree.
     * 4. Join values using comma separators.
     *
     * TIME COMPLEXITY: O(N²) in this current version
     *   - String concatenation (+) creates new string copies at each node depth.
     *   - OPTIMIZATION TIP: Passing a `StringBuilder` reduces this to O(N).
     *
     * SPACE COMPLEXITY: O(N)
     *   - Call stack takes O(H) space, where H is tree height (O(N) worst-case).
     *   - Output string holds N nodes + N+1 null markers.
     */
    public static String serialize(TreeNode root) {
        // Base case: Null nodes are represented as "N"
        if (root == null) {
            return "N";
        }

        // Pre-order traversal: [Root] + "," + [Left Subtree] + "," + [Right Subtree]
        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }

    /**
     * Decodes your encoded data string back into a binary tree.
     *
     * STEP-BY-STEP EXPLANATION:
     * 1. Split the string by commas into full string tokens.
     * 2. Load tokens into an ArrayDeque (Queue) for O(1) sequential retrieval via poll().
     * 3. Pass the queue to recursive helper buildTree().
     *
     * TIME COMPLEXITY: O(N)
     *   - `data.split(",")` scans the string once in O(N).
     *   - Queue initialization takes O(N) time.
     *
     * SPACE COMPLEXITY: O(N)
     *   - Holds N node tokens in the ArrayDeque.
     */
    public static TreeNode deserialize(String data) {
        // Step 1: Tokenize string into full elements (handles multi-digit & negatives)
        String[] nodes = data.split(",");

        // Step 2: Use ArrayDeque queue instead of Arrays.asList to allow mutation/polling.
        // List.of(nodes) provides an unmodifiable list fed into the ArrayDeque constructor.
        Queue<String> queue = new ArrayDeque<>(List.of(nodes));

        // Step 3: Initiate recursive reconstruction
        return buildTree(queue);
    }

    /**
     * Helper method to recursively build the tree from token queue.
     *
     * STEP-BY-STEP EXPLANATION:
     * 1. Extract the next available token from the queue using `poll()`.
     * 2. Base Case: If token is "N", return null (no node here).
     * 3. Convert token string to actual integer using `Integer.parseInt()`.
     * 4. Create current TreeNode.
     * 5. Recursively build left child first, then right child (matching Pre-Order input).
     * 6. Return the constructed subtree root.
     *
     * TIME COMPLEXITY: O(N)
     *   - Each of the N tokens in the queue is polled and processed exactly once in O(1) time.
     *
     * SPACE COMPLEXITY: O(H) call stack space
     *   - O(log N) for balanced trees, O(N) for completely skewed/line trees.
     */
    public static TreeNode buildTree(Queue<String> queue) {
        // Step 1: Retrieve and remove the next head token in O(1) time
        String val = queue.poll();

        // Step 2: Check for null marker base case
        if (val.equals("N")) {
            return null;
        }

        // Step 3: Integer.parseInt handles:
        //   a) Multi-digit numbers (e.g. "120")
        //   b) Negative numbers (e.g. "-5")
        //   c) Avoids the ASCII char bug ('1' -> 49) encountered with charAt()
        TreeNode node = new TreeNode(Integer.parseInt(val));

        // Step 4: Reconstruct subtrees in exact Pre-Order sequence (Left -> Right)
        node.left = buildTree(queue);
        node.right = buildTree(queue);

        // Step 5: Return node to parent caller
        return node;
    }

    public static void main(String[] args) {
        // Construct Sample Tree:
        //        1
        //       / \
        //      2   3
        //         / \
        //        4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);

        // Test Serialization: Expected output "1,2,N,N,3,4,N,N,5,N,N"
        String serialization = serialize(root);
        System.out.println("Serialized: " + serialization);

        // Test Deserialization: Reconstructs tree back into equivalent TreeNode structure
        TreeNode deserializedRoot = deserialize(serialization);
        System.out.println("Deserialized Root Val: " + deserializedRoot.val);
    }
}