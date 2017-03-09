package HW2;


/**
 * Created by lizichen1 on 3/6/17.
 */
public class Process_RR {


    int A, B, C, M;

    int state; // 0 - unstarted, 1 - ready, 2 - running, 3 - blocked, 4 - terminated.

    int timeRemaining;              // time remaining for the process to terminate
    int blockLeft;                  // time until the process back to ready

    int burstTotal;                 // The total time of the CPU burst (used to calculate IO block time)
    int cpuBurstRemaining;

    int total_ioTime;
    int total_ReadyState_Time;

    int finishTime;                 // finishTime =  C + total_ioTime + total_ReadyState_Time + A
    int turnaroundTime;             // finishTime - A

    int quantumMax;

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
        this.cpuBurstRemaining = 0;

        this.turnaroundTime = 0;

        this.state = 0; // unstarted
        this.quantumMax = 1000;
    }

    public void tick()  {
        switch (state) {
            case 0:
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
            case 4:
                break;
            default:
                System.out.println(Utils.UNKNOWN_STATE);
        }
    }

    public void setToRun(int cpuBurst)  {
        this.state = 2;
        if(this.cpuBurstRemaining <= 0) {
            this.burstTotal = cpuBurst;
            this.cpuBurstRemaining = cpuBurst;
        }
    }

    private void handleRunTick()  {
        if (cpuBurstRemaining >= 0) {
            timeRemaining--;
            cpuBurstRemaining--;
        }
        if (timeRemaining <= 0) {
            this.state = 4;
        } else if (cpuBurstRemaining <= 0) { //from running to blocked
            this.state = 3;
            this.blockLeft = this.burstTotal * this.M;
        } else if (quantumMax > 0 && burstTotal != cpuBurstRemaining && (burstTotal - cpuBurstRemaining)%quantumMax==0) { // Set state to ready if quantum has been reached
            this.state = 1;
        }
    }

    private void handleBlockTick() {
        if (blockLeft >= 0) {
            total_ioTime++;
            blockLeft--;
        }
        if (blockLeft <= 0) {
            this.state = 1;
        }
    }

    public String summary() {
        finishTime = this.C + total_ioTime + total_ReadyState_Time + this.A;
        turnaroundTime = finishTime - A;

        StringBuilder sb = new StringBuilder();

        sb.append("        ");
        sb.append(String.format("(A, B, C, M) = (%d, %d, %d, %d)", this.A, this.B, this.C, this.M));
        sb.append(Utils.NEWLINE_SPACE);
        sb.append("Finishing time: " + finishTime + Utils.NEWLINE_SPACE);
        sb.append("Turnaround time: " + turnaroundTime + Utils.NEWLINE_SPACE);
        sb.append("I/O time: " + total_ioTime + Utils.NEWLINE_SPACE);
        sb.append("Waiting time: " + total_ReadyState_Time);

        return sb.toString();
    }
}
