package HW2;

import java.io.IOException;
import java.util.*;

/**

 Each process gets a small unit of CPU time (time quantum), usually 10-100 milliseconds.

 After this time has elapsed, the process is preempted and added to the end of the ready queue.


 If there are n processes in the ready queue and the time quantum is q,
 then each process gets 1/n of the CPU time in chunks of at most q time units at once.
 No process waits more than (n-1)q time units.

 */
public class RR extends RR_Scheduler{

    private static List<String> processList;
    private static int numberOfProcesses;
    private static ArrayList<Process_RR> allProcesses;

    public RR(ArrayList<Process_RR> processes, int quantum, boolean verbose, int numberOfProcesses) throws IOException {
        super(verbose, processes);
        this.quantum = quantum;

        this.numProcesses = numberOfProcesses;
        this.allProcesses = processes;

        for (Process_RR p : processes) {
            p.quantumLimit = 2;
        }
    }

    @Override
    protected void updateReadyQueue() {

        if(ready != null){
            for(Process_RR currentProcess : ready){
                if(!readyQueue.contains(currentProcess)) {
                    readyQueue.addFirst(currentProcess);
                }
            }
        }

        for(Process_RR p : readyQueue){
            if(ready.contains(p)){
                int index = ready.indexOf(p);
                ready.remove(index);
            }
        }
    }

    public static void main(String[] args){

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

            RR rr = new RR(allprocess, quantum, verbose, numberOfProcesses);

            System.out.println(Utils.THE_ORIGINAL_INPUT_WAS + rr.originalProcessesDetailString);
            System.out.println(Utils.THE_SORTED_INPUT_WAS + rr.sortedProcesses);

            System.out.println();

            while (rr.terminatedProcess.size() < rr.numProcesses) {
                rr.cycle();
            }
            System.out.println();
            rr.printFinalSummary();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
