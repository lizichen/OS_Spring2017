package HW3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class HW3 {

    public static void main(String[] args) {

        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW3/input-data/input-13.txt";
        TaskFileReader t = new TaskFileReader(input);
        t.readInput();

        int numberOfTasks = t.numberOfTasks;
        int typesOfReousces = t.typesOfResources;
        int[] numberOfResourceUnit = t.numberOfResourceUnit;

        Utils.log("NumberOfTasks:"+numberOfTasks+" TypesOfResources:"+typesOfReousces+" NumberOfResourceUnits:"+ Arrays.toString(numberOfResourceUnit)+"\n");
        for (Integer key:t.taskHashtable.keySet()) {
            Task task = t.taskHashtable.get(key);
            Utils.log(task.toString());
        }

        //FIFO - start from 1 to N
        FIFO fifo = new FIFO(t.taskHashtable, numberOfTasks, typesOfReousces, numberOfResourceUnit);
        fifo.start();
        ArrayList<Task> resultsToPrint = fifo.getResultTaskList();
        Utils.printResult(resultsToPrint);

        Utils.log("Completes!");

        //Banker
    }
}
