package exercises.backtracking;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result,new StringBuilder(),0,0,n);
        return result;
    }

    private void backtrack(List<String> result, StringBuilder sb, int open, int close, int n) {
        if (sb.length() == 2 * n) {
            result.add(sb.toString());
            return;
        }

        if (open < n) {
            sb.append('(');                              // 1. Choose
            backtrack(result, sb, open + 1, close, n);   // 2. Explore
            sb.deleteCharAt(sb.length() - 1);           // 3. Un-choose (Backtrack)
        }

        if (close < open) {
            sb.append(')');                              // 1. Choose
            backtrack(result, sb, open, close + 1, n);   // 2. Explore
            sb.deleteCharAt(sb.length() - 1);           // 3. Un-choose (Backtrack)
        }

    }
}
