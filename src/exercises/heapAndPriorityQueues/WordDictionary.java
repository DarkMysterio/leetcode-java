package exercises.heapAndPriorityQueues;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * PROBLEM: Design Add and Search Words Data Structure (LeetCode 211)
 * PATTERN: Trie (Prefix Tree) + Depth-First Search (DFS) Recursion
 * ============================================================================
 *
 * KEY ARCHITECTURAL CONCEPTS:
 * 1. TRIE NODE DESIGN:
 *    - Nodes store a `children` Map and an `isEndOfWord` boolean.
 *    - Redundant character fields are omitted because the character identity
 *      lives in the parent node's Map key.
 *
 * 2. INDEX POINTER VS. STRING SLICING:
 *    - Instead of creating costly `word.substring(1)` copies on every recursive
 *      step, we pass an integer `index` pointer. This keeps string memory
 *      overhead at O(1) across recursive calls.
 *
 * 3. WILDCARD ('.') HANDLING VIA DFS:
 *    - When encountering '.', we don't know which path to take. We must explore
 *      ALL available children in `curr.children.values()` recursively.
 * ============================================================================
 */
public class WordDictionary {

    private final TrieNode root;

    /**
     * Internal node structure representing a single state in the prefix tree.
     */
    public static class TrieNode {
        // Map connecting character edges to child TrieNodes
        Map<Character, TrieNode> children;

        // Indicates if a word in the dictionary ends at this node
        boolean isEndOfWord;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
        }
    }

    /**
     * Initializes the WordDictionary with an empty root node.
     */
    public WordDictionary() {
        root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie.
     *
     * TIME COMPLEXITY:  O(N), where N is word length.
     * SPACE COMPLEXITY: O(N) worst-case (when creating N new nodes).
     */
    public void addWord(String word) {
        TrieNode curr = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            // Build path if character edge doesn't exist
            if (!curr.children.containsKey(ch)) {
                curr.children.put(ch, new TrieNode());
            }
            // Step down into the child node
            curr = curr.children.get(ch);
        }
        // Mark the completion of a valid word
        curr.isEndOfWord = true;
    }

    /**
     * Public entry point for searching a word (supports '.' wildcards).
     */
    public boolean search(String word) {
        // Kick off DFS starting at string index 0 and tree root node
        return searchHelp(word, 0, root);
    }

    /**
     * Helper Method: Traverses the Trie recursively using Depth-First Search.
     *
     * @param word  The target string we are searching for.
     * @param index Pointer tracking our current position in `word`.
     * @param curr  Pointer tracking our current node position in the Trie.
     * @return true if a matching word path is found; false otherwise.
     */
    private boolean searchHelp(String word, int index, TrieNode curr) {

        // ====================================================================
        // BASE CASE 1: Reached the end of the search word string
        // ====================================================================
        // WHY THIS WAS FIXED:
        // Returning `true` unconditionally here was a previous bug because matching
        // prefix letters (e.g. searching "ap" when only "app" exists) is NOT enough.
        // We MUST verify that `curr.isEndOfWord` is true.
        if (index == word.length()) {
            return curr.isEndOfWord;
        }

        char ch = word.charAt(index);

        // ====================================================================
        // BRANCH A: Wildcard Matching ('.' character)
        // ====================================================================
        if (ch == '.') {
            // Traverse all existing child branches at this level
            for (TrieNode child : curr.children.values()) {

                // WHY THIS WAS FIXED:
                // We MUST evaluate the boolean result of `searchHelp(...)`.
                // If ANY child branch successfully completes the rest of the search,
                // return true immediately (short-circuiting remaining iterations).
                if (searchHelp(word, index + 1, child)) {
                    return true;
                }
            }

            // If none of the child branches matched the remainder of the word, return false
            return false;
        }

        // ====================================================================
        // BRANCH B: Exact Character Matching (e.g., 'a'-'z')
        // ====================================================================
        else {
            // If the character edge doesn't exist in the current node's map, path fails
            if (!curr.children.containsKey(ch)) {
                return false;
            }

            // Path exists: advance `index` by 1 and move `curr` pointer to child node
            return searchHelp(word, index + 1, curr.children.get(ch));
        }
    }
}