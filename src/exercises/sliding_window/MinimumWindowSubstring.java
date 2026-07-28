package exercises.sliding_window.visualize;

import java.util.HashMap;

/**
 * ============================================================================
 * PROBLEM: Minimum Window Substring (LeetCode 76)
 * PATTERN: Dynamic Sliding Window (Expand Right, Shrink Left)
 * ============================================================================
 * TIME COMPLEXITY:  O(N + M) where N = s.length(), M = t.length()
 * SPACE COMPLEXITY: O(1) auxiliary space (HashMaps max size 128 ASCII chars)
 * ============================================================================
 */
public class MinimumWindowSubstring {

    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) {
            return "";
        }

        // STEP 1: Build target frequency map for 't'
        HashMap<Character, Integer> t_freq = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            t_freq.put(c, t_freq.getOrDefault(c, 0) + 1);
        }

        // Map for characters in current window
        HashMap<Character, Integer> window_freq = new HashMap<>();

        // 'need' = unique characters in t that need to be satisfied
        // 'have' = unique characters currently satisfied in the window
        int need = t_freq.size();
        int have = 0;

        // Track minimum window boundaries
        int minLen = Integer.MAX_VALUE;
        int minStart = 0;

        int l = 0;

        // STEP 2: EXPAND the window with right pointer 'r'
        for (int r = 0; r < s.length(); r++) {
            char rightChar = s.charAt(r);
            window_freq.put(rightChar, window_freq.getOrDefault(rightChar, 0) + 1);

            // If rightChar is in 't' AND we reached its required frequency in the window
            // Note: Use .equals() for Integer object comparison in Java!
            if (t_freq.containsKey(rightChar) &&
                    window_freq.get(rightChar).equals(t_freq.get(rightChar))) {
                have++;
            }

            // STEP 3: SHRINK the window with left pointer 'l' while window IS VALID
            while (have == need) {
                // Update minimum substring tracking
                int currentLen = r - l + 1;
                if (currentLen < minLen) {
                    minLen = currentLen;
                    minStart = l;
                }

                // Try to shrink from left
                char leftChar = s.charAt(l);
                window_freq.put(leftChar, window_freq.get(leftChar) - 1);

                // If removing leftChar makes the window invalid, decrement 'have'
                if (t_freq.containsKey(leftChar) &&
                        window_freq.get(leftChar) < t_freq.get(leftChar)) {
                    have--;
                }

                l++; // Shrink left boundary
            }
        }

        // Return minimum window substring or empty string if no valid window was found
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }
}