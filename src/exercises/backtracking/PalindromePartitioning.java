package exercises.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Palindrome Partitioning (LeetCode 131)
 * PATTERN: Binary Decision Tree Backtracking (Cut vs. Merge Choice)
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. SOLUTION VALIDATION & CODE ANALYSIS
 * ----------------------------------------------------------------------------
 * - FUNCTIONAL CORRECTNESS: YES, your solution is functionally CORRECT!
 *   For a string of length N, there are N-1 potential partition points ("cuts").
 *   For every character from index 1 to N-1, your code makes a binary choice:
 *     Choice 1: Start a new string element (Put a cut before s.charAt(index)).
 *     Choice 2: Merge into the last string element (No cut, append to previous).
 *   This systematically generates all 2^(N-1) possible partition configurations.
 *
 * - CRITICAL EDGE CASE TRAP:
 *   If `s` is empty (""), `s.charAt(0)` in `partition()` will throw an
 *   `IndexOutOfBoundsException`. Always add a guard condition for `s.isEmpty()`.
 *
 * - PRUNING / PERFORMANCE TRAP:
 *   Your code currently waits until `index == s.length()` to validate if all
 *   partition elements are palindromes.
 *   This forces the recursion to explore ALL 2^(N-1) leaf branches, even if
 *   a substring generated near the top of the tree (e.g., "ab") is NOT a
 *   palindrome. In optimal backtracking, we validate immediately before making
 *   a recursive call to prune invalid branches early!
 *
 * - JAVA VERSIONING NOTE:
 *   `currentArray.removeLast()` is introduced in Java 21 (`SequencedCollection`).
 *   For broader compatibility (Java 8/11/17 on LeetCode/interviews),
 *   use `currentArray.remove(currentArray.size() - 1)`.
 *
 * ----------------------------------------------------------------------------
 * 2. WHAT YOU MISTOOK ABOUT JAVA STRINGS (THE MENTAL MODEL)
 * ----------------------------------------------------------------------------
 * - TRAP: Attempting `currentArray.getLast() += "A"` or `currentArray.get(i) += c`.
 *
 * - WHY IT FAILS IN JAVA:
 *   1. Strings in Java are IMMUTABLE. Methods like `concat()` or operators like
 *      `+` create a BRAND NEW String object in memory; they never modify the
 *      original string in-place.
 *   2. `list.get(i)` returns an R-VALUE (the evaluated object reference), NOT
 *      an assignable variable storage location (L-value reference like in C++).
 *
 * - THE STANDARD PATTERN FOR LIST + STRING BACKTRACKING:
 *   To modify a string inside a List and undo it cleanly:
 *     a) Snapshot:   `String original = list.get(lastIndex);`
 *     b) CHOOSE:     `list.set(lastIndex, original + newChar);`  // Replaces element
 *     c) RECURSE:    `backtrack(...);`
 *     d) UN-CHOOSE:  `list.set(lastIndex, original);`            // Restores snapshot
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * TIME COMPLEXITY: O(N * 2^N)
 *   - There are 2^(N-1) possible partition combinations explored by the binary tree.
 *   - At each leaf node (base case), iterating and validating strings takes O(N) time.
 *   - Total Time: O(N * 2^N).
 *
 * SPACE COMPLEXITY: O(N)
 *   - Call stack reaches a max recursion depth of N.
 *   - `currentArray` holds at most N elements.
 * ============================================================================
 */
public class PalindromePartitioning {

    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();

        // EDGE CASE GUARD: Prevent IndexOutOfBoundsException on empty/null inputs
        if (s == null || s.isEmpty()) {
            return result;
        }

        // INTUITION: Pre-seed `currentArray` with `s.charAt(0)`.
        // Every valid partition of string `s` MUST start with a substring beginning
        // at character index 0. We begin binary choices starting from index 1.
        List<String> start = new ArrayList<>();
        start.add(String.valueOf(s.charAt(0)));

        backtrack(result, start, 1, s);
        return result;
    }

    public void backtrack(List<List<String>> result, List<String> currentArray,
                          int index, String s) {

        // ---------------------------------------------------------------------
        // BASE CASE: REACHED END OF STRING
        // INTUITION: Every character in 's' has been assigned to a substring.
        // Validate if ALL generated substrings in 'currentArray' are palindromes.
        // ---------------------------------------------------------------------
        if (index == s.length()) {
            for (String elem : currentArray) {
                if (!validate(elem)) {
                    return; // Reject this partition path if any substring fails
                }
            }
            // DEEP COPY: Create a new ArrayList snapshot so future pops don't alter this result!
            result.add(new ArrayList<>(currentArray));
            return;
        }

        // ---------------------------------------------------------------------
        // CHOICE 1: START A NEW SUBSTRING (Put a partition "cut" before index)
        // ---------------------------------------------------------------------

        // 1. CHOOSE: Treat s.charAt(index) as a separate single-character string
        currentArray.add(String.valueOf(s.charAt(index)));

        // 2. RECURSE: Move to index + 1
        backtrack(result, currentArray, index + 1, s);

        // 3. UN-CHOOSE (BACKTRACK): Remove the newly appended string
        // Note: Using remove(size - 1) for universal Java 8+ compatibility
        currentArray.remove(currentArray.size() - 1);

        // ---------------------------------------------------------------------
        // CHOICE 2: MERGE INTO PREVIOUS SUBSTRING (No cut, append to last element)
        // ---------------------------------------------------------------------

        // JAVA STRING BACKTRACKING PATTERN:
        // Snapshot the current last string -> Update with list.set() -> Restore with list.set()
        int lastIndex = currentArray.size() - 1;
        String originalString = currentArray.get(lastIndex);

        // 1. CHOOSE: Append s.charAt(index) to the last string element in currentArray
        currentArray.set(lastIndex, originalString + s.charAt(index));

        // 2. RECURSE: Move to index + 1 with the merged last string
        backtrack(result, currentArray, index + 1, s);

        // 3. UN-CHOOSE (BACKTRACK): Restore the original string at lastIndex
        currentArray.set(lastIndex, originalString);
    }

    /**
     * Helper method: Verifies if a string is a palindrome using Two Pointers.
     * Time Complexity: O(K) where K is the length of string 's'.
     */
    public boolean validate(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}