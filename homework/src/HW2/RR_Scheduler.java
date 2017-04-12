package HW2;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static HW2.ProcessState.*;

/**
 * Created by lizichen1 on 3/1/17.
 */
public abstract class RR_Scheduler {

    int quantum = Utils.LARGE_QUANTUM_INTEGER;
    private int totalIoBlockCycles = 0;
    ArrayList<Process_RR> processes;
    int numProcesses;

    int cycleIterationNumber = 0;
    boolean verbose;
    boolean noProcessRunning = true;

    String originalProcessesDetailString;
    String sortedProcesses;

    ArrayList<Process_RR> unstartedProcesses;
    LinkedList<Process_RR> ready = new LinkedList<>();
    LinkedList<Process_RR> readyQueue = new LinkedList<>();
    LinkedList<Process_RR> blockedProcessList = new LinkedList<>();
    ArrayList<Process_RR> terminatedProcess = new ArrayList<>();

    RandomNumberProvider randomNumberProvider = new RandomNumberProvider();

    public RR_Scheduler(boolean ver, ArrayList<Process_RR> processes) throws IOException {

        this.verbose = ver;

        this.numProcesses = processes.size();
        this.originalProcessesDetailString = SchedulerHelper.getProcessInformation(this.numProcesses, processes);
        this.processes = Utils.sort(processes);
        this.sortedProcesses = SchedulerHelper.getProcessInformation(this.numProcesses, this.processes);

        this.unstartedProcesses = processes;
    }

    // call for each cycle
    public void cycle() {

        if(verbose)
            echoCycleStatus();

        for (Process_RR p : processes) {
            p.tick();
        }

        prepNextTick();
        cycleIterationNumber++;
    }

    public void prepNextTick() {
        readyAllUnstarted();
        updateReadyQueue();

        if (noProcessRunning && !readyQueue.isEmpty()) {

            Process_RR p = readyQueue.removeLast();

            if(p!=null){
                if (cycleIterationNumber == 17) {
                    p.setToRun(this.randomNumberProvider.randomOS(p.B, verbose));
                }
                else {
                    if (p.CPU_Burst_TimeRemaining >0) {
                        p.state = ProcessState.RUNNING;
                    }
                    else {
                        p.setToRun(this.randomNumberProvider.randomOS(p.B, verbose));
                    }
                }
            }
        }
    }

    private void readyAllUnstarted() {
        noProcessRunning = true;
        boolean hasBlockedProcess = false;
        for (Process_RR p : processes) {

            ProcessState state = p.state;

            if (state == UNSTARTED && cycleIterationNumber >= p.A) {
                p.state = ProcessState.READY;
                this.ready.add(p);
            }

            switch (state) {
                case UNSTARTED:
                    break;
                case READY:
                    if (!ready.contains(p))
                        ready.add(p);
                    if (blockedProcessList.contains(p))
                        blockedProcessList.remove(p);
                    break;
                case RUNNING:
                    noProcessRunning = false;
                    break;
                case BLOCKED:
                    if (!blockedProcessList.contains(p))
                        blockedProcessList.add(p);
                    if (ready.contains(p))
                        ready.remove(p);

                    hasBlockedProcess = true;
                    break;
                case TERMINATED:
                    if (!terminatedProcess.contains(p))
                        terminatedProcess.add(p);
                    break;
                default:
                    System.out.println("WARNING: " + Utils.UNKNOWN_STATE + " : "+state);
                    System.exit(-1);
            }
        }

        if (hasBlockedProcess) {
            totalIoBlockCycles++;
        }
    }

    private void echoCycleStatus() {
        StringBuilder cycleStatusString = new StringBuilder();
        cycleStatusString.append(String.format(Utils.BEFORE_CYCLE_4S, cycleIterationNumber));

        String stateNameString;
        String burst;
        ProcessState state;

        for (Process_RR p : processes) {
            state = p.state;

            if(state == UNSTARTED){
                stateNameString = Utils.UNSTARTED;
                burst = Utils.RESET_BURST_TO_ZERO;
            } else if(state == READY){
                stateNameString = Utils.READY;
                burst = Utils.RESET_BURST_TO_ZERO;
            } else if (state == RUNNING) {
                stateNameString = Utils.RUNNING;
                burst = String.valueOf(p.CPU_Burst_TimeRemaining);
            } else if(state == BLOCKED){
                stateNameString = Utils.BLOCKED;
                burst = String.valueOf(p.blockLeft);
            } else if(state == TERMINATED){
                stateNameString = Utils.TERMINATED;
                burst = Utils.RESET_BURST_TO_ZERO;
            } else{
                stateNameString = Utils.UNKNOWN_STATE;
                burst = Utils.RESET_BURST_TO_ZERO;
            }

            cycleStatusString.append(String.format("\t%10s%5s", stateNameString, burst));
        }

        System.out.println(cycleStatusString.toString()+".");
    }

    public void printFinalSummary() {
        double finish_time = 0;

        double totalCPUTime = 0;
        double total_turnaround_time = 0;
        double total_wait_time = 0;

        double throughput = 0;

        for (int i = 0; i < this.numProcesses; i++) {
            System.out.println("Process #" + i + ": ");
            Process_RR p = processes.get(i);
            System.out.println(p.summary());

            System.out.println();

            if (p.finishTime >= finish_time)
                finish_time = p.finishTime;

            total_wait_time += p.total_ReadyState_Time;
            total_turnaround_time += p.turnaroundTime;

            throughput = (100.0 / finish_time) * this.numProcesses;
            totalCPUTime += p.C;
        }

        double ave_turnaround_time =  total_turnaround_time / this.numProcesses;
        double ave_wait_time = total_wait_time / this.numProcesses;

        double IO_Utilization_Rate = ((double)totalIoBlockCycles / finish_time);
        double CPU_Utilization_Rate = totalCPUTime / finish_time;

        System.out.println("---------------------------------------------------------------");
        System.out.println("Summary Data: ");
        StringBuilder summaryString = new StringBuilder();
        NumberFormat f = new DecimalFormat("#0.0000");

        summaryString.append("        ");
        summaryString.append("| Finishing Time: " + finish_time+Utils.NEWLINE_SPACE);
        summaryString.append("| CPU Utilization: " + f.format(CPU_Utilization_Rate)+Utils.NEWLINE_SPACE);
        summaryString.append("| I/O Utilization: " + f.format(IO_Utilization_Rate)+Utils.NEWLINE_SPACE);
        summaryString.append("| Throughput: " + f.format(throughput) + " processes per hundred cycles"+Utils.NEWLINE_SPACE);
        summaryString.append("| Average turnaround time: " + f.format(ave_turnaround_time)+Utils.NEWLINE_SPACE);
        summaryString.append("| Average waiting time: " + f.format(ave_wait_time));

        System.out.println(summaryString.toString());
        System.out.println("---------------------------------------------------------------");

        System.out.println("\nCommand: java {RR, SJF, FCFS, Uniprogrammed} [--verbose] input-data.txt ");
    }

    protected abstract void updateReadyQueue(); // Abstract method for specific scheduling algorithm to implement

    public void addFirstReadyQueue(Process_RR p){
        if(readyQueue.contains(p) == false){
            if(blockedProcessList.contains(p) == false){
                if(p.state != RUNNING){
                    readyQueue.addFirst(p);
                }
            }
        }else{
            return;
        }
    }
}
