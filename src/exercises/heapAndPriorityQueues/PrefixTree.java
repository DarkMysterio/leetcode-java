package exercises.heapAndPriorityQueues;

import java.util.HashMap;

/**
 * ============================================================================
 * PROBLEM: Implement Trie / Prefix Tree (LeetCode 208)
 * PATTERN: Tree Traversal via Edge Lookups
 * ============================================================================
 *
 * DESIGN CONCEPTS & UNCERTAINTIES ADDRESSED:
 *
 * 1. THE DUMMY ROOT (`placeHolder`):
 *    - The root node represents an empty prefix (""). It holds no character value
 *      of its own, but its `nextLetters` map acts as the starting point for all
 *      words inserted into the Trie.
 *
 * 2. SEARCH VS. STARTSWITH DIFFERENCE:
 *    - `search(word)`: Requires that the entire string is matched AND `isEndNode == true`
 *      (verifying that this exact word was inserted, not just a prefix of another word).
 *    - `startsWith(prefix)`: Requires only that every character of `prefix` exists along
 *      a path in the tree. It doesn't matter if `isEndNode` is true or false.
 *
 * 3. CLEAN BOOLEAN RETURNS:
 *    - Directly returning `currentNode.isEndNode` replaces redundant `if (isEndNode) return true; else return false;`
 *      blocks, keeping code clean and idiomatic.
 * ============================================================================
 */
class PrefixTree {

    // Entry point of the Trie (represents empty string prefix)
    PrefixNode placeHolder;

    public PrefixTree() {
        placeHolder = new PrefixNode(new HashMap<>());
    }

    /**
     * Inserts a word into the prefix tree character by character.
     *
     * WHAT IT DOES:
     * Iterates through the string. For each character:
     * - If an outgoing edge exists in `nextLetters`, navigate to that child node.
     * - If no edge exists, create a new PrefixNode and attach it to `nextLetters`.
     * - At the final character, set `isEndNode = true`.
     */
    public void insert(String word) {
        PrefixNode currentNode = placeHolder;

        for (int i = 0; i < word.length(); i++) {
            Character letter = word.charAt(i);

            // If the path for this character doesn't exist yet, build it
            if (!currentNode.nextLetters.containsKey(letter)) {
                PrefixNode node = new PrefixNode(new HashMap<>());
                currentNode.nextLetters.put(letter, node);
                currentNode = node; // Move pointer to newly created node
            }
            // If path already exists, simply step down into the existing branch
            else {
                currentNode = currentNode.nextLetters.get(letter);
            }
        }

        // Mark the last node as the end of a valid word
        currentNode.isEndNode = true;
    }

    /**
     * Searches for an EXACT word match in the prefix tree.
     */
    public boolean search(String word) {
        PrefixNode currentNode = placeHolder;

        for (int i = 0; i < word.length(); i++) {
            Character character = word.charAt(i);

            // If at any point the next letter path doesn't exist, the word is absent
            if (currentNode.nextLetters.containsKey(character)) {
                currentNode = currentNode.nextLetters.get(character);
            } else {
                return false;
            }
        }

        // WHAT WE DO: Return boolean value of `isEndNode` directly.
        // WHY: Reaching the end of the loop means all characters exist, but it's
        // only a valid word if `isEndNode` was explicitly set during an `insert()`.
        return currentNode.isEndNode;
    }

    /**
     * Checks if any word in the tree starts with the given prefix.
     */
    public boolean startsWith(String prefix) {
        PrefixNode currentNode = placeHolder;

        for (int i = 0; i < prefix.length(); i++) {
            Character character = prefix.charAt(i);

            if (currentNode.nextLetters.containsKey(character)) {
                currentNode = currentNode.nextLetters.get(character);
            } else {
                return false;
            }
        }

        // FIX: Reaching here means ALL characters of `prefix` were successfully matched.
        // It does NOT matter whether `isEndNode` is true or false!
        return true;
    }
}