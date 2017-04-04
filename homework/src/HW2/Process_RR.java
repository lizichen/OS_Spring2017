package HW2;


/**
 * Created by lizichen1 on 3/6/17.
 */
public class Process_RR {


    int A, B, C, M;

    int state; // 0 - unstarted, 1 - ready, 2 - running, 3 - blocked, 4 - terminatedProcess.

    int timeRemaining;              // time remaining for the process to terminate
    int blockLeft;                  // time until the process back to ready

    int burstTotal;                 // The total time of the CPU burst (used to calculate IO block time)
    int CPU_Burst_TimeRemaining;

    int total_ioTime;
    int total_ReadyState_Time;

    int finishTime;                 // finishTime =  C + total_ioTime + total_ReadyState_Time + A
    int turnaroundTime;             // finishTime - A

    int quantumLimit;

    public Process_RR(int A, int B, int C, int M) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.M = M;

        this.timeRemaining = C;
        this.total_ioTime = 0;
        this.total_ReadyState_Time = 0;
        this.blockLeft = 0;
        this.finishTime = 0;
        this.burstTotal = 0;
        this.CPU_Burst_TimeRemaining = 0;

        this.turnaroundTime = 0;

        this.state = 0; // unstarted
        this.quantumLimit = 1000;
    }

    public void tick()  {
        switch (state) {
            case 0:
            case 4:
                break;
            case 1:
                total_ReadyState_Time++;
                break;
            case 2:
                handleRunTick();
                break;
            case 3:
                handleBlockTick();
                break;
            default:
                System.out.println("WARNING: " + Utils.UNKNOWN_STATE);
                System.exit(-1);
        }
    }

    public void setToRun(int cpuBurst)  {
        this.state = 2; // running state
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
            this.state = 4;
        } else if (CPU_Burst_TimeRemaining <= 0) { //from running to blocked
            this.state = 3;
            this.blockLeft = this.burstTotal * this.M;
        } else if (quantumLimit > 0){
            if (burstTotal != CPU_Burst_TimeRemaining && (burstTotal - CPU_Burst_TimeRemaining) % quantumLimit == 0){
                this.state = 1;
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
            this.state = 1;
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
