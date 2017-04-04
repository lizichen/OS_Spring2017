package HW2;

import java.io.IOException;
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

    int numberOfProcesses = 0;

    public SJF(ArrayList<Process_RR> processes, int quantum, boolean verbose, int numberOfProcesses) throws IOException {
        super(verbose, processes);
        this.quantum = quantum;
        this.numberOfProcesses = numberOfProcesses;
        for (Process_RR p : processes) {
            p.quantumLimit = 1000;
        }
    }

    protected void updateReadyQueue() {
        Process_RR p = new Process_RR(0,0,0,0);
        p.timeRemaining = Utils.INTEGER_CONSTANT;
        int pause = 1;

        Iterator iter = ready.iterator();
        while(iter.hasNext()){
            Process_RR o = (Process_RR) iter.next();
            if(!terminatedProcess.contains(o)){
                if (o.CPU_Burst_TimeRemaining > 0) {
                    return;
                }
                if (o.timeRemaining < p.timeRemaining) {
                    p = o;
                    pause = 0;
                }
            }
        }

        if (pause == 1)
            return;

        addFirstReadyQueue(p);
    }

    public static void main(String[] args) {

        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";
        int quantum = 2;
        boolean verbose = true;

        // java RR --verbose input-6.txt
        // java RR input-6.txt
        if(args.length == 2){
            if(args[0].equals("--verbose")){
                verbose = true;
                input = args[1];
            }
            else{
                verbose = false;
                System.out.println("Please type\n java RR --verbose input.txt or java RR input.txt");
                System.exit(-1);
            }
        }else if(args.length == 1){
            input = args[0];
            verbose = false;
        }

        try {
            ArrayList<Process_RR> allprocess = Utils.fromInputFileToListOfProcesses(input);
            int numberOfProcesses = Utils.getNumberOfProcess(input);

            SJF sjf = new SJF(allprocess, quantum, verbose, numberOfProcesses);

            System.out.println(Utils.THE_ORIGINAL_INPUT_WAS + sjf.originalProcessesDetailString);
            System.out.println(Utils.THE_SORTED_INPUT_WAS + " " + sjf.sortedProcesses);
            System.out.println();

            while (sjf.terminatedProcess.size() < sjf.numProcesses) {
                sjf.cycle();
            }
            System.out.println();
            sjf.printFinalSummary();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
