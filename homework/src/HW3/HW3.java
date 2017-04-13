package HW3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class HW3 {

    public static String inputFilePath = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW3/input-data/";

//    public static void main(String[] args) {
    public static void run(String inputFileName){
//        String inputFileName = "input-13.txt";
        String input = inputFilePath + inputFileName;
        TaskFileReader t = new TaskFileReader(input);
        t.readInput();

        TaskFileReader t2 = new TaskFileReader(input);
        t2.readInput();

        int numberOfTasks = t.numberOfTasks;
        int typesOfReousces = t.typesOfResources;
        int[] numberOfResourceUnit = t.numberOfResourceUnit;

        int numberOfTasks2 = t2.numberOfTasks;
        int typesOfReousces2 = t2.typesOfResources;
        int[] numberOfResourceUnit2 = t2.numberOfResourceUnit;

        Utils.log("NumberOfTasks:"+numberOfTasks+" TypesOfResources:"+typesOfReousces+" NumberOfResourceUnits:"+ Arrays.toString(numberOfResourceUnit)+"\n");

        FIFO fifo = new FIFO(t.taskHashtable, numberOfTasks, typesOfReousces, numberOfResourceUnit);
        Banker banker = new Banker(t2.taskHashtable, numberOfTasks2, typesOfReousces2, numberOfResourceUnit2);

        fifo.start();
        banker.start();

        ArrayList<Task> fifo_resultsToPrint = fifo.getResultTaskList();
        ArrayList<Task> banker_resultsToPrint = banker.getResultTaskList();

        Utils.printResult(fifo_resultsToPrint, banker_resultsToPrint, inputFileName);
    }

    public static void main(String[] args){
        run("input-01.txt");
        run("input-02.txt");
        run("input-03.txt");
        run("input-04.txt");
        run("input-05.txt");
        run("input-06.txt");
        run("input-07.txt");
        run("input-08.txt");
        run("input-09.txt");
        run("input-10.txt");
        run("input-11.txt");
        run("input-12.txt");
        run("input-13.txt");
    }
}
