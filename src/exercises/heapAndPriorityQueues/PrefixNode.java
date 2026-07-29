package exercises.heapAndPriorityQueues;

import java.util.HashMap;

/**
 * ============================================================================
 * NODE STRUCTURE FOR PREFIX TREE (TRIE)
 * ============================================================================
 * KEY INSIGHT / MISTAKE PREVENTED:
 * Notice that we do NOT store a `char letter` field inside this node.
 *
 * WHY?
 * The character identity is already stored implicitly as the KEY in the parent's
 * `nextLetters` HashMap! Storing it inside the child node as well is redundant
 * and wastes memory across thousands of tree nodes.
 * ============================================================================
 */
public class PrefixNode {

    // Map linking an outgoing character edge (e.g., 'a') to its child PrefixNode
    HashMap<Character, PrefixNode> nextLetters;

    // Marks whether a valid word ends at this specific node in the tree
    boolean isEndNode;

    /**
     * Constructor initializing the outgoing edge map.
     * `isEndNode` defaults to false until an insertion explicitly marks it true.
     */
    public PrefixNode(HashMap<Character, PrefixNode> nextLetters) {
        this.nextLetters = nextLetters;
        this.isEndNode = false;
    }
}