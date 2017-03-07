package HW2;

/**
 * Created by lizichen1 on 3/3/17.
 */
public class Process implements Comparable<Process>{

    int ArrivalTime;
    int BurstRange;
    int CPUTime;
    int IOMultiplier;

    int burstTime;
    int previousCPUBurst;

    int finishUpTime;
    int CPUTime_copy;

    int overallIOTime;
    int overallWaitingTime;

    int status; // -1 for unstarted, 0 for ready, 1 for running, 2 for blocked, 3 for complete

    public Process(String A, String B, String C, String M){
        this.ArrivalTime = Integer.valueOf(A);
        this.BurstRange = Integer.valueOf(B);
        this.CPUTime = Integer.valueOf(C);
        this.CPUTime_copy = this.CPUTime;
        this.IOMultiplier = Integer.valueOf(M);
        this.status = -1;
        this.burstTime = 0;
        this.previousCPUBurst= 0;
        this.finishUpTime = 0;
        this.overallIOTime = 0;
        this.overallWaitingTime = 0;
    }

    public int compareTo(Process anotherProcess) {

        return this.ArrivalTime - anotherProcess.ArrivalTime;
    }

}
