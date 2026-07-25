package exercises;

import java.util.*;

public class GroupAnagrams {

    /*
     * Time Complexity : O(N * K)
     * - N = number of strings, K = maximum length of a string.
     * - Iterating through each string takes O(K) to build frequencies.
     * - Arrays.toString() on size 26 array takes O(26) = O(1) constant time.
     * - Overall time scales linearly with total characters across all strings.
     *
     * Space Complexity: O(N * K)
     * - HashMap stores up to N keys and all strings of total length (N * K) in memory.
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // Syntax/Why: Guard clause to handle edge cases and prevent NullPointerException O(1)
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }

        // Syntax/Why: Maps unique character frequency key -> matching list of anagrams
        Map<String, List<String>> map = new HashMap<>();

        for (String str : strs) {
            // Syntax/Why: Fixed-size table for 'a'-'z' character counts (O(1) auxiliary space per word)
            int[] freq = new int[26];

            // Syntax/Why: Convert string to char array to iterate; offset 'a' maps 'a'->0, 'b'->1, etc.
            for (char c : str.toCharArray()) {
                freq[c - 'a']++;
            }

            // Syntax/Why: Convert array to string (e.g., "[1, 0, 0...]") so identical counts produce identical keys
            String key = Arrays.toString(freq);

            // Syntax/Why: Ensure an ArrayList exists for this key if seen for the first time
            map.putIfAbsent(key, new ArrayList<>());

            // Syntax/Why: Fetch the list associated with key and append current word
            map.get(key).add(str);
        }

        // Syntax/Why: Wrap map.values() (Collection) into an ArrayList to match List<List<String>> return type
        return new ArrayList<>(map.values());
    }
}