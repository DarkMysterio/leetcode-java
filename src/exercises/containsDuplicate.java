package exercises;

import java.util.HashSet;
import java.util.Set;

public class containsDuplicate {

    public static boolean hasDuplicate(int[] nums){
        /*
        Time Complexity: O(n)
        We iterate through the array of 'n' elements at most once. Inside the loop,
        HashSet operations like contains() and add() execute in O(1) average time,
        making the overall time linear relative to the input size.

        Space Complexity: O(n)
        In the worst-case scenario (where all elements in the array are unique),
        the HashSet will store all 'n' elements, requiring linear auxiliary memory.
        */
        Set<Integer> elements = new HashSet<>();
        for(int elems : nums){
            if(elements.contains(elems)){
                return true;
            }
            elements.add(elems);
        }

        return false;
    }

}
