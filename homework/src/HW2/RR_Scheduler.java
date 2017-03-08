package HW2;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by lizichen1 on 3/8/17.
 */
public abstract class RR_Scheduler {

    protected String schedulingAlgoName;
    protected ArrayList<Process_RR> processes;
    protected int numProcesses;
    protected Scanner random;
    protected int cycleNum;
    protected boolean verbose;
    protected boolean noProcessRunning;
    protected String name;
    protected String originalProcesses;
    protected String sortedProcesses;

    // Holds all processes in a specific state
    protected ArrayList<Process_RR> unstarted;
    protected LinkedList<Process_RR> ready;
    protected LinkedList<Process_RR> readyQueue;
    protected LinkedList<Process_RR> blockedList;
    protected ArrayList<Process_RR> terminated;
    protected Comparator<Process_RR> comparator;
    protected int quantum;

    private int totalIoBlockCycles;

    public RR_Scheduler(boolean verbose, ArrayList<Process_RR> processes) {
        this.processes = new ArrayList<Process_RR>();
        initRandomStream();
        cycleNum = 0;
        this.verbose = verbose;
        this.numProcesses = processes.size();
        this.originalProcesses = processStringFromList(this.numProcesses, processes);
        this.processes = sort(processes);
        this.sortedProcesses = processStringFromList(this.numProcesses, this.processes);
        this.unstarted = processes;
        noProcessRunning = true;
        this.ready = new LinkedList<Process_RR>();
        this.readyQueue = new LinkedList<Process_RR>();
        this.blockedList = new LinkedList<Process_RR>();
        this.terminated = new ArrayList<Process_RR>();
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

    // Sorts the arrayList depending on the required sorting algorithm
    protected abstract ArrayList<Process_RR> sort(ArrayList<Process_RR> processes);

    // Simulates one cpu cycle
    public void cycle() {
        // stall(40);
        if(verbose)
            preCyclePrint();
        tickProcesses();
        prepNextTick();
        cycleNum++;
    }

    public void stall(int stallCycle) {
        if (cycleNum == stallCycle) {
            System.out.println("STALLING ON PURPOSE!");
            while (true) {
            }
        }
    }

    protected void tickProcesses() {
        for (Process_RR p : processes) {
            p.tick();
        }
    }

    // Maintains the necessary data structures for determining process state changes
    // Prepares for tickProcesses() call by setting the correct state for all processes
    public void prepNextTick() {
        readyAllUnstarted();
        // printBlockedList();
        // Maintain the queue of ready processes
        maintainReadyQueue();
        // printLinkedList(readyQueue);

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

    public void printLinkedList(LinkedList<Process_RR> list) {
        for (Process_RR i : list) {
            System.out.print(i.toString() + " |");
        }
        System.out.println();
    }

    // Abstract method for specific scheduling algorithm to implement
    protected abstract void maintainReadyQueue();

    public boolean notAllTerminated() {
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

    public void printProcesses() {
        System.out.println("Printing all processes for this scheduler:");
        System.out.println("----------------------");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("Process_FCFS " + i + ": " + processes.get(i).toString());
        }
    }

    public String getName() {
        return this.schedulingAlgoName;
    }

    public void addProcess(Process_RR p) {
        processes.add(p);
    }

    public ArrayList<Process_RR> getProcesses() {
        return processes;
    }

    public void printBlockedList() {
        System.out.print("blocked list: ");
        for (Process_RR p : blockedList) {
            System.out.print(p.getId() + " ");
        }
        System.out.println();
    }

    private void preCyclePrint() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Before cycle %4s:", cycleNum));
        for (Process_RR p : processes) {
            int state = p.getState();
            String stateName;
            String burst;
            switch (state) {
                case 0:		stateName = "unstarted";
                    burst = "0";
                    break;
                case 1:		stateName = "ready";
                    burst = "0";
                    break;
                case 2:		stateName = "running";
                    // burst = String.valueOf(quantum-(p.getBurstLeft()%quantum));
                    burst = String.valueOf(p.getBurstLeft());
                    break;
                case 3:		stateName = "blocked";
                    burst = String.valueOf(p.getBlockLeft());
                    break;
                case 4:		stateName = "terminated";
                    burst = "0";
                    break;
                default: 	stateName = "?";
                    burst = "0";
                    break;
            }
            sb.append(String.format("%12s%3s", stateName, burst));
        }
        System.out.println(sb.toString()+".");
    }

    private void initRandomStream() {
        try {
            File file = new File("input/random-numbers");
            if (file.exists()) random = new Scanner(file);
            else System.out.println("Could not find random-numbers");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public int randomOS(int B) {
        String next = random.nextLine();
        if (verbose) {
            System.out.println("Find burst when choosing next process to run: " + next + " / " + B + "= " + (Integer.parseInt(next) % B));
        }
        return 1 + (Integer.parseInt(next) % B);
    }

    public void run() {
        Process_RR p1 = new Process_RR(0,1,100,2);
        while (true) {
            break;
        }

        System.out.println("I AM RUNNING.");
    }
    public void printResults() {
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

    public String getOriginalProcesses() { return originalProcesses; }
    public String getSortedProcesses() { return sortedProcesses; }

}
