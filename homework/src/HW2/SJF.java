package HW2;

import java.io.File;
import java.util.*;

/**
 * Created by lizichen1 on 3/8/17.
 */
public class SJF extends RR_Scheduler{

    int numberOfProcesses = 0;

   // SJF(allprocess, quantum, verbose, numberOfProcesses);
    public SJF(ArrayList<Process_RR> processes, int quantum, boolean verbose, int numberOfProcesses) {
        super(verbose, processes);
        this.name = "Shortest Job First";
        this.quantum = quantum;
        this.numberOfProcesses = numberOfProcesses;
        setQuantum(processes);
    }

    private static void setQuantum(ArrayList<Process_RR> list) {
        for (Process_RR p : list) {
            p.setQuantumMax(1000);
        }
    }

    protected void maintainReadyQueue() {
        Process_RR p = new Process_RR(0,0,0,0);
        p.setTimeLeft(10000);
        boolean shouldStop = true;
        for (Process_RR temp : ready) {
            if (terminated.contains(temp)) continue;
            if (temp.getBurstLeft() > 0) {
                return;
            }
            // System.out.printf("Process_FCFS %d timeLeft: %d", temp.getId(), temp.timeLeft);
            if (temp.timeLeft < p.timeLeft) {
                p = temp;
                shouldStop = false;
            }
        }
        if (shouldStop) return;

        // Now that we have the shortest ready process, set it to the ready queue
        // Only if it is not yet terminated, not blocked, and not running
        if (!readyQueue.contains(p) && !blockedList.contains(p) && p.getState() != 2) readyQueue.addFirst(p);

        ListIterator<Process_RR> i = ready.listIterator();
    }

    protected ArrayList<Process_RR> sort(ArrayList<Process_RR> processes) {
        Collections.sort(processes, new Comparator<Process_RR>() {
            public int compare(Process_RR a, Process_RR b) {
                int arrivalA = a.getA();
                int arrivalB = b.getA();
                if (arrivalA > arrivalB) return 1;
                else if (arrivalA < arrivalB) return -1;
                else return 0;
            }
        });
        return processes;
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

            SJF sjf = new SJF(allprocess, quantum, verbose, numberOfProcesses);


            System.out.println("The original input was: " + sjf.getOriginalProcesses());
            System.out.println("The (sorted) input was: " + sjf.getSortedProcesses());
            System.out.println();

            while (sjf.notAllTerminated()) {
                sjf.cycle();
            }
            sjf.printResults();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
