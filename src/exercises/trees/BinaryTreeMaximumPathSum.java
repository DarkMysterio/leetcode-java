package exercises.trees;

public class BinaryTreeMaximumPathSum {
    int max = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return max;

    }

    public int maxGain(TreeNode root){
        if(root == null){
            return 0;
        }

        int leftSum = Math.max(0,maxGain(root.left));
        int rightSum = Math.max(0,maxGain(root.right));

        int currentSplitSum = root.val + leftSum + rightSum ;
        max = Math.max(max, currentSplitSum);

        return root.val + Math.max(leftSum, rightSum);
    }

}
