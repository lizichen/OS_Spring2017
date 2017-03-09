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

    public static final int LARGE_QUANTUM_INTEGER = 99999;
    public static final String RESET_BURST_TO_ZERO = "0";


    int quantum;

    private int totalIoBlockCycles;

    public static final String BEFORE_CYCLE_4S = "Before Cycle \t%4s:";

    ArrayList<Process_RR> processes;
    int numProcesses;
    Scanner random_integers;
    int cycleNum;
    boolean verbose;
    boolean noProcessRunning;


    String originalProcesses;
    String sortedProcesses;

    ArrayList<Process_RR> unstarted;
    LinkedList<Process_RR> ready;
    LinkedList<Process_RR> readyQueue;
    LinkedList<Process_RR> blockedList;
    ArrayList<Process_RR> terminated;



    public RR_Scheduler(boolean verbose, ArrayList<Process_RR> processes) throws FileNotFoundException {

        this.verbose = verbose;

        this.processes = new ArrayList();

        File file = new File(Utils.RANDOM_NUMBER_FILE);
        random_integers = new Scanner(file);

        cycleNum = 0;

        this.numProcesses = processes.size();
        this.originalProcesses = getProcessInfo(this.numProcesses, processes);
        this.processes = sort(processes);
        this.sortedProcesses = getProcessInfo(this.numProcesses, this.processes);
        this.unstarted = processes;
        noProcessRunning = true;

        this.ready = new LinkedList();
        this.readyQueue = new LinkedList();
        this.blockedList = new LinkedList();
        this.terminated = new ArrayList();

        this.totalIoBlockCycles = 0;
        this.quantum = LARGE_QUANTUM_INTEGER;
    }

    public static String getProcessInfo(int numProcesses, ArrayList<Process_RR> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(numProcesses + " ");
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append("(");
            Process_RR p = list.get(i);
            stringBuilder.append(p.A + " ");
            stringBuilder.append(p.B + " ");
            stringBuilder.append(p.C + " ");
            stringBuilder.append(p.M);
            stringBuilder.append(") ");
        }
        return stringBuilder.toString();
    }

    // call for each cycle
    public void cycle() {
        if(verbose)
            cycleStatus();
        for (Process_RR p : processes) {
            p.tick();
        }
        prepNextTick();
        cycleNum++;
    }

    public void prepNextTick() {
        readyAllUnstarted();
        updateReadyQueue();
        if (noProcessRunning && !readyQueue.isEmpty()) {
            Process_RR p = readyQueue.removeLast();
            if (cycleNum == 17) {
                int random = randomOS(p.B);
                p.setToRun(random);
            }
            else {
                if (p.cpuBurstRemaining >0) {
                    p.state = 2;
                }
                else {
                    int random = randomOS(p.B);
                    p.setToRun(random);
                }
            }
        }
    }



    private void readyAllUnstarted() {
        noProcessRunning = true;
        boolean atLeastOneBlocked = false;
        for (Process_RR p : processes) {

            int state = p.state;

            if (state == 0 && cycleNum >= p.A) {
                p.state = 1;
                this.ready.add(p);
            }
            if (state == 1) {
                if (!ready.contains(p))
                    ready.add(p);
                if (blockedList.contains(p))
                    blockedList.remove(p);
            }else if (state == 2) {
                noProcessRunning = false;
            }else if (state == 3) {
                if (!blockedList.contains(p))
                    blockedList.add(p);
                if (ready.contains(p))
                    ready.remove(p);
                atLeastOneBlocked = true;
            }else if (state == 4) {
                if (!terminated.contains(p)) terminated.add(p);
            }
        }

        if (atLeastOneBlocked) {
            totalIoBlockCycles++;
        }
    }

    private void cycleStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(BEFORE_CYCLE_4S, cycleNum));
        for (Process_RR p : processes) {
            int state = p.state;
            String stateName;
            String burst;
            if(state == 0){
                stateName = Utils.UNSTARTED;
                burst = RESET_BURST_TO_ZERO;
            }else if(state == 1){
                stateName = Utils.READY;
                burst = RESET_BURST_TO_ZERO;
            } else if (state == 2) {
                stateName = Utils.RUNNING;
                burst = String.valueOf(p.cpuBurstRemaining);
            } else if(state == 3){
                stateName = Utils.BLOCKED;
                burst = String.valueOf(p.blockLeft);
            }else if(state == 4){
                stateName = Utils.TERMINATED;
                burst = RESET_BURST_TO_ZERO;
            }else{
                stateName = Utils.UNKNOWN_STATE;
                burst = RESET_BURST_TO_ZERO;
            }
            sb.append(String.format("\t%12s%3s", stateName, burst));
        }
        System.out.println(sb.toString()+".");
    }



    public void printFinalSummary() {
        int finishingTime = 0;

        double cpuUtilization = 0;
        double throughput = 0;
        double averageTurnAroundTime = 0;
        double averageWaitTime = 0;
        double ioUtilization = 0;

        int i;

        for (i = 0; i < numProcesses; i++) {
            System.out.println("Process #" + i + ": ");
            Process_RR p = processes.get(i);
            System.out.println(p.summary()+"\n");


            finishingTime = (p.finishTime >= finishingTime) ? p.finishTime : finishingTime;
            averageWaitTime += p.total_ReadyState_Time;
            averageTurnAroundTime += p.turnaroundTime;
            throughput = (100.0/finishingTime) * numProcesses;
            cpuUtilization += p.C;
        }

        averageTurnAroundTime /= i;
        averageWaitTime /= i;

        ioUtilization = ((double)totalIoBlockCycles/finishingTime);
        cpuUtilization /= finishingTime;

        System.out.println("Summary Data: ");
        StringBuilder sb = new StringBuilder();
        NumberFormat f = new DecimalFormat("#0.0000000");

        sb.append("        ");
        sb.append("Finishing Time: " + finishingTime+Utils.NEWLINE_SPACE);
        sb.append("CPU Utilization: " + f.format(cpuUtilization)+Utils.NEWLINE_SPACE);
        sb.append("I/O Utilization: " + f.format(ioUtilization)+Utils.NEWLINE_SPACE);
        sb.append("Throughput: " + f.format(throughput) + " processes per hundred cycles"+Utils.NEWLINE_SPACE);
        sb.append("Average turnaround time: " + f.format(averageTurnAroundTime)+Utils.NEWLINE_SPACE);
        sb.append("Average waiting time: " + f.format(averageWaitTime));
        System.out.println(sb.toString());

        System.out.println("\nPlease Use Command: java {RR, SJF, FCFS, Uniprogrammed} --verbose input.txt ");
    }

    protected ArrayList<Process_RR> sort(ArrayList<Process_RR> processes){
        Collections.sort(processes, new Comparator<Process_RR>() {
            public int compare(Process_RR process_A, Process_RR process_B) {
                if (process_A.A > process_B.A)
                    return 1;
                else if (process_A.A < process_B.A)
                    return -1;
                else
                    return 0;
            }
        });
        return processes;
    }

    public int randomOS(int B) {
        String next = random_integers.nextLine();
        if (verbose) {
            System.out.println(Utils.FIND_BURST_WHEN_CHOOSING_NEXT_PROCESS_TO_RUN + next + " / " + B + "= " + (Integer.parseInt(next) % B));
        }
        return 1 + (Integer.parseInt(next) % B);
    }

    protected abstract void updateReadyQueue(); // Abstract method for specific scheduling algorithm to implement
}
