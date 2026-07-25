package exercises;

import java.util.ArrayList;
import java.util.List;

public class CourseSchedule2 {
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        List<Integer>[] listCou = new List[numCourses];
        for(int[] arr : prerequisites){
            if(listCou[arr[1]] == null){
                listCou[arr[1]] = new ArrayList<>();
            }
            listCou[arr[1]].add(arr[0]);
        }
        int freeClass = 0;
        for(int i = 0; i < numCourses;i++){
            if(listCou[i] == null){
                freeClass = i;
            }
        }
        System.out.println(freeClass);
        return new int[2];
    }

    public static void main(String[] args) {
        int[][] v = new int[][]{{1,0},{2,0},{3,1},{3,2}};
        findOrder(4,v);
    }

}
