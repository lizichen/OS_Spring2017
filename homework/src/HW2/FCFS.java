package HW2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lizichen1 on 2/27/17.
 */
public class FCFS {

    int numberOfProcesses;
    ArrayList<Process_FCFS> processFCFSes = new ArrayList<>();
    RandomNumberProvider worktime = new RandomNumberProvider();
    int overallFinishTime = 0;

    public FCFS(String inputString) throws IOException {
        String[] tokens = inputString.split("\\s+");
        this.numberOfProcesses = Integer.valueOf(tokens[0]);

        for(int i=1;i<tokens.length;i+=4){
            Process_FCFS newProcessFCFS = new Process_FCFS(tokens[i].substring(1), tokens[i+1], tokens[i+2], tokens[i+3].substring(0, tokens[i+3].length()-1));
            this.processFCFSes.add(newProcessFCFS);
        }
    }

    private void sortProcessesByArrivalTime() { // Sort the Arrival Time ascendingly.
        Collections.sort(this.processFCFSes);
        System.out.print("The (sorted) input was: "+this.numberOfProcesses+" ");
        for(Process_FCFS p : this.processFCFSes){
            System.out.print("("+p.ArrivalTime+" "+p.BurstRange+" "+p.CPUTime+" "+p.IOMultiplier+") ");
        }
        System.out.println(" ");
        System.out.println("Number of Processes: "+this.numberOfProcesses);
        System.out.println(" ");
    }

    private void start() {

        int ticker = -1;
        int cpuBurstTime = 0;
        int timeLeftToSwitch = 0;
        int[] completedProcess = new int[this.numberOfProcesses];
        int[] runningProcess = new int[this.numberOfProcesses];

        while(true){
            int result = 1;
            int numberOfActiveProcess = 0;
            for(Integer i:completedProcess){
                result *= i;
                if(i == 2){
                    numberOfActiveProcess++;
                }
            }
            //System.out.println("# Active Process_FCFS = "+numberOfActiveProcess);
            if(result == 1){
                System.out.println("Success! All processFCFSes completed!");
                break;
            }

            System.out.print("Before Cycle\t"+(ticker+1)+":\t");

            for(int i=0;i<numberOfProcesses;i++){
                Process_FCFS p = this.processFCFSes.get(i);
                if(p.CPUTime <= 0){
                    completedProcess[i] = 1;
                    if(p.finishUpTime == 0) {
                        p.finishUpTime = ticker - 1;
                    }
                    //System.out.print("\tProcess #"+i+" is completed!");
                    System.out.print("\tTerminated \t0");
                    continue;
                }

                // from unstarted to ready
                if(p.status == -1){
                    if(p.ArrivalTime == ticker){
                        p.status = 0; //ready
                        completedProcess[i] = 2;
                        //System.out.println("\tProcess #"+i+" is ready.");
                        System.out.print("\tReady \t0\t");
                    }else{
                        //System.out.println("\tProcess #"+i+" is unstarted.");
                        System.out.print("\tunstarted \t0");
                    }
                }
                // from ready to run
                else if(p.status == 0){
                    if(timeLeftToSwitch == 0){
                        p.status = 1;
                        runningProcess[i] = 1;
                        cpuBurstTime = this.worktime.randomOS(p.BurstRange);
                        p.previousCPUBurst = cpuBurstTime;
                        //System.out.println("\tProcess #"+i+" is running for "+cpuBurstTime+" ticks.");
                        System.out.print("\tRunning \t"+cpuBurstTime);
                        timeLeftToSwitch = cpuBurstTime;
                        p.CPUTime -= cpuBurstTime;
                        p.burstTime = cpuBurstTime;
                        p.burstTime--;
                    }else{
                        //System.out.println("\tProcess #"+i+" is ready/waiting.");
                        p.overallWaitingTime++;
                        System.out.print("\tReady \t0\t");
                    }
                }
                // from run to blocked
                else if(p.status == 1){
                    if(p.burstTime == 0){
                        p.status = 2; // blocked
                        runningProcess[i] = 0;
                        p.burstTime = p.previousCPUBurst * p.IOMultiplier;
                        p.overallIOTime += p.burstTime;
                        //System.out.println("\tPreviousCPUBurst:"+p.previousCPUBurst+" IOMultiplier:"+p.IOMultiplier);
                        //System.out.println("\tProcess #"+i+" is blocked for "+p.burstTime+" ticks.");
                        System.out.print("\tBlocked \t"+p.burstTime);
                        p.burstTime--;
                    }else {
                        p.burstTime--;
                        //System.out.println("\tProcess #"+i+" is running for "+p.burstTime+" ticks.");
                        System.out.print("\tRunning \t"+p.burstTime);
                    }
                    // if only after this tick, the process is ready
                    if(p.burstTime == 0){
                        if(numberOfActiveProcess < 3) {
                            p.status = 0;
                            System.out.print("\tReady \t0\t");
                        }
                    }
                }
                // from blocked to ready
                else if(p.status == 2){
                    if(p.burstTime == 0){
                        p.status = 0;
                        System.out.print("\tReady \t0");
                        p.overallWaitingTime++;

                        //if nobody is running then just run!
                        boolean hasrunningprocess = false; //runningProcess[k] = 0 is not running, 1 is running
                        for(Integer k:runningProcess){
                            if(k == 1) {
                                hasrunningprocess = true;
                                break;
                            }
                        }
                        if(!hasrunningprocess){
                            p.overallWaitingTime--;
                            p.status = 1;
                            runningProcess[i] = 1;
                            cpuBurstTime = this.worktime.randomOS(p.BurstRange);
                            p.previousCPUBurst = cpuBurstTime;
                            System.out.println("\tRunning \t"+cpuBurstTime);
                            timeLeftToSwitch = cpuBurstTime;
                            p.CPUTime -= cpuBurstTime;
                            p.burstTime = cpuBurstTime;
                            p.burstTime--;
                        }
                    }else{
                        System.out.print("\tBlocked \t"+ p.burstTime--);
                    }
                }
            }
            System.out.println();
            if(timeLeftToSwitch!=0) {
                timeLeftToSwitch--;
            }
            ticker++;
        }
        // ends clock
        this.overallFinishTime = ticker-2;

    }

