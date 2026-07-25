package exercises;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> visited = new HashMap<>();
        for(int i=0; i < nums.length; i++){
            int newTarget = target - nums[i];
            if(visited.containsKey(newTarget)){
                return new int[]{visited.get(newTarget),i};
            }
            visited.put(nums[i],i);
        }
        return new int[]{};
    }
}
