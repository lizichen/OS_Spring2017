package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by lizichen1 on 3/8/17.
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
        scanner.close();
        data = data.replaceAll("[()]", "");
        String[] tokens = data.split("\\s+");

        int numberOfProcesses = Integer.valueOf(tokens[0]);
        return numberOfProcesses;
    }

    public static String getProcessInformation(int numProcesses, ArrayList<Process_RR> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(numProcesses + EMPTY_SPACE);
        for (int i = 0; i < list.size(); i++) {
            Process_RR oneprocess = list.get(i);
            stringBuilder.append("(");
            stringBuilder.append(oneprocess.A + EMPTY_SPACE); stringBuilder.append(oneprocess.B + EMPTY_SPACE); stringBuilder.append(oneprocess.C + EMPTY_SPACE);
            stringBuilder.append(oneprocess.M + ")" + EMPTY_SPACE);
        }
        return stringBuilder.toString();
    }

    public static ArrayList<Process_RR> sort(ArrayList<Process_RR> processes){
        Collections.sort(processes, new ProcessComparator());
        return processes;
    }
}
