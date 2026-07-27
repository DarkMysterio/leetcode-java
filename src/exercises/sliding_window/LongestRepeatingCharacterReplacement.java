package exercises.sliding_window;

import java.util.HashMap;

/**
 * ============================================================================
 * PROBLEM: Longest Repeating Character Replacement (LeetCode 424)
 * PATTERN: Dynamic Sliding Window + Frequency Tracking
 * ============================================================================
 * TIME COMPLEXITY: O(N)
 *   - 'r' moves forward N times; 'l' moves forward at most N times overall.
 *
 * SPACE COMPLEXITY: O(1)
 *   - HashMap is bounded by the alphabet size (max 26 or 128 entries).
 * ============================================================================
 */
public class LongestRepeatingCharacterReplacement {

    public int characterReplacement(String s, int k) {
        // Base Case 1: Null check safety guard
        if (s == null) {
            return 0;
        }

        // Base Case 2: Any string of length 0 or 1 is inherently valid
        if (s.length() <= 1) {
            return s.length();
        }

        int maxSize = 0;

        // Tracks frequency of characters inside the active sliding window [l, r]
        HashMap<Character, Integer> count = new HashMap<>();

        // 'l' represents the left boundary of our sliding window
        int l = 0;

        // 'maxFreq' tracks the maximum count of any SINGLE character in our window
        int maxFreq = 0;

        // 'r' represents the right boundary, expanding the window one step at a time
        for (int r = 0; r < s.length(); r++) {

            // WHY: Extract the new character entering the window on the right
            char rightChar = s.charAt(r);

            // WHY: Update the character's frequency count in our window map
            count.put(rightChar, count.getOrDefault(rightChar, 0) + 1);

            // WHY: Update the peak frequency seen in our window.
            // We only need to check the character we JUST added at index 'r'.
            maxFreq = Math.max(maxFreq, count.get(rightChar));

            // WHY: Validate window state!
            // Condition: (Total Window Length - Max Frequency Character) > k
            // If this is true, we need MORE than 'k' replacements to make this
            // window uniform, which violates our budget of 'k'.
            while (r - l + 1 - maxFreq > k) {
                // WHY: Decrement count of the character leaving the left of the window
                count.put(s.charAt(l), count.getOrDefault(s.charAt(l), 0) - 1);

                // WHY: Shrink window boundary from the left to regain validity
                l += 1;

                // NOTE ON PREVIOUS UNCERTAINTY:
                // We do NOT recalculate maxFreq here when 'l' advances.
                // A lower maxFreq cannot yield a larger valid window than our
                // current best, so keeping maxFreq as a historical max is safe!
            }

            // WHY: Calculate current valid window size and update global record
            int window_size = r - l + 1;
            maxSize = Math.max(maxSize, window_size);

        }

        return maxSize;
    }
}