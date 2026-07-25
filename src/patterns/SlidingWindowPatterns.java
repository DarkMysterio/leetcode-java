package patterns;

import java.util.*;

/**
 * ============================================================
 * SLIDING WINDOW PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Problem involves a contiguous subarray or substring
 *  - Looking for max/min/count of something in a window
 *  - "At most k distinct", "no repeating chars", "sum >= target"
 *
 * Two flavors:
 *  A) FIXED-SIZE window — window size k stays constant
 *     → Right moves forward; Left = right - k + 1
 *  B) VARIABLE-SIZE window — window expands/shrinks based on condition
 *     → Right always moves forward; Left shrinks when window is invalid
 *
 * Key trick: Use a HashMap/array to track window state as you slide.
 *
 * Time: O(n)  Space: O(k) where k = window size or alphabet size
 */
public class SlidingWindowPatterns {

    public static void main(String[] args) {
        System.out.println("=== Longest Substring Without Repeating ===");
        System.out.println(lengthOfLongestSubstringSolved("abcabcbb")); // 3
        System.out.println(lengthOfLongestSubstringSolved("bbbbb"));    // 1

        System.out.println("\n=== Permutation in String ===");
        System.out.println(checkInclusionSolved("ab", "eidbaooo"));  // true
        System.out.println(checkInclusionSolved("ab", "eidboaoo"));  // false

        System.out.println("\n=== Minimum Size Subarray Sum ===");
        System.out.println(minSubarraySumSolved(7, new int[]{2,3,1,2,4,3})); // 2
        System.out.println(minSubarraySumSolved(4, new int[]{1,4,4}));       // 1
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE A — Fixed-Size Window
    // ─────────────────────────────────────────────
    static int fixedWindowTemplate(int[] nums, int k) {
        int windowSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right]; // expand window by adding right element

            if (right >= k - 1) {   // window has reached size k
                maxSum = Math.max(maxSum, windowSum);  // process current window
                windowSum -= nums[right - k + 1];      // shrink: remove leftmost element
            }
        }
        return maxSum;
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE B — Variable-Size Window
    // ─────────────────────────────────────────────
    static int variableWindowTemplate(int[] nums, int target) {
        int left = 0;
        int windowSum = 0;
        int result = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right]; // always expand by adding right element

