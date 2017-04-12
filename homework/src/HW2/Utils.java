package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by lizichen1 on 3/3/17.
 */
public class Utils {

    //process states
    public static final String UNSTARTED = "unstarted";
    public static final String READY = "ready";
    public static final String RUNNING = "running";
    public static final String BLOCKED = "blocked";
    public static final String TERMINATED = "ended";
    public static final String UNKNOWN_STATE = "UNKNOWN STATUS!";

    public static final String FIND_BURST_WHEN_CHOOSING_NEXT_PROCESS_TO_RUN = "Find burst when choosing next process to run: ";
    public static final String NEWLINE_SPACE = "\n        ";
    public static final String THE_ORIGINAL_INPUT_WAS = "The original input was: ";
    public static final String THE_SORTED_INPUT_WAS = "The sorted input was:";

    public static final String RANDOM_NUMBER_FILE = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/input/random-numbers";

    public static final String EMPTY_SPACE = " ";

    public static final int INTEGER_CONSTANT = 9999;
    public static final int LARGE_QUANTUM_INTEGER = 99999;
    public static final String RESET_BURST_TO_ZERO = "0";
    public static final String BEFORE_CYCLE_4S = "Before Cycle \t%5s:";

    public static ArrayList<Process_RR> fromInputFileToListOfProcesses(String input) throws FileNotFoundException {

        File inFile = new File(input);
        Scanner scanner = new Scanner(inFile);
        String data = scanner.useDelimiter("\\Z").next();
        scanner.close();
        data = data.replaceAll("[()]", "");
        String[] tokens = data.split("\\s+");

        ArrayList<Process_RR> allprocess = new ArrayList<>();

        for(int i=1;i<tokens.length;i+=4){
            System.out.printf("%d, %d, %d, %d \n", Integer.valueOf(tokens[i]), Integer.valueOf(tokens[i+1]), Integer.valueOf(tokens[i+2]), Integer.valueOf(tokens[i+3]));
            Process_RR newProcess = new Process_RR(Integer.valueOf(tokens[i]), Integer.valueOf(tokens[i+1]), Integer.valueOf(tokens[i+2]), Integer.valueOf(tokens[i+3]));
            allprocess.add(newProcess);
        }

        return allprocess;
    }

    public static int getNumberOfProcess(String input) throws FileNotFoundException {
        File inFile = new File(input);
        Scanner scanner = new Scanner(inFile);
        String data = scanner.useDelimiter("\\Z").next();

        data = data.replaceAll("[()]", "");
        String[] tokens = data.split("\\s+");

        scanner.close();
        int numberOfProcesses = Integer.valueOf(tokens[0]);
        return numberOfProcesses;
    }



    public static ArrayList<Process_RR> sort(ArrayList<Process_RR> processes){
        Collections.sort(processes, new ProcessComparator());
        return processes;
    }
}
