package HW2;

import java.io.File;
import java.util.*;

public class RR extends RR_Scheduler{

    private static List<String> processList;
    private static int numberOfProcesses;
    private static ArrayList<Process_RR> allProcesses;
    protected ArrayList<Process_RR> terminated;

    public RR(ArrayList<Process_RR> processes, int quantum, boolean verbose, int numberOfProcesses){

        super(verbose, processes);
        this.quantum = quantum;
        setQuantum(processes);
    }

    public static void main(String[] args){

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

            RR rr = new RR(allprocess, quantum, verbose, numberOfProcesses);



            System.out.println("The original input was: " + rr.getOriginalProcesses());
            System.out.println("The (sorted) input was: " + rr.getSortedProcesses());
            System.out.println();

            while (rr.notAllTerminated()) {
                rr.cycle();
            }
            rr.printResults();

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    private static void setQuantum(ArrayList<Process_RR> list) {
        for (Process_RR p : list) {
            p.setQuantumMax(2);
        }
    }

    @Override
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

    @Override
    protected void maintainReadyQueue() {
        ListIterator<Process_RR> i = ready.listIterator();
        // LinkedList<Process_FCFS> newReadyList = (LinkedList<Process_FCFS>) ready.clone();
        while (i.hasNext()) {
            Process_RR p = i.next();
            if (!readyQueue.contains(p)) readyQueue.addFirst(p);
        }
        i = readyQueue.listIterator();
        while (i.hasNext()) {
            Process_RR p = i.next();
            if (ready.contains(p)) ready.remove(p);
        }
		/*
		for (Process_FCFS p : ready) {
			readyQueue.addFirst(p);
		}
		*/
    }
}
