package HW3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class HW3 {

    public static String inputFilePath = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW3/input-data/";
    //switch
    static boolean run_fifo   = false;
    static boolean run_banker = true;

    public static void main(String[] args) {

        String inputFileName = "input-13.txt";
        String input = inputFilePath + inputFileName;
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
        if(run_fifo == true){
            FIFO fifo = new FIFO(t.taskHashtable, numberOfTasks, typesOfReousces, numberOfResourceUnit);
            fifo.start();

            ArrayList<Task> fifo_resultsToPrint = fifo.getResultTaskList();
            Utils.printResult(fifo_resultsToPrint, inputFileName, "FIFO");
        }

        if(run_banker == true){
            Banker banker = new Banker(t.taskHashtable, numberOfTasks, typesOfReousces, numberOfResourceUnit);
            banker.start();

            ArrayList<Task> banker_resultsToPrint = banker.getResultTaskList();
            Utils.printResult(banker_resultsToPrint, inputFileName, "Banker");
        }
    }
}
