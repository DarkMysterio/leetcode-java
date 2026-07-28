package exercises.sliding_window;

import java.util.HashMap;

/**
 * ============================================================================
 * PROBLEM: Permutation in String (LeetCode 567)
 * PATTERN: Fixed-Size Sliding Window + HashMap Equality
 * ============================================================================
 *
 * LESSONS LEARNED & MISTAKES ADDRESSED:
 *
 * 1. FIXED VS. VARIABLE WINDOW:
 *    - Previous mistake: Using nested loops and dynamic shrinking (while inside for).
 *    - Insight: A permutation of 's1' MUST have the exact length of 's1'. The window
 *      size is fixed to s1.length(). We slide this fixed frame 1 step at a time.
 *
 * 2. HASHMAP EQUALITY TRAPS:
 *    - Avoid `s1_count.values() == s2_count.values()` -> '==' checks memory references.
 *    - Avoid `s1_count.values().equals(...)` -> Collection.equals() checks object identity,
 *      not contents, and ignores keys entirely (e.g., {'a':1} matches {'z':1}).
 *    - Correct: `s1_count.equals(window_count)` checks key-value parity in O(1) time
 *      (since alphabet size is bounded at 26).
 *
 * 3. THE ZERO-COUNT KEY CLEANUP BUG:
 *    - Decrementing a key's value to 0 leaves `{'x': 0}` in the map.
 *    - HashMap.equals() compares key sets! If `s1_count` does not contain 'x',
 *      `map1.equals(map2)` returns false despite the frequency effectively being 0.
 *    - Solution: Explicitly `remove()` any key whose count hits 0.
 *
 * 4. AUTO-UNBOXING NULLPOINTEREXCEPTION (NPE):
 *    - Doing `map.get(char) - 1` throws an NPE if `char` isn't in the map, because
 *      Java tries to unbox `null` to primitive `int`. Always check or use `getOrDefault`.
 *
 * ============================================================================
 * TIME COMPLEXITY: O(N)
 *   - Where N = s2.length(). We pass over s2 once. HashMap comparison on max 26 keys
 *     takes O(26) = O(1) time per step.
 *
 * SPACE COMPLEXITY: O(1)
 *   - Maximum of 26 key-value pairs stored in HashMaps at any time.
 * ============================================================================
 */
public class PermutationinString {

    public boolean checkInclusion(String s1, String s2) {

        // MISTAKE PREVENTED: StringIndexOutOfBoundsException
        // WHY: If s1 is longer than s2, s2 cannot contain any permutation of s1.
        if (s1 == null || s2 == null || s1.length() > s2.length()) {
            return false;
        }

        // STEP 1: Build target frequency map for s1
        // WHY: Using initial capacity 26 prevents map resizes for lowercase English letters.
        HashMap<Character, Integer> s1_count = new HashMap<>(26);
        for (int i = 0; i < s1.length(); i++) {
            s1_count.put(s1.charAt(i), s1_count.getOrDefault(s1.charAt(i), 0) + 1);
        }

        // STEP 2: Build frequency map for the FIRST window in s2 [indices 0 ... s1.length() - 1]
        // WHY: Seeding the initial window lets us check index 0 before entering the sliding loop.
        HashMap<Character, Integer> window_count = new HashMap<>(26);
        int window_size = s1.length();
        for (int i = 0; i < window_size; i++) {
            window_count.put(s2.charAt(i), window_count.getOrDefault(s2.charAt(i), 0) + 1);
        }

        // Check if the initial window itself is an exact permutation match
        // WHY: HashMap.equals() checks that both maps have identical key-value pairs.
        if (window_count.equals(s1_count)) {
            return true;
        }

        // STEP 3: Slide the fixed window across s2 from index `window_size` to `s2.length() - 1`
        // WHY A FOR-LOOP: Using 'r' avoids pointer bugs (like missing the final window or
        // incrementing 'r' twice).
        for (int r = window_size; r < s2.length(); r++) {

            // A) ADD the new incoming character on the right boundary
            char charIn = s2.charAt(r);
            window_count.put(charIn, window_count.getOrDefault(charIn, 0) + 1);

            // B) REMOVE the outgoing character from the left boundary
            // WHY (r - window_size): The character exiting the window is strictly
            // `window_size` positions behind the current right boundary 'r'.
            char charOut = s2.charAt(r - window_size);

            // PREVENTING NPE: s2_count.get(charOut) is safe here because charOut was
            // added during a previous iteration and is guaranteed to exist in the map.
            window_count.put(charOut, window_count.get(charOut) - 1);

            // C) CRUCIAL CLEANUP STEP (Prevents Map Parity False-Negatives)
            // WHY: If count reaches 0, we MUST remove the key completely!
            // Otherwise `window_count` has key `charOut: 0`, causing `window_count.equals(s1_count)`
            // to return false even though the character count inside the window is 0.
            if (window_count.get(charOut) == 0) {
                window_count.remove(charOut);
            }

            // D) CHECK MATCH AFTER EVERY SLIDE
            // WHY HERE: Placing this at the end of the loop ensures the final window
            // (when r = s2.length() - 1) is evaluated before the loop terminates.
            if (window_count.equals(s1_count)) {
                return true;
            }
        }

        // If no window matched during traversal, no valid permutation exists.
        return false;
    }
}