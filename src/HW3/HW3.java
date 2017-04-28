package HW3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class HW3 {

//    public static String inputFilePath = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW3/input-data/";

    /**
     * Read in the input file and start FIFO then Banker's
     */
    public static void run(String inputFileName){
//        String input = inputFilePath + inputFileName;

        TaskFileReader t = new TaskFileReader(inputFileName);
        t.readInput();

        TaskFileReader t2 = new TaskFileReader(inputFileName);
        t2.readInput();

        int numberOfTasks = t.numberOfTasks;
        int typesOfReousces = t.typesOfResources;
        int[] numberOfResourceUnit = t.numberOfResourceUnit;

        int numberOfTasks2 = t2.numberOfTasks;
        int typesOfReousces2 = t2.typesOfResources;
        int[] numberOfResourceUnit2 = t2.numberOfResourceUnit;

        Utils.log("NumberOfTasks:"+numberOfTasks+" TypesOfResources:"+typesOfReousces+" NumberOfResourceUnits:"+ Arrays.toString(numberOfResourceUnit)+"\n");

        ParentClassForBankerAndFIFO fifo = new FIFO(t.taskHashtable, numberOfTasks, typesOfReousces, numberOfResourceUnit);
        ParentClassForBankerAndFIFO banker = new Banker(t2.taskHashtable, numberOfTasks2, typesOfReousces2, numberOfResourceUnit2);

        fifo.start();
        banker.start();

        ArrayList<Task> fifo_resultsToPrint = fifo.getResultTaskList();
        ArrayList<Task> banker_resultsToPrint = banker.getResultTaskList();

        Utils.printResult(fifo_resultsToPrint, banker_resultsToPrint, inputFileName);
    }

    /**
     * This is where magic happens...
     */
    public static void main(String[] args){

//      run("input-01.txt");

        String input;

        if(args.length == 1){
            input = args[0];
            run(input);
        }else{
            System.out.println("Please type command like this: java HW3 input-01.txt");
        }

    }
}