            // Shrink window from left while condition is satisfied
            while (windowSum >= target) {
                result = Math.min(result, right - left + 1); // record window size
                windowSum -= nums[left]; // remove leftmost element
                left++;                  // shrink from left
            }
        }

        return result == Integer.MAX_VALUE ? 0 : result;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS
    // ─────────────────────────────────────────────
    // LeetCode 3 — Medium
    // Find the length of the longest substring with no duplicate characters.
    //
    // Example: "abcabcbb" → 3  ("abc")
    //          "bbbbb"    → 1  ("b")
    //
    // Approach (variable window + set):
    //  - Use a HashSet to track characters in the current window
    //  - Right pointer expands; if duplicate found, shrink from left
    //  - Track max window size seen
    //
    // Time: O(n)  Space: O(min(n, alphabet))

    // TODO VERSION
    static int lengthOfLongestSubstringTODO(String s) {
        Set<Character> window = new HashSet<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // TODO: while c is already in window, shrink from left:
            //   remove s.charAt(left) from window, left++

            // TODO: add c to window
            // TODO: update maxLen = Math.max(maxLen, right - left + 1)
        }

        return maxLen;
    }

    // SOLVED VERSION
    static int lengthOfLongestSubstringSolved(String s) {
        Set<Character> window = new HashSet<>(); // tracks chars in current window
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // Window is invalid if c is already in it → shrink from left
            while (window.contains(c)) {
                window.remove(s.charAt(left)); // remove leftmost char
                left++;                        // shrink window
            }

            window.add(c);                                     // add new char to window
            maxLen = Math.max(maxLen, right - left + 1);      // update best length
        }

        return maxLen;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: PERMUTATION IN STRING
    // ─────────────────────────────────────────────
    // LeetCode 567 — Medium
    // Given strings s1 and s2, return true if s2 contains a permutation of s1.
    //
    // Example: s1="ab", s2="eidbaooo" → true  ("ba" is in s2)
    //          s1="ab", s2="eidboaoo" → false
    //
    // Approach (fixed window + frequency arrays):
    //  - Window size = s1.length()
    //  - Use int[26] to compare char frequencies
    //  - Slide the window across s2, checking if frequencies match s1
    //
    // Time: O(n)  Space: O(1) — arrays of size 26

    // TODO VERSION
    static boolean checkInclusionTODO(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] need = new int[26];  // frequency of each char in s1
        int[] have = new int[26];  // frequency of each char in current window

        // TODO: fill 'need' with char counts from s1

        int k = s1.length(); // fixed window size

        for (int right = 0; right < s2.length(); right++) {
            // TODO: add s2.charAt(right) to 'have' window

            // TODO: if window size > k, remove leftmost char from 'have'

            // TODO: if window size == k, check if have[] equals need[] → return true
        }

        return false;
    }

    // SOLVED VERSION
    static boolean checkInclusionSolved(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] need = new int[26]; // target char frequencies from s1
        int[] have = new int[26]; // current window char frequencies

        // Count characters needed from s1
        for (char c : s1.toCharArray()) need[c - 'a']++;

        int k = s1.length(); // fixed window size = s1 length

        for (int right = 0; right < s2.length(); right++) {
            have[s2.charAt(right) - 'a']++; // add new char to window

            // Window too large → remove leftmost char
            if (right >= k) {
                have[s2.charAt(right - k) - 'a']--; // remove char that's now outside window
            }

            // Window has reached target size → check if frequencies match
            if (right >= k - 1 && Arrays.equals(need, have)) {
                return true; // found a permutation
            }
        }

        return false;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: MINIMUM SIZE SUBARRAY SUM
    // ─────────────────────────────────────────────
    // LeetCode 209 — Medium
    // Find the minimal length subarray whose sum >= target.
    // Return 0 if no such subarray exists.
    //
    // Example: target=7, nums=[2,3,1,2,4,3] → 2  (subarray [4,3])
    //          target=4, nums=[1,4,4]        → 1  (subarray [4])
    //
    // Approach (variable window):
    //  - Expand by adding right element to windowSum
    //  - Shrink from left while sum >= target, recording minimum length
    //
    // Time: O(n)  Space: O(1)

    // TODO VERSION
    static int minSubarraySumTODO(int target, int[] nums) {
        int left = 0;
        int windowSum = 0;
        int minLen = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            // TODO: add nums[right] to windowSum

            // TODO: while windowSum >= target:
            //   update minLen = Math.min(minLen, right - left + 1)
            //   remove nums[left] from windowSum
            //   left++
        }

        // TODO: return 0 if no valid window found, else return minLen
        return 0;
    }

    // SOLVED VERSION
    static int minSubarraySumSolved(int target, int[] nums) {
        int left = 0;
        int windowSum = 0;
        int minLen = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right]; // expand: add right element

            // Condition met → try to shrink window to find minimum length
            while (windowSum >= target) {
                minLen = Math.min(minLen, right - left + 1); // record this window size
                windowSum -= nums[left]; // remove leftmost element
                left++;                  // shrink from left
            }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen; // 0 if no valid window
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH SLIDING WINDOW
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  window size = right - left        (off by one!)
     * ✅ RIGHT:  window size = right - left + 1
     *
     * ❌ WRONG:  removing s2.charAt(right - k + 1) (should be right - k)
     * ✅ RIGHT:  when right >= k: remove s2.charAt(right - k)
     *            Think: indices 0..k-1 are first window, shift by 1 each step
     *
     * ❌ WRONG:  checking window equality before window reaches size k
     * ✅ RIGHT:  if (right >= k - 1) { check equality }
     *
     * ❌ WRONG:  Arrays.equals(have, need) won't work if comparing List<Integer>
     * ✅ RIGHT:  Arrays.equals works for int[] — for other types use equals() carefully
     *
     * ❌ WRONG:  using HashMap when int[26] works (lowercase letters only)
     * ✅ RIGHT:  int[26] is faster (O(1) comparison with Arrays.equals)
     */
}
