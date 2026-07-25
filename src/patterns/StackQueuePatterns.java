package patterns;

import java.util.*;

/**
 * ============================================================
 * STACK & QUEUE PATTERNS — Interview Reference
 * ============================================================
 * When to use STACK:
 *  - Need to process elements in reverse order (LIFO)
 *  - Matching open/close brackets, tags
 *  - Tracking "previous larger/smaller" element → monotonic stack
 *  - Undo operations, backtracking
 *
 * When to use QUEUE:
 *  - BFS (level-order traversal)
 *  - Process elements in order they arrived (FIFO)
 *
 * ⚠️ Use ArrayDeque, NOT Stack class and NOT LinkedList for performance!
 *
 * Key methods (ArrayDeque as Stack):
 *  push(x)  = addFirst(x)   → add to top
 *  pop()    = removeFirst()  → remove from top
 *  peek()   = peekFirst()    → view top without removing
 *
 * Key methods (ArrayDeque as Queue):
 *  offer(x) = addLast(x)    → enqueue to back
 *  poll()   = removeFirst()  → dequeue from front
 *  peek()   = peekFirst()    → view front without removing
 *
 * Time: O(1) for push/pop/peek/offer/poll
 */
public class StackQueuePatterns {

    public static void main(String[] args) {
        System.out.println("=== Valid Parentheses ===");
        System.out.println(isValidSolved("()[]{}"));   // true
        System.out.println(isValidSolved("(]"));        // false
        System.out.println(isValidSolved("{[]}"));      // true

        System.out.println("\n=== Min Stack ===");
        MinStack ms = new MinStack();
        ms.push(-2); ms.push(0); ms.push(-3);
        System.out.println("getMin: " + ms.getMin()); // -3
        ms.pop();
        System.out.println("top: " + ms.top());       // 0
        System.out.println("getMin: " + ms.getMin()); // -2

        System.out.println("\n=== Evaluate RPN ===");
        System.out.println(evalRPNSolved(new String[]{"2","1","+","3","*"})); // 9
        System.out.println(evalRPNSolved(new String[]{"4","13","5","/","+"})); // 6

        System.out.println("\n=== Daily Temperatures ===");
        int[] temps = dailyTemperaturesSolved(new int[]{73,74,75,71,69,72,76,73});
        System.out.println(Arrays.toString(temps)); // [1,1,4,2,1,1,0,0]
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Monotonic Stack
    // ─────────────────────────────────────────────
    // Use when: "find next greater/smaller element" type problems
    // Maintain stack in decreasing (or increasing) order of values
    //
    // Pattern: Next Greater Element
    static int[] nextGreaterTemplate(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1); // default: no greater element

        Deque<Integer> stack = new ArrayDeque<>(); // stores INDICES

        for (int i = 0; i < n; i++) {
            // While stack not empty AND current element > element at stack top
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                int idx = stack.pop();      // pop index of smaller element
                result[idx] = nums[i];      // current is the "next greater" for that index
            }
            stack.push(i); // push current index onto stack
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: VALID PARENTHESES
    // ─────────────────────────────────────────────
    // LeetCode 20 — Easy
    // Given a string with '(', ')', '{', '}', '[', ']', determine if it's valid.
    // Valid means: open brackets closed in correct order.
    //
    // Example: "()" → true,  "()[]{}" → true,  "(]" → false,  "{[]}" → true
    //
    // Approach:
    //  - Push opening brackets onto stack
    //  - When closing bracket appears, check if it matches the top of stack
    //  - At end, stack must be empty
    //
    // Time: O(n)  Space: O(n)

    // TODO VERSION
    static boolean isValidTODO(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            // TODO: if c is opening bracket → push to stack
            // TODO: else (closing bracket):
            //   if stack is empty → return false (no matching open)
            //   pop the top and check if it matches c
            //   if not matching → return false
        }

        // TODO: return true only if stack is empty (all matched)
        return true;
    }

