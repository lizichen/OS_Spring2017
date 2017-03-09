package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by lizichen1 on 3/8/17.
 */
public abstract class RR_Scheduler {

    public static final String BEFORE_CYCLE_4S = "Before Cycle \t%4s:";

    ArrayList<Process_RR> processes;
    int numProcesses;
    Scanner random_integers;
    int cycleNum;
    boolean verbose;
    boolean noProcessRunning;
    String name;
    String originalProcesses;
    String sortedProcesses;

    ArrayList<Process_RR> unstarted;
    LinkedList<Process_RR> ready;
    LinkedList<Process_RR> readyQueue;
    LinkedList<Process_RR> blockedList;
    ArrayList<Process_RR> terminated;

    int quantum;

    private int totalIoBlockCycles;


    public RR_Scheduler(boolean verbose, ArrayList<Process_RR> processes) throws FileNotFoundException {
        this.processes = new ArrayList();

        File file = new File("input/random-numbers");
        random_integers = new Scanner(file);


        cycleNum = 0;
        this.verbose = verbose;
        this.numProcesses = processes.size();
        this.originalProcesses = processStringFromList(this.numProcesses, processes);
        this.processes = sort(processes);
        this.sortedProcesses = processStringFromList(this.numProcesses, this.processes);
        this.unstarted = processes;
        noProcessRunning = true;


        this.ready = new LinkedList();
        this.readyQueue = new LinkedList();
        this.blockedList = new LinkedList();
        this.terminated = new ArrayList();


        this.totalIoBlockCycles = 0;
        this.quantum = 99999;
    }

    protected static String processStringFromList(int numProcesses, ArrayList<Process_RR> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(numProcesses + " ");
        for (int i = 0; i < list.size(); i++) {
            sb.append("(");
            Process_RR p = list.get(i);
            sb.append(p.getA() + " ");
            sb.append(p.getB() + " ");
            sb.append(p.getC() + " ");
            sb.append(p.getM());
            sb.append(") ");
        }
        return sb.toString();
    }

    protected ArrayList<Process_RR> sort(ArrayList<Process_RR> processes){
        Collections.sort(processes, new Comparator<Process_RR>() {
            public int compare(Process_RR process_A, Process_RR process_B) {
                int arrivalA = process_A.A;
                int arrivalB = process_B.A;
                if (arrivalA > arrivalB)
                    return 1;
                else if (arrivalA < arrivalB)
                    return -1;
                else
                    return 0;
            }
        });
        return processes;
    }

    // call for each cycle
    public void cycle() {
        if(verbose)
            cycleStatus();
        tickProcesses();
        prepNextTick();
        cycleNum++;
    }

    protected void tickProcesses() {
        for (Process_RR p : processes) {
            p.tick();
        }
    }

    public void prepNextTick() {
        readyAllUnstarted();
        updateReadyQueue();
        if (noProcessRunning && !readyQueue.isEmpty()) {
            Process_RR p = readyQueue.removeLast();
            if (cycleNum == 17) {
                int random = randomOS(p.getB());
                p.setToRun(random);
            }
            else {
                if (p.burstLeft>0) {
                    p.setToRun();
                }
                else {
                    int random = randomOS(p.getB());
                    p.setToRun(random);
                }
            }
        }
    }

    protected abstract void updateReadyQueue(); // Abstract method for specific scheduling algorithm to implement

    public boolean incomplete() {
        return (terminated.size() < numProcesses);
    }

