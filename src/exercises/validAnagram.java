package exercises;

public class validAnagram {
    public boolean isAnagram(String s, String t) {
        /*
         * Time Complexity: O(n)
         * - Where 'n' is the length of strings 's' and 't'.
         * - We iterate through the strings of length 'n' exactly once, performing O(1) array access operations.
         * - The final validation loop runs a fixed 26 times regardless of input size, which is O(1).
         * - Therefore, the overall time complexity is linear, O(n).
         *
         * Space Complexity: O(1)
         * - The frequency array size is hardcoded to 26 (for lowercase English letters 'a' through 'z').
         * - Memory allocation remains constant regardless of how large the input strings grow.
         */

        // WHAT: Early exit check comparing the lengths of both strings.
        // WHY: Anagrams must have the exact same number of total characters.
        //      If lengths differ, they cannot be anagrams, saving unnecessary computation.
        if (s.length() != t.length()) {
            return false;
        }

        // WHAT: Fixed-size array representing letter counts for 'a' through 'z'.
        // WHY: Memory-efficient alternative to a HashMap. Primitive arrays avoid
        //      Object boxing overhead and provide faster O(1) lookup times.
        int[] freq = new int[26];

        // WHAT: Single pass loop over both strings simultaneously.
        // WHY: Increment counts for characters in string 's' and decrement for string 't'.
        //      Subtracting 'a' (ASCII 97) converts characters into 0-indexed values ('a' -> 0, 'b' -> 1, etc.).
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
            freq[t.charAt(i) - 'a']--;
        }

        // WHAT: Iterate through the fixed 26-element array to verify frequencies.
        // WHY: If 's' and 't' are anagrams, every increment from 's' should be cancelled out
        //      by a corresponding decrement from 't', leaving every frequency slot at exactly 0.
        for (int count : freq) {
            if (count != 0) {
                return false; // Found a mismatch in character frequencies
            }
        }

        // WHAT: Return true if all 26 character slots balance out to 0.
        // WHY: Confirms that both strings contain identical characters with identical frequencies.
        return true;
    }

}