    // SOLVED VERSION
    static boolean isValidSolved(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c); // push any opening bracket
            } else {
                // Closing bracket — stack must have a matching opener
                if (stack.isEmpty()) return false;

                char top = stack.pop(); // get the most recent opener

                // Check if the pair matches
                if (c == ')' && top != '(') return false;
                if (c == ']' && top != '[') return false;
                if (c == '}' && top != '{') return false;
            }
        }

        return stack.isEmpty(); // valid only if all openers were matched
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: MIN STACK
    // ─────────────────────────────────────────────
    // LeetCode 155 — Medium
    // Design a stack that supports push, pop, top, and getMin in O(1).
    //
    // Approach:
    //  - Maintain TWO stacks: one normal, one tracking minimum so far
    //  - minStack always stores the current minimum at each state
    //
    // Time: O(1) all operations  Space: O(n)

    // TODO VERSION (skeleton only — implement MinStackTODO class below)

    // SOLVED VERSION
    static class MinStack {
        private Deque<Integer> stack;    // main stack
        private Deque<Integer> minStack; // parallel stack tracking minimum

        public MinStack() {
            stack = new ArrayDeque<>();
            minStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val); // push to main stack

            // Push to minStack: store min(val, current min)
            if (minStack.isEmpty()) {
                minStack.push(val);
            } else {
                minStack.push(Math.min(val, minStack.peek())); // maintain running min
            }
        }

        public void pop() {
            stack.pop();    // remove from both stacks in sync
            minStack.pop();
        }

        public int top() {
            return stack.peek(); // top of main stack
        }

        public int getMin() {
            return minStack.peek(); // top of minStack = current minimum
        }
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: EVALUATE REVERSE POLISH NOTATION
    // ─────────────────────────────────────────────
    // LeetCode 150 — Medium
    // Evaluate an expression in Reverse Polish Notation (postfix).
    // Operators: "+", "-", "*", "/"
    //
    // Example: ["2","1","+","3","*"] → (2+1)*3 = 9
    //          ["4","13","5","/","+"] → 4+(13/5) = 6
    //
    // Approach:
    //  - Push numbers onto stack
    //  - When operator appears: pop two numbers, compute, push result
    //  - Final answer is the only element left on stack
    //
    // Time: O(n)  Space: O(n)

    // TODO VERSION
    static int evalRPNTODO(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : tokens) {
            // TODO: if token is a number → Integer.parseInt(token) and push
            // TODO: else it's an operator:
            //   pop b (top), then pop a (second)
            //   compute a OP b (note: order matters for - and /)
            //   push result
        }

        return 0; // TODO: return stack.pop()
    }

    // SOLVED VERSION
    static int evalRPNSolved(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                int b = stack.pop(); // second operand (popped first!)
                int a = stack.pop(); // first operand

                int result;
                switch (token) {
                    case "+" -> result = a + b;
                    case "-" -> result = a - b; // a - b, NOT b - a !
                    case "*" -> result = a * b;
                    default  -> result = a / b; // integer division, truncates toward zero
                }
                stack.push(result);
            } else {
                stack.push(Integer.parseInt(token)); // push number onto stack
            }
        }

        return stack.pop(); // final result
    }

    // ─────────────────────────────────────────────
    // PROBLEM 4: DAILY TEMPERATURES (Monotonic Stack)
    // ─────────────────────────────────────────────
    // LeetCode 739 — Medium
    // For each day, find how many days until a warmer temperature.
    // If no warmer day, answer is 0.
    //
    // Example: [73,74,75,71,69,72,76,73] → [1,1,4,2,1,1,0,0]
    //
    // Approach (monotonic decreasing stack):
    //  - Stack stores INDICES of temperatures we haven't resolved yet
    //  - When current temp > temp at top of stack → we found the "next warmer"
    //  - result[top_index] = current_index - top_index
    //
    // Time: O(n)  Space: O(n)

    // TODO VERSION
    static int[] dailyTemperaturesTODO(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n]; // default 0 (no warmer day)
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            // TODO: while stack not empty AND temperatures[i] > temperatures[stack.peek()]:
            //   pop index from stack
            //   result[poppedIndex] = i - poppedIndex  (days until warmer)

            // TODO: push current index i onto stack
        }

        return result;
    }

    // SOLVED VERSION
    static int[] dailyTemperaturesSolved(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];             // 0 by default
        Deque<Integer> stack = new ArrayDeque<>(); // monotonic stack of indices

        for (int i = 0; i < n; i++) {
            // Current temperature is warmer than what's waiting on the stack
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIdx = stack.pop();              // index of colder day
                result[prevIdx] = i - prevIdx;          // days until warmer = i - prevIdx
            }
            stack.push(i); // push current day's index (still looking for its warmer day)
        }

        return result;
        // Anything left in stack already has result = 0 (no warmer day found)
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH STACK/QUEUE
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  using Stack class (synchronized, slower)
     * ✅ RIGHT:  Deque<Integer> stack = new ArrayDeque<>();
     *
     * ❌ WRONG:  using LinkedList as queue (works, but ArrayDeque is faster)
     * ✅ RIGHT:  Deque<Integer> queue = new ArrayDeque<>();  queue.offer(x); queue.poll();
     *
     * ❌ WRONG:  stack.pop() on empty stack → EmptyStackException / NoSuchElementException
     * ✅ RIGHT:  always check !stack.isEmpty() before pop/peek
     *
     * ❌ WRONG:  in RPN:  a = stack.pop(); b = stack.pop(); result = a - b;
     * ✅ RIGHT:            b = stack.pop(); a = stack.pop(); result = a - b;
     *           (b is popped first because it was pushed last)
     *
     * ❌ WRONG:  monotonic stack storing VALUES (can't compute distances)
     * ✅ RIGHT:  monotonic stack stores INDICES (compute result[idx] = i - idx)
     */
}
