package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by lizichen1 on 3/8/17.
 */
public class Uniprogrammed extends RR_Scheduler {


    //(allprocess, quantum, verbose, numberOfProcesses);
    public Uniprogrammed( ArrayList<Process_RR> processes, boolean verbose, int numberOfProcesses) throws FileNotFoundException {
        super(verbose, processes);
        this.name = "Uniprogrammed";
    }


    protected void updateReadyQueue() {
        // dPrint();
        // Only allow a process that is not yet terminated into the ready queue
        // Get the first process not yet terminated
        if (numProcesses == terminated.size()) return;

        Process_RR p = new Process_RR(0,0,0,0);
        for (Process_RR temp : processes) {
            if (terminated.contains(temp)) continue;
            else {
                p = temp;
                break;
            }
        }

        // With that process, add to readyQueue if it is not already in it
        if (!readyQueue.contains(p) && !blockedList.contains(p) && p.getState() != 2) readyQueue.addFirst(p);
        // dPrint();
    }

    public static void main(String[] args) {


        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";

        try {
            File inFile = new File(input);
            Scanner scanner = new Scanner(inFile);
            String data = scanner.useDelimiter("\\Z").next();


            scanner.close();

            int quantum = 2;
            boolean verbose = true;

            data = data.replaceAll("[()]", "");


            String[] tokens = data.split("\\s+");

            int numberOfProcesses = Integer.valueOf(tokens[0]);

            ArrayList<Process_RR> allprocess = new ArrayList<>();

            for(int i=1;i<tokens.length;i+=4){
                System.out.printf("%d, %d, %d, %d \n", Integer.valueOf(tokens[i]), Integer.valueOf(tokens[i+1]), Integer.valueOf(tokens[i+2]), Integer.valueOf(tokens[i+3]));
                Process_RR newProcess = new Process_RR(Integer.valueOf(tokens[i]), Integer.valueOf(tokens[i+1]), Integer.valueOf(tokens[i+2]), Integer.valueOf(tokens[i+3]));
                allprocess.add(newProcess);
            }

            Uniprogrammed uni = new Uniprogrammed(allprocess, verbose, numberOfProcesses);


            System.out.println("The original input was: " + uni.getOriginalProcesses());
            System.out.println("The (sorted) input was: " + uni.getSortedProcesses());
            System.out.println();

            while (uni.incomplete()) {
                uni.cycle();
            }
            uni.printFinalSummary();

        }
        catch (Exception e) {
            System.out.println(e);
        }


    }
}