    private void printProcesses() {
        int sumRunningTime = 0;
        int sumIOTime = 0;
        int sumTurnaroundTime = 0;
        int sumWaitingTime = 0;

        for(int i=0;i<numberOfProcesses;i++){
            Process_FCFS p = this.processFCFSes.get(i);
            System.out.println("Process_FCFS "+i+":");
            System.out.println("\t\t(A, B, C, M) = ("+p.ArrivalTime+", "+p.BurstRange+", "+p.CPUTime_copy+", "+p.IOMultiplier+")");
            sumRunningTime+=p.CPUTime_copy;

            System.out.println("\t\tFinishing time: "+p.finishUpTime);
            System.out.println("\t\tTurnaround time: "+(p.finishUpTime-p.ArrivalTime));
            sumTurnaroundTime += (p.finishUpTime-p.ArrivalTime);
            System.out.println("\t\tI/O time: "+p.overallIOTime);
            sumIOTime+=p.overallIOTime;
            System.out.println("\t\tWaiting time: "+p.overallWaitingTime);
            sumWaitingTime += p.overallWaitingTime;
            System.out.println();
        }

        System.out.println("Summary Data:");
        System.out.println("\tFinishing time: "+ this.overallFinishTime);
        System.out.println("\tSum running time: "+sumRunningTime);

        System.out.printf("\tCPU Utilization: %.6f \n",     ((double)sumRunningTime / (double)this.overallFinishTime));
        System.out.printf("\tI/O Utilization: %.6f \n",     ((double)sumIOTime/(double)this.overallFinishTime));
        System.out.printf("\tThroughput: %.6f processFCFSes per hundred cycles\n",           (double)(100 * this.numberOfProcesses)/this.overallFinishTime);
        System.out.printf("\tAverage turnaround time: %.6f\n", (double)(sumTurnaroundTime/this.numberOfProcesses));
        System.out.printf("\tAverage waiting time: %.6f\n",    (double)(sumWaitingTime/this.numberOfProcesses));

    }

    public static void main(String[] args) throws IOException {
        // input format: java FCFS [--verbose] input-1.txt
        //java HW2/FCFS /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt
        String filename = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";

        if(args.length == 2){
            if(args[0].equals("--verbose")){

            }
        }else if(args.length == 1){ // java FCFS input-6.txt
            filename = args[0];
        }

        String inputString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        FCFS newscheduler = new FCFS(inputString);
        System.out.println("The original input was: "+inputString);
        newscheduler.sortProcessesByArrivalTime();
        newscheduler.start();
        newscheduler.printProcesses();
    }
}
