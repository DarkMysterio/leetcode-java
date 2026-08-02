package exercises.backtracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Letter Combinations of a Phone Number (LeetCode 17)
 * PATTERN: Multi-way Decision Tree Backtracking (Choose -> Recurse -> Un-choose)
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION (NATURAL LANGUAGE MENTAL MODEL)
 * ----------------------------------------------------------------------------
 * Imagine you are typing a text message on an old phone keypad.
 * For each digit you press (e.g., '2', '3'), you have a menu of possible letters:
 *   - Pressing '2' gives you a choice between 'a', 'b', or 'c'.
 *   - Pressing '3' gives you a choice between 'd', 'e', or 'f'.
 *
 * We visualize this as a decision tree:
 *   - Tree Depth (Height) = Total number of digits in the input string (N).
 *   - Tree Branches (Width) = The available letters for the current digit (3 or 4).
 *
 * We start at the top with an empty string ("") at index 0.
 * At each step, we pick one letter for the current digit, place it into our
 * current path, and move to the next digit. Once we reach the end of the input,
 * we save our completed word and backtrack (undo our last letter choice) so we
 * can try the next available letter.
 *
 * ----------------------------------------------------------------------------
 * 2. WHY IS THE TIME COMPLEXITY O(4^N * N)?
 * ----------------------------------------------------------------------------
 * Let's break down the math naturally:
 *
 * 1. THE BRANCHING FACTOR (4^N):
 *    - Some digits map to 3 letters ('2', '3', '4', '5', '6', '8').
 *    - Other digits map to 4 letters ('7' -> "pqrs", '9' -> "wxyz").
 *    - In the absolute worst case (e.g., input is "7777"), EVERY single digit
 *      gives us 4 branching choices.
 *    - At Step 1, we have 4 choices.
 *    - At Step 2, each of those 4 choices branches into 4 more choices (4 * 4 = 16).
 *    - At Step N, the total number of bottom leaf combinations is 4 * 4 * ... * 4 = 4^N.
 *
 * 2. THE LEAF COPYING WORK (* N):
 *    - When we reach the bottom of the tree (a leaf node), we have built a full word
 *      inside our `StringBuilder` of length N.
 *    - Converting that `StringBuilder` into a brand new `String` (`currentPath.toString()`)
 *      requires Java to copy all N characters one by one into memory.
 *    - Since we do this N-character copy for all 4^N leaf combinations, the overall
 *      work done at the leaves is 4^N * N.
 *
 * TOTAL TIME COMPLEXITY: O(4^N * N)
 *
 * ----------------------------------------------------------------------------
 * 3. SPACE COMPLEXITY: O(N)
 * ----------------------------------------------------------------------------
 * - The recursion call stack goes as deep as the length of `digits` (N).
 * - Our `StringBuilder` buffer holds at most N characters at any point.
 * - (Note: We exclude the space taken by the final output list `result`).
 * ============================================================================
 */
class LetterCombinationsOfaPhoneNumber {

    public List<String> letterCombinations(String digits) {

        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Handle Empty or Null Input
        // INTUITION: If the user provides no digits (e.g., ""), there are no
        // letters to combine. We return an empty list [] instead of [""]!
        // ---------------------------------------------------------------------
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();

        // ---------------------------------------------------------------------
        // KEYPAD MAPPING
        // INTUITION: Set up our dictionary that maps each phone digit key (2-9)
        // to its corresponding string of available letters.
        // ---------------------------------------------------------------------
        HashMap<Integer, String> phoneDigits = new HashMap<>();
        phoneDigits.put(2, "abc");
        phoneDigits.put(3, "def");
        phoneDigits.put(4, "ghi");
        phoneDigits.put(5, "jkl");
        phoneDigits.put(6, "mno");
        phoneDigits.put(7, "pqrs");
        phoneDigits.put(8, "tuv");
        phoneDigits.put(9, "wxyz");

        // ---------------------------------------------------------------------
        // KICK OFF BACKTRACKING
        // INTUITION: Start at digit index 0 with a fresh, empty StringBuilder buffer.
        // ---------------------------------------------------------------------
        backtrack(result, new StringBuilder(), phoneDigits, 0, digits);

        return result;
    }

    public void backtrack(List<String> result, StringBuilder currentPath,
                          HashMap<Integer, String> phoneDigits, int index, String digits) {

        // ---------------------------------------------------------------------
        // BASE CASE: REACHED A COMPLETE COMBINATION
        // INTUITION: When `index` reaches `digits.length()`, it means we have
        // successfully picked 1 letter for every digit in the input.
        // ---------------------------------------------------------------------
        if (index == digits.length()) {
            // Take a static snapshot of our path buffer and save it into our results
            result.add(currentPath.toString());

            // CRITICAL: Stop exploring deeper! Return control back up to the parent caller.
            return;
        }

        // ---------------------------------------------------------------------
        // ASCII MATH CONVERSION TRICK
        // INTUITION: `digits.charAt(index)` returns a character like '2' (ASCII code 50).
        // Subtracting character '0' (ASCII code 48) cleanly converts '2' into
        // the numeric integer 2 (50 - 48 = 2).
        // ---------------------------------------------------------------------
        Integer currentDigit = digits.charAt(index) - '0';

        // Retrieve the available letters string for this digit (e.g., "abc")
        String currentLettersAtDigit = phoneDigits.get(currentDigit);

        // ---------------------------------------------------------------------
        // CHOOSE -> RECURSE -> UN-CHOOSE LOOP
        // INTUITION: Loop through every candidate letter mapped to this digit.
        // For digit '2', this loop runs 3 times (for 'a', 'b', and 'c').
        // ---------------------------------------------------------------------
        for (int i = 0; i < currentLettersAtDigit.length(); i++) {

            // 1. CHOOSE: Append the current letter to our ongoing path
            currentPath.append(currentLettersAtDigit.charAt(i));

            // 2. RECURSE: Move forward to process the next digit at `index + 1`
            backtrack(result, currentPath, phoneDigits, index + 1, digits);

            // 3. UN-CHOOSE (BACKTRACK): Remove the letter we just added.
            // This resets our `StringBuilder` so the next iteration of this loop
            // can try a different letter cleanly without leftover state!
            currentPath.deleteCharAt(currentPath.length() - 1);
        }
    }
}