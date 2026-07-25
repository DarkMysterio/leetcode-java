package patterns;

import java.util.*;

/**
 * ============================================================
 * HASHMAP PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Need O(1) lookup by key (index, character, value)
 *  - Counting frequencies
 *  - Grouping elements that share a property
 *  - Checking if two things are equal/anagrams
 *
 * Key methods:
 *  map.getOrDefault(key, 0)          — safe get with fallback
 *  map.containsKey(key)              — check membership
 *  map.computeIfAbsent(k, v -> ...) — auto-create value if absent
 *  map.put(key, map.get(key) + 1)    — increment counter
 *
 * Time:  O(1) average for put/get/containsKey
 * Space: O(n) where n = number of unique keys
 */
public class HashMapPatterns {

    public static void main(String[] args) {
        System.out.println("=== Two Sum ===");
        int[] result = twoSumSolved(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(result)); // [0, 1]

        System.out.println("\n=== Valid Anagram ===");
        System.out.println(validAnagramSolved("anagram", "nagaram")); // true
        System.out.println(validAnagramSolved("rat", "car"));         // false

        System.out.println("\n=== Group Anagrams ===");
        String[] words = {"eat","tea","tan","ate","nat","bat"};
        System.out.println(groupAnagramsSolved(words));

        System.out.println("\n=== Top K Frequent ===");
        int[] topK = topKFrequentSolved(new int[]{1,1,1,2,2,3}, 2);
        System.out.println(Arrays.toString(topK)); // [1, 2]
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATES
    // ─────────────────────────────────────────────

    // Template 1 — Frequency count (char)
    static Map<Character, Integer> charFrequency(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1); // increment or start at 0
        }
        return freq;
    }

    // Template 2 — Frequency count (int)
    static Map<Integer, Integer> intFrequency(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) {
            freq.put(n, freq.getOrDefault(n, 0) + 1);
        }
        return freq;
    }

    // Template 3 — Index lookup (value → index)
    static Map<Integer, Integer> buildIndexMap(int[] nums) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            indexMap.put(nums[i], i); // value → position
        }
        return indexMap;
    }

    // Template 4 — Grouping (key → list of values)
    static Map<String, List<String>> groupByKey(String[] words, java.util.function.Function<String, String> keyFn) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : words) {
            String key = keyFn.apply(w);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(w);
        }
        return groups;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: TWO SUM
    // ─────────────────────────────────────────────
    // LeetCode 1 — Easy
    // Given int[] nums and a target, return indices of two numbers that add to target.
    // Assume exactly one solution exists.
    //
    // Example: nums=[2,7,11,15], target=9 → [0,1]
    //
    // Approach:
    //  - For each num, check if (target - num) is already in the map
    //  - If yes → found the pair → return indices
    //  - If no  → store num → index in map and continue
    //
    // Time: O(n)  Space: O(n)

    // TODO VERSION
    static int[] twoSumTODO(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value → index

        for (int i = 0; i < nums.length; i++) {
            // TODO: compute what complement is needed
            // TODO: check if complement exists in seen map
            // TODO: if yes, return [seen.get(complement), i]
            // TODO: otherwise, store nums[i] → i in map
        }

        return new int[]{}; // no solution found
    }

    // SOLVED VERSION
    static int[] twoSumSolved(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // num → index

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];       // what we need to find

            if (seen.containsKey(complement)) {      // already seen the complement?
                return new int[]{seen.get(complement), i}; // return both indices
            }

            seen.put(nums[i], i);                    // record current number and index
        }

        return new int[]{}; // guaranteed not reached per problem constraints
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: VALID ANAGRAM
    // ─────────────────────────────────────────────
    // LeetCode 242 — Easy
    // Given two strings s and t, return true if t is an anagram of s.
    //
    // Example: s="anagram", t="nagaram" → true
    //          s="rat",     t="car"     → false
    //
    // Approach A (map):
    //  - Count frequency of each char in s
    //  - Decrement frequency for each char in t
    //  - If any freq goes below 0 → false
    //  - If all end at 0 → true
    //
    // Approach B (array): Use int[26] for lowercase letters only (faster)
    //
    // Time: O(n)  Space: O(1) — only 26 letters

    // TODO VERSION
    static boolean validAnagramTODO(String s, String t) {
        if (s.length() != t.length()) return false;

        // TODO: Option A — use HashMap<Character, Integer>
        // OR
        // TODO: Option B — use int[26] array (c - 'a' gives index 0–25)

        return true;
    }

    // SOLVED VERSION (using int[26] — faster, cleaner)
    static boolean validAnagramSolved(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] count = new int[26]; // index 0='a', index 1='b', ..., index 25='z'

        for (char c : s.toCharArray()) count[c - 'a']++;  // increment for s
        for (char c : t.toCharArray()) count[c - 'a']--;  // decrement for t

        for (int n : count) {
            if (n != 0) return false; // any mismatch means not an anagram
        }

        return true;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: GROUP ANAGRAMS
    // ─────────────────────────────────────────────
    // LeetCode 49 — Medium
    // Given a list of strings, group anagrams together.
    //
    // Example: ["eat","tea","tan","ate","nat","bat"]
    //       → [["bat"],["nat","tan"],["ate","eat","tea"]]
    //
    // Approach:
    //  - Sort each word → use sorted string as the key
    //  - Group words with the same sorted key together
    //
    // Key pattern: Map<String, List<String>> with computeIfAbsent
    //
    // Time: O(n * k log k)  where k = max word length
    // Space: O(n * k)

    // TODO VERSION
    static List<List<String>> groupAnagramsTODO(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            // TODO: sort the characters of s to get a canonical key
            // String key = ???;

            // TODO: add s to the list under that key
            // map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        // TODO: return all groups
        return new ArrayList<>();
    }

    // SOLVED VERSION
    static List<List<String>> groupAnagramsSolved(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] chars = s.toCharArray();     // get chars
            Arrays.sort(chars);                 // sort them → anagrams become equal
            String key = new String(chars);     // "eat" → "aet", "tea" → "aet"

            // If key not in map, create a new list; then add current word
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values()); // return all grouped lists
    }

    // ─────────────────────────────────────────────
    // PROBLEM 4: TOP K FREQUENT ELEMENTS
    // ─────────────────────────────────────────────
    // LeetCode 347 — Medium
    // Given int[] nums and k, return the k most frequent elements.
    //
    // Example: nums=[1,1,1,2,2,3], k=2 → [1,2]
    //
    // Approach (using sort):
    //  1. Count frequencies with HashMap
    //  2. Extract unique keys into a list
    //  3. Sort by frequency descending
    //  4. Return first k
    //
    // Approach (using min-heap — see HeapPatterns.java for that version)
    //
    // Time: O(n log n)  Space: O(n)

    // TODO VERSION
    static int[] topKFrequentTODO(int[] nums, int k) {
        // TODO: Build frequency map
        Map<Integer, Integer> freq = new HashMap<>();

        // TODO: Sort the keys by their frequency descending

        // TODO: Return first k keys as int[]
        return new int[]{};
    }

    // SOLVED VERSION
    static int[] topKFrequentSolved(int[] nums, int k) {
        // Step 1: Count frequency of each number
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) {
            freq.put(n, freq.getOrDefault(n, 0) + 1);
        }

        // Step 2: Get all unique numbers and sort by frequency descending
        List<Integer> keys = new ArrayList<>(freq.keySet());
        keys.sort((a, b) -> Integer.compare(freq.get(b), freq.get(a))); // highest freq first

        // Step 3: Take the first k elements
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = keys.get(i);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH HASHMAPS
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  map.get(key) + 1   when key might not exist → NullPointerException!
     * ✅ RIGHT:  map.getOrDefault(key, 0) + 1
     *
     * ❌ WRONG:  map.get(key)        for Integer unboxing → NPE if key absent
     * ✅ RIGHT:  map.containsKey(key) check first, or use getOrDefault
     *
     * ❌ WRONG:  iterating map and modifying it simultaneously → ConcurrentModificationException
     * ✅ RIGHT:  collect keys into a list first, then iterate the list
     *
     * ❌ WRONG:  using int[] as map key → uses reference equality, not value
     * ✅ RIGHT:  use String key (e.g., Arrays.toString(arr)) or Map<List<Integer>, ...>
     *
     * ✅ PATTERN: computeIfAbsent is cleaner than checking + putting manually:
     *    map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
     */
}
