package HW3;

import java.util.Arrays;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class HW3 {

    public static void main(String[] args) {

        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW3/input-data/input-12.txt";
        TaskFileReader t = new TaskFileReader(input);
        t.readInput();

        int numberOfTasks = t.numberOfTasks;
        int typesOfReousces = t.typesOfResources;
        int[] numberOfResourceUnit = t.numberOfResourceUnit;

        System.out.println("NumberOfTasks:"+numberOfTasks+" TypesOfResources:"+typesOfReousces+" NumberOfResourceUnits:"+ Arrays.toString(numberOfResourceUnit)+"\n");
        for (Integer key:t.taskHashtable.keySet()) {
            Task task = t.taskHashtable.get(key);
            System.out.println(task.toString());
        }

        //FIFO - start from 1 to N
        FIFO.start(t.taskHashtable);



        //Banker

    }
}
