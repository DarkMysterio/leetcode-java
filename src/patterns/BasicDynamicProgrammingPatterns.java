package patterns;

import java.util.*;

/**
 * ============================================================
 * BASIC DYNAMIC PROGRAMMING PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Problem asks for: min/max cost, number of ways, can we achieve X?
 *  - "Overlapping subproblems" — same sub-problem solved multiple times
 *  - "Optimal substructure" — optimal answer built from optimal sub-answers
 *
 * Two approaches:
 *  A) TOP-DOWN (Memoization): Recursion + cache results in a map/array
 *  B) BOTTOM-UP (Tabulation): Build solution iteratively from base cases
 *
 * Interview tip: Start with the recurrence relation (formula):
 *   "What is dp[i] in terms of previous dp values?"
 *
 * Then implement bottom-up from that formula.
 *
 * Common patterns:
 *  1D DP:      dp[i] depends on dp[i-1], dp[i-2], etc.
 *  Take/Skip:  at each step, choose to take or skip (max/min)
 *  Coin Change: try all options at each step, take the best
 *
 * Time: O(n) or O(n*m) typically
 * Space: O(n) for array, O(1) if we only keep last few values
 */
public class BasicDynamicProgrammingPatterns {

    public static void main(String[] args) {
        System.out.println("=== Climbing Stairs ===");
        System.out.println(climbStairsSolved(2)); // 2
        System.out.println(climbStairsSolved(3)); // 3
        System.out.println(climbStairsSolved(5)); // 8

        System.out.println("\n=== House Robber ===");
        System.out.println(robSolved(new int[]{1,2,3,1})); // 4
        System.out.println(robSolved(new int[]{2,7,9,3,1})); // 12

        System.out.println("\n=== Coin Change ===");
        System.out.println(coinChangeSolved(new int[]{1,5,11}, 15)); // 3  (11+3*1 wait, 5+5+5)
        System.out.println(coinChangeSolved(new int[]{1,5,11}, 15)); // Actually: 11+... let me check
        // coins=[1,5,11], amount=15: 11+... no. 5+5+5=15 → 3 coins
        System.out.println(coinChangeSolved(new int[]{2}, 3));       // -1 (impossible)
        System.out.println(coinChangeSolved(new int[]{1,2,5}, 11));  // 3 (5+5+1)
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — 1D DP (bottom-up)
    // ─────────────────────────────────────────────
    static int oneDimDPTemplate(int n) {
        if (n <= 0) return 0;

        int[] dp = new int[n + 1]; // dp[i] = answer for input i

        // Base cases
        dp[0] = 1; // example: one way to reach step 0
        dp[1] = 1; // example: one way to reach step 1

        // Fill using recurrence relation
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2]; // example: Fibonacci-like
        }

        return dp[n];
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Take/Skip Pattern
    // ─────────────────────────────────────────────
    static int takeSkipTemplate(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];

        int[] dp = new int[n];
        dp[0] = nums[0];                         // base: only first house
        dp[1] = Math.max(nums[0], nums[1]);      // base: best of first two

        for (int i = 2; i < n; i++) {
            // Option A: skip nums[i] → best up to i-1
            // Option B: take nums[i] → add to best up to i-2 (can't use i-1 with i)
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[n - 1];
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE — Space-optimized (only 2 vars)
    // ─────────────────────────────────────────────
    static int spaceOptimizedTemplate(int n) {
        if (n <= 0) return 0;

        int prev2 = 1; // dp[i-2]
        int prev1 = 1; // dp[i-1]

        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2; // dp[i]
            prev2 = prev1;            // slide window
            prev1 = curr;
        }

        return prev1;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: CLIMBING STAIRS
    // ─────────────────────────────────────────────
    // LeetCode 70 — Easy
    // Climb n stairs, 1 or 2 steps at a time. How many distinct ways?
    //
    // Example: n=2 → 2  (1+1, 2)
    //          n=3 → 3  (1+1+1, 1+2, 2+1)
    //
    // Recurrence: dp[i] = dp[i-1] + dp[i-2]
    //   (reach step i from step i-1 with 1 step, or from step i-2 with 2 steps)
    //
    // Base cases: dp[1] = 1, dp[2] = 2
    //
    // Pattern: Fibonacci! Can space-optimize to O(1)
    //
    // Time: O(n)  Space: O(1) with optimization

    // TODO VERSION
    static int climbStairsTODO(int n) {
        // TODO: handle base cases n==1 and n==2
        // TODO: initialize prev2=1 (dp[1]), prev1=2 (dp[2])
        // TODO: for i from 3 to n:
        //   curr = prev1 + prev2
        //   prev2 = prev1
        //   prev1 = curr
        // TODO: return prev1
        return 0;
    }

    // SOLVED VERSION (space-optimized)
    static int climbStairsSolved(int n) {
        if (n == 1) return 1; // only one way: {1}
        if (n == 2) return 2; // two ways: {1,1} and {2}

        int prev2 = 1; // dp[i-2]: ways to reach 2 steps ago
        int prev1 = 2; // dp[i-1]: ways to reach 1 step ago

        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2; // ways to reach step i = from i-1 + from i-2
            prev2 = prev1;            // shift: i-2 becomes i-1
            prev1 = curr;             // shift: i-1 becomes i
        }

        return prev1; // ways to reach step n
    }