    // Ready all unstarted processes
    private void readyAllUnstarted() {
        noProcessRunning = true;
        boolean atLeastOneBlocked = false;
        for (Process_RR p : processes) {

            int state = p.getState();

            if (state == 0 && cycleNum >= p.getA()) {
                p.setReady();
                this.ready.add(p);
            }

            if (state == 1) {
                if (!ready.contains(p)) ready.add(p);
                if (blockedList.contains(p)) blockedList.remove(p);
            }
            // Tracks if there is a currently running process
            if (state == 2) {
                // System.out.printf("Process_FCFS %d is running\n", p.getId());
                noProcessRunning = false;
            }

            if (state == 3) {
                if (!blockedList.contains(p)) blockedList.add(p);
                if (ready.contains(p)) ready.remove(p);
                atLeastOneBlocked = true;
            }

            if (state == 4) {
                if (!terminated.contains(p)) terminated.add(p);
            }
        }
        if (atLeastOneBlocked) {
            totalIoBlockCycles += 1;
        }

    }

    public ArrayList<Process_RR> getProcesses() {
        return processes;
    }

    private void cycleStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(BEFORE_CYCLE_4S, cycleNum));
        for (Process_RR p : processes) {
            int state = p.getState();
            String stateName;
            String burst;
            switch (state) {
                case 0:
                    stateName = Utils.UNSTARTED;
                    burst = "0";
                    break;
                case 1:
                    stateName = Utils.READY;
                    burst = "0";
                    break;
                case 2:
                    stateName = Utils.RUNNING;
                    burst = String.valueOf(p.getBurstLeft());
                    break;
                case 3:
                    stateName = Utils.BLOCKED;
                    burst = String.valueOf(p.getBlockLeft());
                    break;
                case 4:
                    stateName = Utils.TERMINATED;
                    burst = "0";
                    break;
                default:
                    stateName = Utils.UNKNOWN_STATE;
                    burst = "0";
                    break;
            }
            sb.append(String.format("\t%12s%3s", stateName, burst));
        }
        System.out.println(sb.toString()+".");
    }

    public int randomOS(int B) {
        String next = random_integers.nextLine();
        if (verbose) {
            System.out.println("Find burst when choosing next process to run: " + next + " / " + B + "= " + (Integer.parseInt(next) % B));
        }
        return 1 + (Integer.parseInt(next) % B);
    }

    public void printFinalSummary() {
        int finishingTime = 0;
        double cpuUtilization = 0;
        double throughput = 0;
        double avgTurnaroundTime = 0;
        double avgWaitTime = 0;
        double ioUtilization = 0;

        int i = 0;

        System.out.println("The scheduling algorithm used was " + this.name);
        System.out.println();

        for (i = 0; i < numProcesses; i++) {
            System.out.println("Process_FCFS " + i + ": ");
            Process_RR p = processes.get(i);
            System.out.println(p.results());
            System.out.println();

            finishingTime = (p.getFinishTime() >= finishingTime) ? p.getFinishTime() : finishingTime;
            avgWaitTime += p.getWaitTime();
            avgTurnaroundTime += p.getTurnaroundTime();
            throughput = (100.0/finishingTime) * numProcesses;
            cpuUtilization += p.C;
        }

        avgTurnaroundTime /= i;
        avgWaitTime /= i;
        ioUtilization = ((double)totalIoBlockCycles/finishingTime);
        cpuUtilization /= finishingTime;

        System.out.println("Summary Data: ");
        StringBuilder sb = new StringBuilder();
        NumberFormat f = new DecimalFormat("#0.000000");
        sb.append("        ");
        sb.append("Finishing Time: " + finishingTime);
        sb.append("\n        ");
        sb.append("CPU Utilization: " + f.format(cpuUtilization));
        sb.append("\n        ");
        sb.append("I/O Utilization: " + f.format(ioUtilization));
        sb.append("\n        ");
        sb.append("Throughput: " + f.format(throughput) + " processes per hundred cycles");
        sb.append("\n        ");
        sb.append("Average turnaround time: " + f.format(avgTurnaroundTime));
        sb.append("\n        ");
        sb.append("Average waiting time: " + f.format(avgWaitTime));
        System.out.println(sb.toString());
    }

    public String getOriginalProcesses() {
        return originalProcesses;
    }

    public String getSortedProcesses() { return sortedProcesses; }

}
