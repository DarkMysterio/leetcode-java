package patterns;

import java.util.*;

/**
 * ============================================================
 * JAVA COLLECTIONS CHEAT SHEET — Interview Reference
 * ============================================================
 * Use this file as a quick-reference for the most common data
 * structures and their key methods during a coding interview.
 *
 * Data structures covered:
 *  - int[], char[], String[]
 *  - ArrayList
 *  - HashMap
 *  - HashSet
 *  - ArrayDeque (as Stack and Queue)
 *  - PriorityQueue (min-heap and max-heap)
 *  - StringBuilder
 *  - Sorting (arrays and lists, custom comparators)
 */
public class JavaCollectionsCheatSheet {

    public static void main(String[] args) {
        demoArrays();
        demoArrayList();
        demoHashMap();
        demoHashSet();
        demoArrayDequeAsStack();
        demoArrayDequeAsQueue();
        demoPriorityQueue();
        demoStringBuilder();
        demoSorting();
        demoComparators();
    }

    // ─────────────────────────────────────────────
    // 1. ARRAYS  (int[], char[], String[])
    // ─────────────────────────────────────────────
    static void demoArrays() {
        System.out.println("\n=== ARRAYS ===");

        // Declare and fill
        int[] nums = new int[5];        // default value = 0
        int[] init = {3, 1, 4, 1, 5};  // inline init

        // Access / update
        nums[0] = 10;
        System.out.println("nums[0] = " + nums[0]);

        // Length property (NOT .length())
        System.out.println("Length: " + init.length);
        System.out.println(nums.length);

        // Sort in-place  O(n log n)
        Arrays.sort(init);
        System.out.println("Sorted: " + Arrays.toString(init));

        // Copy entire array
        int[] copy = Arrays.copyOf(init, init.length);

        // Copy a range  [from, to)
        int[] range = Arrays.copyOfRange(init, 1, 4);
        System.out.println("Range [1,4): " + Arrays.toString(range));

        // Fill all positions with a value
        int[] filled = new int[5];
        Arrays.fill(filled, -1);
        System.out.println("Filled: " + Arrays.toString(filled));

        // Convert int[] → List  (need Integer[], not int[])
        Integer[] boxed = {1, 2, 3};
        List<Integer> list = new ArrayList<>(Arrays.asList(boxed));

        // 2D array
        int[][] grid = new int[3][4];   // 3 rows, 4 cols
        grid[0][0] = 7;

        // char array (useful for string manipulation)
        char[] chars = "hello".toCharArray();
        chars[0] = 'H';
        String back = new String(chars); // → "Hello"
        System.out.println("char[] → String: " + back);

        // String[] array
        String[] words = {"banana", "apple", "cherry"};
        Arrays.sort(words);             // lexicographic sort
        System.out.println("Sorted strings: " + Arrays.toString(words));
    }

    // ─────────────────────────────────────────────
    // 2. ARRAYLIST
    // ─────────────────────────────────────────────
    static void demoArrayList() {
        System.out.println("\n=== ARRAYLIST ===");

        List<Integer> list = new ArrayList<>();

        // Add to end  O(1) amortized
        list.add(1);
        list.add(2);
        list.add(3);

        // Add at index  O(n)
        list.add(1, 99);  // [1, 99, 2, 3]

        // Get by index  O(1)
        System.out.println("list.get(1) = " + list.get(1));

        // Set (update) by index  O(1)
        list.set(1, 42);

        // Remove by index  O(n)
        list.remove(0);

        // Remove by value — pass Integer object, not int!
        list.remove(Integer.valueOf(42));

        // Size
        System.out.println("Size: " + list.size());

        // Contains  O(n)
        System.out.println("Contains 2: " + list.contains(2));

        // Sort  O(n log n)
        Collections.sort(list);

        // Convert to array
        Integer[] arr = list.toArray(new Integer[0]);

        // Nested list (adjacency list style)
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < 5; i++) adj.add(new ArrayList<>());
        adj.get(0).add(1);

