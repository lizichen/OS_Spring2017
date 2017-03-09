package HW2;

import java.io.File;
import java.io.FileNotFoundException;
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
    //protected ArrayList<Process_RR> terminated;

    public RR(ArrayList<Process_RR> processes, int quantum, boolean verbose, int numberOfProcesses) throws FileNotFoundException {
        super(verbose, processes);
        this.quantum = quantum;

        this.numProcesses = numberOfProcesses;
        this.allProcesses = processes;

        for (Process_RR p : processes) {
            p.quantumMax = 2;
        }
    }

    @Override
    protected void updateReadyQueue() {

        ListIterator<Process_RR> i = ready.listIterator();

        Process_RR p;

        while (i.hasNext()) {
            p = i.next();

            if (!readyQueue.contains(p))
                readyQueue.addFirst(p);
        }

        i = readyQueue.listIterator();

        while (i.hasNext()) {
            p = i.next();
            if (ready.contains(p))
                ready.remove(p);
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

            RR rr = new RR(allprocess, quantum, verbose, numberOfProcesses);

            System.out.println(Utils.THE_ORIGINAL_INPUT_WAS + rr.originalProcesses);
            System.out.println(Utils.THE_SORTED_INPUT_WAS + rr.sortedProcesses);

            System.out.println();



            while (rr.terminated.size() < rr.numProcesses) {
                rr.cycle();
            }
            rr.printFinalSummary();

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
}
