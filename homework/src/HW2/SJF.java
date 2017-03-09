package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by lizichen1 on 3/8/17.
 *
 * Shortest-Job-First
 *
 *
 Process      Burst Time

 P1             6

 P2             8

 P3             7

 P4             3

 SJF scheduling Gantt chart
 P4,    P1,     P3,     P2
 0 - 3  3 - 9   9 - 16  16 - 24

 Average waiting time = (3 + 16 + 9 + 0) / 4 = 7

 */
public class SJF extends RR_Scheduler{

    public static final int INTEGER_CONSTANT = 10000;
    int numberOfProcesses = 0;

    public SJF(ArrayList<Process_RR> processes, int quantum, boolean verbose, int numberOfProcesses) throws FileNotFoundException {
        super(verbose, processes);
        this.quantum = quantum;
        this.numberOfProcesses = numberOfProcesses;
        for (Process_RR p : processes) {
            p.quantumMax = 1000;
        }
    }

    protected void updateReadyQueue() {
        Process_RR p = new Process_RR(0,0,0,0);

        p.timeRemaining = INTEGER_CONSTANT;

        boolean shouldStop = true;
        for (Process_RR temp : ready) {

            if (terminated.contains(temp))
                continue;
            if (temp.cpuBurstRemaining > 0) {
                return;
            }
            if (temp.timeRemaining < p.timeRemaining) {
                p = temp;
                shouldStop = false;
            }
        }

        if (shouldStop)
            return;

        if (!readyQueue.contains(p) && !blockedList.contains(p) && p.state != 2)
            readyQueue.addFirst(p);

        ListIterator<Process_RR> i = ready.listIterator();
    }

    public static void main(String[] args) {

        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";
        int quantum = 2;
        boolean verbose = true;

        try {
            File inFile = new File(input);
            Scanner scanner = new Scanner(inFile);
            String data = scanner.useDelimiter("\\Z").next();
            scanner.close();
            data = data.replaceAll("[()]", "");
            String[] tokens = data.split("\\s+");

            int numberOfProcesses = Integer.valueOf(tokens[0]);
            ArrayList<Process_RR> allprocess = new ArrayList<>();

            for(int i=1;i<tokens.length;i+=4){
                System.out.printf("%d, %d, %d, %d \n", Integer.valueOf(tokens[i]), Integer.valueOf(tokens[i+1]), Integer.valueOf(tokens[i+2]), Integer.valueOf(tokens[i+3]));
                Process_RR newProcess = new Process_RR(Integer.valueOf(tokens[i]), Integer.valueOf(tokens[i+1]), Integer.valueOf(tokens[i+2]), Integer.valueOf(tokens[i+3]));
                allprocess.add(newProcess);
            }

            SJF sjf = new SJF(allprocess, quantum, verbose, numberOfProcesses);

            System.out.println(Utils.THE_ORIGINAL_INPUT_WAS + sjf.originalProcesses);
            System.out.println(Utils.THE_SORTED_INPUT_WAS + " " + sjf.sortedProcesses);
            System.out.println();

            while (sjf.terminated.size() < sjf.numProcesses) {
                sjf.cycle();
            }
            sjf.printFinalSummary();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
