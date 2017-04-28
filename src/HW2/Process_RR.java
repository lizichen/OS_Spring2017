package HW2;


import static HW2.ProcessState.*;
import static HW2.Utils.LARGE_QUANTUM_INTEGER;

/**
 * Created by lizichen1 on 3/1/17.
 */
public class Process_RR {

    int A, B, C, M;
    int timeRemaining;              // time remaining for the process to terminate


    int total_ioTime = 0;
    int total_ReadyState_Time = 0;
    int blockLeft = 0;                  // time until the process back to ready
    int finishTime = 0;                 // finishTime =  C + total_ioTime + total_ReadyState_Time + A
    int burstTotal = 0;                 // The total time of the CPU burst (used to calculate IO block time)
    int CPU_Burst_TimeRemaining = 0;
    int turnaroundTime = 0;             // finishTime - A
    ProcessState state = UNSTARTED;
    int quantumLimit = LARGE_QUANTUM_INTEGER;


    public Process_RR(int A, int B, int C, int M) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.M = M;
        this.timeRemaining = C;
    }


    public void tick()  {
        switch (state) {
            case UNSTARTED:
            case TERMINATED:
                break;
            case READY:
                total_ReadyState_Time++;
                break;
            case RUNNING:
                handleRunTick();
                break;
            case BLOCKED:
                handleBlockTick();
                break;
            default:
                System.out.println("WARNING: " + Utils.UNKNOWN_STATE);
                System.exit(-1);
        }
    }


    public void setToRun(int cpuBurst)  {
        this.state = RUNNING; // running state
        if(this.CPU_Burst_TimeRemaining <= 0) {
            this.burstTotal = cpuBurst;
            this.CPU_Burst_TimeRemaining = cpuBurst;
        }
    }


    private void handleRunTick()  {
        if (CPU_Burst_TimeRemaining >= 0) {
            timeRemaining--;
            CPU_Burst_TimeRemaining--;
        }

        if (timeRemaining <= 0) {
            this.state = TERMINATED;
        } else if (CPU_Burst_TimeRemaining <= 0) { //from running to blocked
            this.state = BLOCKED;
            this.blockLeft = this.burstTotal * this.M;
        } else if (quantumLimit > 0){
            if (burstTotal != CPU_Burst_TimeRemaining && (burstTotal - CPU_Burst_TimeRemaining) % quantumLimit == 0){
                this.state = READY;
            }
        }
    }

    private void handleBlockTick() {
        if (blockLeft >= 0) {
            total_ioTime++;
            blockLeft--;
        }

        // change state
        if (blockLeft <= 0) {
            this.state = READY;
        }
    }

    public String summary() {
        finishTime = this.C + total_ioTime + total_ReadyState_Time + this.A;
        turnaroundTime = finishTime - A;

        StringBuilder summarystring = new StringBuilder();

        summarystring.append("        ");
        summarystring.append(String.format("(A, B, C, M) = (%d, %d, %d, %d)", this.A, this.B, this.C, this.M));
        summarystring.append(Utils.NEWLINE_SPACE);
        summarystring.append("Finishing time: " + finishTime + Utils.NEWLINE_SPACE);
        summarystring.append("Turnaround time: " + turnaroundTime + Utils.NEWLINE_SPACE);
        summarystring.append("I/O time: " + total_ioTime + Utils.NEWLINE_SPACE);
        summarystring.append("Waiting time: " + total_ReadyState_Time);

        return summarystring.toString();
    }
}
