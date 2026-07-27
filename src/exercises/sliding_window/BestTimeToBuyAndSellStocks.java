package exercises.sliding_window;

/**
 * ============================================================================
 * PROBLEM: Best Time to Buy and Sell Stock (LeetCode 121)
 * STRATEGY: Sliding Window / Two Pointers
 * ============================================================================
 * TIME COMPLEXITY: O(N)
 *   - The 'sell' pointer traverses the array of size N exactly once.
 *
 * SPACE COMPLEXITY: O(1)
 *   - Auxiliary space is constant; only pointers and max values are stored.
 * ============================================================================
 */
public class BestTimeToBuyAndSellStocks {

    public int maxProfit(int[] prices) {
        // Base edge case check
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int maxProfit = 0;

        // 'buy' (p) tracks the best buy day index in the current window
        int buy = 0;

        // 'sell' (q) scans through all future candidate sell days
        int sell = 1;

        while (sell < prices.length) {
            // Step 1: Calculate current profit/loss if we bought on 'buy' and sold on 'sell'
            int currProfit = prices[sell] - prices[buy];

            // Step 2: Record new global max profit if current transaction is better
            if (currProfit > maxProfit) {
                maxProfit = currProfit;
            }

            // Step 3: Window Reset Logic
            // If currProfit < 0, then prices[sell] < prices[buy].
            // This means 'sell' is a cheaper buying day than 'buy'.
            // We slide our left window pointer forward to this new low point.
            if (currProfit < 0) {
                buy = sell;
            }

            // Step 4: Move to the next potential selling day
            sell += 1;
        }

        return maxProfit;
    }
}