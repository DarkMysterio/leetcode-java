package exercises.sliding_window;

import java.util.HashSet;
import java.util.Set;

/**
 * ============================================================================
 * PROBLEM: Longest Substring Without Repeating Characters
 * APPROACH: Dynamic Sliding Window (Two Pointers + Dynamic Shrinking via Set)
 * ============================================================================
 *
 * CORE LOGIC & STRATEGY:
 * - We maintain a sliding window defined by two pointers:
 *     - 'p' (left boundary): Shrinks the window from the left when duplicates occur.
 *     - 'q' (right boundary): Expands the window to the right by visiting new characters.
 * - A HashSet (`letters`) acts as a fast lookup table to check if the current character
 *   at index 'q' already exists inside our current window [p, q].
 * - When a duplicate character is detected at 'q', we incrementally remove characters from
 *   the left of our window (`letters.remove(s.charAt(p))` and `p++`) until the duplicate
 *   character is purged from the set.
 * - Once the window is guaranteed to be valid and duplicate-free, we insert `s.charAt(q)`
 *   and update `maxLength`.
 *
 * ============================================================================
 * COMPLEXITY ANALYSIS:
 * ============================================================================
 * TIME COMPLEXITY: O(N)
 *   - Although there is a nested `while` loop inside the outer `while` loop, the inner
 *     loop doesn't reset 'p'.
 *   - Each character in the string 's' is added to the HashSet at most ONCE by pointer 'q',
 *     and removed from the HashSet at most ONCE by pointer 'p'.
 *   - Therefore, both 'p' and 'q' travel at most N steps across the string, leading to
 *     at most 2N total operations. This gives an amortized time complexity of O(N).
 *
 * SPACE COMPLEXITY: O(min(N, M))
 *   - Auxiliary space is driven by the HashSet storing unique characters in the current window.
 *   - 'N' is the total length of the string 's'.
 *   - 'M' is the size of the character set (e.g., M = 128 for standard ASCII or 26 for
 *     lowercase English alphabet).
 *   - In the worst case, the set will never store more characters than the total unique
 *     characters present in the character set.
 * ============================================================================
 */
public class LongestSubstringWithoutRepeatingChar {

    public int lengthOfLongestSubstring(String s) {
        // Step 1: Null check safety guard
        if(s == null){
            return 0;
        }

        // Step 2: Base case handling.
        // An empty string returns 0; a single character string returns 1.
        if(s.length() <= 1){
            return s.length();
        }

        // Track the maximum length found so far.
        // Since length > 1 at this point, the minimum valid substring length is at least 1.
        int maxLength = 1;

        // HashSet to store unique characters present in the active sliding window [p, q]
        Set<Character> letters = new HashSet<>();

        // Seed the window with the very first character at index 0
        letters.add(s.charAt(0));

        // 'p' is the left pointer (window start) initialized to 0
        // 'q' is the right pointer (window end) initialized to 1
        int p = 0, q = 1;

        // Step 3: Expand the right pointer 'q' through the remainder of the string
        while(q < s.length()){
            Character currentChar = s.charAt(q);

            // Step 4: Duplicate resolution loop (Shrink Window)
            // If 'currentChar' is already in our set, the current window [p, q] is invalid.
            // We iteratively remove characters from the left boundary 'p' and advance 'p'
            // until 'currentChar' is no longer in 'letters'.
            while( letters.contains(currentChar)){
                letters.remove(s.charAt(p));
                p += 1; // Shrink window from the left
            }

            // Step 5: Add the now-unique rightmost character to our active set
            letters.add(currentChar);

            // Step 6: Calculate current valid window size [p, q] (inclusive length)
            int windowSize = q - p + 1;

            // Step 7: Record new global max length if current window size beats previous record
            if(windowSize > maxLength){
                maxLength = windowSize;
            }

            // Step 8: Advance the right pointer to inspect the next character
            q += 1;
        }

        // Return the global maximum length found across all valid sliding windows
        return maxLength;
    }
}