        System.out.println("List: " + list);
    }

    // ─────────────────────────────────────────────
    // 3. HASHMAP
    // ─────────────────────────────────────────────
    static void demoHashMap() {
        System.out.println("\n=== HASHMAP ===");

        Map<String, Integer> map = new HashMap<>();

        // Put / Get  O(1)
        map.put("apple", 3);
        map.put("banana", 5);
        System.out.println("apple: " + map.get("apple"));

        // Returns null if key missing
        System.out.println("missing: " + map.get("xyz"));  // null

        // getOrDefault — avoids null checks
        int count = map.getOrDefault("missing", 0);

        // containsKey / containsValue  O(1) / O(n)
        System.out.println("containsKey banana: " + map.containsKey("banana"));

        // putIfAbsent — only puts if key not present
        map.putIfAbsent("apple", 99); // won't overwrite existing

        // computeIfAbsent — great for grouping/list values
        Map<String, List<String>> groups = new HashMap<>();
        groups.computeIfAbsent("fruits", k -> new ArrayList<>()).add("apple");
        groups.computeIfAbsent("fruits", k -> new ArrayList<>()).add("banana");
        System.out.println("groups: " + groups);

        // Increment frequency (pattern used constantly)
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : "hello".toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        System.out.println("freq: " + freq);

        // Iterate entries
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }

        // Iterate keys only
        for (String key : map.keySet()) { /* ... */ }

        // Iterate values only
        for (int val : map.values()) { /* ... */ }

        // Remove a key
        map.remove("banana");

        // Size
        System.out.println("size: " + map.size());
    }

    // ─────────────────────────────────────────────
    // 4. HASHSET
    // ─────────────────────────────────────────────
    static void demoHashSet() {
        System.out.println("\n=== HASHSET ===");

        Set<Integer> set = new HashSet<>();

        // Add  O(1)
        set.add(1);
        set.add(2);
        set.add(1);  // duplicate — ignored

        // Contains  O(1)  ← this is the superpower vs List
        System.out.println("contains 1: " + set.contains(1));

        // Remove  O(1)
        set.remove(2);

        // Size
        System.out.println("size: " + set.size());  // 1

        // Initialize from array quickly
        Set<Integer> fromArray = new HashSet<>(Arrays.asList(1, 2, 3, 4));

        // Iterate
        for (int n : set) System.out.println(n);

        // LinkedHashSet — preserves insertion order
        Set<String> ordered = new LinkedHashSet<>();
        ordered.add("z"); ordered.add("a");
        System.out.println("ordered: " + ordered);  // [z, a]

        // TreeSet — sorted order, O(log n) ops
        Set<Integer> sorted = new TreeSet<>(fromArray);
        System.out.println("sorted: " + sorted);  // [1, 2, 3, 4]
    }

    // ─────────────────────────────────────────────
    // 5. ARRAYDEQUE — AS STACK (LIFO)
    // ─────────────────────────────────────────────
    static void demoArrayDequeAsStack() {
        System.out.println("\n=== ARRAYDEQUE AS STACK ===");
        // Prefer ArrayDeque over Stack class — faster, no synchronization

        Deque<Integer> stack = new ArrayDeque<>();

        // Push to top  O(1)
        stack.push(1);  // same as addFirst
        stack.push(2);
        stack.push(3);

        // Peek top without removing  O(1)
        System.out.println("peek: " + stack.peek()); // 3

        // Pop from top  O(1)
        System.out.println("pop: " + stack.pop()); // 3

        // Is empty
        System.out.println("empty: " + stack.isEmpty()); // false

        // Size
        System.out.println("size: " + stack.size()); // 2

        // Iterate top → bottom  (normal iterator order = insertion-reversed)
        System.out.print("Stack top→bottom: ");
        for (int n : stack) System.out.print(n + " ");
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // 6. ARRAYDEQUE — AS QUEUE (FIFO)
    // ─────────────────────────────────────────────
    static void demoArrayDequeAsQueue() {
        System.out.println("\n=== ARRAYDEQUE AS QUEUE ===");

        Deque<Integer> queue = new ArrayDeque<>();

        // Enqueue to back  O(1)
        queue.offer(1);  // same as addLast
        queue.offer(2);
        queue.offer(3);

        // Peek front without removing  O(1)
        System.out.println("peek: " + queue.peek()); // 1

        // Dequeue from front  O(1)
        System.out.println("poll: " + queue.poll()); // 1

        // Is empty
        System.out.println("empty: " + queue.isEmpty()); // false
        System.out.println("remaining: " + queue);       // [2, 3]
    }

    // ─────────────────────────────────────────────
    // 7. PRIORITYQUEUE (MIN-HEAP & MAX-HEAP)
    // ─────────────────────────────────────────────
    static void demoPriorityQueue() {
        System.out.println("\n=== PRIORITYQUEUE ===");

        // MIN-HEAP (default) — smallest element at top
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(1);
        minHeap.offer(3);
        System.out.println("minHeap peek: " + minHeap.peek()); // 1
        System.out.println("minHeap poll: " + minHeap.poll()); // 1

        // MAX-HEAP — largest element at top
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(1);
        maxHeap.offer(3);
        System.out.println("maxHeap peek: " + maxHeap.peek()); // 5
        System.out.println("maxHeap poll: " + maxHeap.poll()); // 5

        // Heap of int[] (sort by first element)
        PriorityQueue<int[]> heapOfPairs = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        heapOfPairs.offer(new int[]{3, 10});
        heapOfPairs.offer(new int[]{1, 20});
        heapOfPairs.offer(new int[]{2, 30});
        int[] top = heapOfPairs.poll();
        System.out.println("pair top: [" + top[0] + ", " + top[1] + "]"); // [1, 20]

        // Key methods:
        //   offer(x)  — add            O(log n)
        //   poll()    — remove min/max  O(log n)
        //   peek()    — view min/max    O(1)
        //   size()    — count           O(1)
        //   isEmpty() — check empty     O(1)
    }

    // ─────────────────────────────────────────────
    // 8. STRINGBUILDER
    // ─────────────────────────────────────────────
    static void demoStringBuilder() {
        System.out.println("\n=== STRINGBUILDER ===");
        // Use when building strings in a loop — O(1) append vs O(n) String concat

        StringBuilder sb = new StringBuilder();

        // Append anything
        sb.append("hello");
        sb.append(" ");
        sb.append(42);
        sb.append('!');

        // Insert at index
        sb.insert(5, ",");  // "hello, 42!"

        // Delete range [start, end)
        sb.delete(5, 6);    // removes ","

        // Reverse the whole thing
        sb.reverse();       // "!24 olleh"

        // Length
        System.out.println("length: " + sb.length());

        // charAt
        System.out.println("charAt(0): " + sb.charAt(0));

        // Convert back to String
        String result = sb.toString();
        System.out.println("result: " + result);

        // Pattern: build result string in a loop
        StringBuilder csv = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i > 0) csv.append(",");
            csv.append(i);
        }
        System.out.println("csv: " + csv); // 0,1,2,3,4
    }

    // ─────────────────────────────────────────────
    // 9. SORTING
    // ─────────────────────────────────────────────
    static void demoSorting() {
        System.out.println("\n=== SORTING ===");

        // Sort int[]  — Arrays.sort  O(n log n)
        int[] nums = {3, 1, 4, 1, 5};
        Arrays.sort(nums);
        System.out.println("int[] sorted: " + Arrays.toString(nums));

        // Sort String[]
        String[] words = {"banana", "apple", "cherry"};
        Arrays.sort(words);
        System.out.println("String[] sorted: " + Arrays.toString(words));

        // Sort List<Integer>
        List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 8, 1));
        Collections.sort(list);
        System.out.println("List sorted: " + list);

        // Sort int[][] by first column (intervals)
        int[][] intervals = {{5, 10}, {1, 3}, {2, 6}};
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        System.out.println("Intervals sorted: " + Arrays.deepToString(intervals));

        // Sort List<int[]> by first element
        List<int[]> pairs = new ArrayList<>();
        pairs.add(new int[]{3, 0});
        pairs.add(new int[]{1, 2});
        pairs.add(new int[]{2, 1});
        pairs.sort((a, b) -> Integer.compare(a[0], b[0]));
    }

    // ─────────────────────────────────────────────
    // 10. CUSTOM COMPARATORS
    // ─────────────────────────────────────────────
    static void demoComparators() {
        System.out.println("\n=== CUSTOM COMPARATORS ===");

        // ✅ ALWAYS use Integer.compare — NEVER a-b (can overflow!)
        Comparator<Integer> asc  = (a, b) -> Integer.compare(a, b);
        Comparator<Integer> desc = (a, b) -> Integer.compare(b, a);

        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 2, 8, 1));
        nums.sort(asc);
        System.out.println("asc: " + nums);
        nums.sort(desc);
        System.out.println("desc: " + nums);

        // Sort strings by length, then lexicographically
        List<String> words = new ArrayList<>(Arrays.asList("banana", "fig", "apple", "kiwi"));
        words.sort((a, b) -> {
            if (a.length() != b.length()) return Integer.compare(a.length(), b.length());
            return a.compareTo(b);  // natural string comparison
        });
        System.out.println("by length: " + words);

        // Sort 2D array: primary by row[0] asc, secondary by row[1] desc
        int[][] data = {{1, 5}, {1, 3}, {2, 4}};
        Arrays.sort(data, (a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(b[1], a[1]);
        });
        System.out.println("2D sorted: " + Arrays.deepToString(data));

        // Sort by frequency (useful in Top K Frequent)
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(1, 3); freq.put(2, 1); freq.put(3, 2);
        List<Integer> keys = new ArrayList<>(freq.keySet());
        keys.sort((a, b) -> Integer.compare(freq.get(b), freq.get(a))); // desc freq
        System.out.println("by freq desc: " + keys); // [1, 3, 2]
    }
}