    // ALTERNATIVE: Full dp array version (easier to understand during interview)
    static int climbStairsWithArray(int n) {
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2]; // from step i-1 or step i-2
        }
        return dp[n];
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: HOUSE ROBBER
    // ─────────────────────────────────────────────
    // LeetCode 198 — Medium
    // Rob houses in a row; can't rob adjacent houses. Max money?
    //
    // Example: [1,2,3,1] → 4  (rob house 0 + house 2 = 1+3)
    //          [2,7,9,3,1] → 12  (rob house 0+2+4 = 2+9+1=12)
    //
    // Recurrence: dp[i] = max(dp[i-1], dp[i-2] + nums[i])
    //   "skip house i" → take dp[i-1]
    //   "rob house i"  → can't use i-1, so take dp[i-2] + nums[i]
    //
    // Base cases: dp[0] = nums[0], dp[1] = max(nums[0], nums[1])
    //
    // Time: O(n)  Space: O(1) with optimization

    // TODO VERSION
    static int robTODO(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        // TODO: initialize prev2 = nums[0], prev1 = max(nums[0], nums[1])
        // TODO: for i from 2 to n-1:
        //   curr = max(prev1, prev2 + nums[i])
        //   prev2 = prev1
        //   prev1 = curr
        // TODO: return prev1
        return 0;
    }

    // SOLVED VERSION (space-optimized)
    static int robSolved(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0]; // only one house

        int prev2 = nums[0];                  // dp[0]: max if only first house
        int prev1 = Math.max(nums[0], nums[1]); // dp[1]: max of first two houses

        for (int i = 2; i < n; i++) {
            // Option A: skip house i → best we could do up to house i-1
            // Option B: rob house i → best up to house i-2, plus nums[i]
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1; // shift window
            prev1 = curr;
        }

        return prev1; // max money from all n houses
    }

    // ALTERNATIVE: With dp array for clarity
    static int robWithArray(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1],         // skip house i
                             dp[i - 2] + nums[i]); // rob house i
        }
        return dp[n - 1];
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: COIN CHANGE
    // ─────────────────────────────────────────────
    // LeetCode 322 — Medium
    // Given coin denominations and a target amount, find the minimum number
    // of coins to make that amount. Return -1 if impossible.
    //
    // Example: coins=[1,5,11], amount=15 → 3  (5+5+5)
    //          coins=[2], amount=3       → -1
    //          coins=[1,2,5], amount=11  → 3  (5+5+1)
    //
    // Recurrence: dp[i] = min over each coin c where c <= i:
    //               dp[i] = min(dp[i], dp[i - c] + 1)
    //   "use coin c to reach amount i → need dp[i-c] more coins + 1"
    //
    // Base case: dp[0] = 0 (0 coins needed for amount 0)
    // Initialize: dp[1..amount] = amount+1 (= "infinity" = impossible)
    //
    // Time: O(amount * coins.length)  Space: O(amount)

    // TODO VERSION
    static int coinChangeTODO(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // "infinity" — means not reachable yet
        dp[0] = 0;                   // base case: 0 coins for amount 0

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                // TODO: if coin <= i (we can use this coin):
                //   dp[i] = Math.min(dp[i], dp[i - coin] + 1)
            }
        }

        // TODO: return dp[amount] == amount+1 ? -1 : dp[amount]
        return -1;
    }

    // SOLVED VERSION
    static int coinChangeSolved(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // fill with "infinity" (can't be reached)
        dp[0] = 0;                   // base: 0 coins needed for amount 0

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) { // can we use this coin for amount i?
                    // Use coin: 1 coin + min coins for remaining amount (i - coin)
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        // If dp[amount] is still "infinity", we couldn't make the amount
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH DP
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  int[] dp = new int[n]  when you need indices 0..n
     * ✅ RIGHT:  int[] dp = new int[n + 1]  for problems indexed 0..n
     *
     * ❌ WRONG:  Coin Change: Arrays.fill(dp, Integer.MAX_VALUE) then dp[i-c]+1 → overflow!
     * ✅ RIGHT:  Arrays.fill(dp, amount + 1)  — safe "infinity" (can't exceed amount)
     *
     * ❌ WRONG:  House Robber: dp[1] = nums[1]  (misses the case of skipping house 1)
     * ✅ RIGHT:  dp[1] = Math.max(nums[0], nums[1])  (pick the better of first two)
     *
     * ❌ WRONG:  Climbing Stairs: dp[0] = 0, dp[1] = 1, dp[2] = 1
     * ✅ RIGHT:  dp[1] = 1, dp[2] = 2  (two ways to climb 2 stairs: 1+1 or 2)
     *            → Or just set dp[0]=1, dp[1]=1 and compute dp[2]=dp[1]+dp[0]=2
     *
     * ❌ WRONG:  Trying to use 2D DP when 1D is sufficient
     * ✅ RIGHT:  Coin Change, Climbing Stairs, House Robber are all 1D
     *            Think: "does dp[i] need info from a 2nd dimension?"
     *
     * ✅ STRATEGY for interview:
     *    1. Write the recurrence first (the formula)
     *    2. Identify base cases
     *    3. Decide array size (n or n+1)
     *    4. Fill bottom-up
     *    5. Space-optimize if time permits (replace array with 2 variables)
     */
}
