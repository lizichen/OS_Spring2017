package HW2;

/**
 * Created by lizichen1 on 3/6/17.
 */
public class Process_RR {

    int id;

    // A - Arrival time
    // B - The inclusive limit of a random cpu burst
    // C - Total CPU time needed
    // M - The multiplier for calculating I/O burst (CPU burst * M)
    int A, B, C, M;

    // 0 - unstarted
    // 1 - ready
    // 2 - running
    // 3 - blocked
    // 4 - terminated
    int state;

    // Keep track of total IO Time (time in Blocked state)
    int ioTime;

    // Keep track of total wait time (time in Ready state)
    int waitTime;

    // This is equal to C + ioTime + waitTime + A
    int finishTime;

    // This is equal to finishTime - A
    int turnaroundTime;

    // Variables to track the progress of a process
    // How much longer before the process is complete
    int timeLeft;

    // If in block state, how long until process is ready
    int blockLeft;

    // The total time of the CPU burst (used to calculate IO block time)
    int burstTotal;

    // Tracks how much longer process has in cpuBurst
    int burstLeft;

    // Tracks how many bursts have occured before a block
    int burstCount;

    int quantumMax;

    public Process_RR(int A, int B, int C, int M) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.M = M;
        this.timeLeft = C;
        this.ioTime = 0;
        this.waitTime = 0;
        this.finishTime = 0;
        this.blockLeft = 0;
        this.turnaroundTime = 0;
        this.burstTotal = 0;
        this.burstLeft = 0;
        // Set state to unstarted
        this.state = 0;
        this.quantumMax = 1000;
    }


    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
    public void setQuantumMax(int quantum) {
        this.quantumMax = quantum;
    }

    public int getQuantumMax() { return this.quantumMax; }

    public int getState() {
        return state;
    }

    // Sets the process to ready
    public void setReady() {
        if (this.state == 4){
            System.out.println("Wrong process state!");
        }
        this.state = 1;
    }

    // Sets the process to run
    public void setToRun(int cpuBurst)  {
        if (this.blockLeft > 0){
            System.out.println("Wrong process state!");
        }
        this.state = 2;

        // Only reset the burst if the process's burst has finished
        // Reason: Could be interrupted by RR quantum burst
        if(this.burstLeft <= 0) {
            // Sets the burst time for the process
            this.burstTotal = cpuBurst;
            this.burstLeft = cpuBurst;
        }
    }

    // Sets the process to run without a cpuBurst
    public void setToRun()  {
        if (this.blockLeft > 0){
            System.out.println("Attempted to run a process that still has blockTime.");
        }
        this.state = 2;
    }

    public void setTerminate() {
        if (this.timeLeft > 0){
            System.out.println("Process_FCFS terminated before completion.");
        }

        // System.out.println("Set terminate called.");
        this.state = 4;
    }

    public void setBlocked()  {
        if (this.state != 2) {
            System.out.println("Attempted to block non-running process.");
        }
        // System.out.println("Set blocked called.");
        this.state = 3;
        this.blockLeft = this.burstTotal * this.M;
    }

    public void tick()  {
        switch (state) {
            // Unstarted State - Do nothing
            case 0: break;

            // Ready State
            case 1: handleReadyTick();
                break;

            // Running State
            case 2: handleRunTick();
                break;

            // Blocked State
            case 3: handleBlockTick();
                break;

            // Terminated State
            case 4: handleTerminationTick();
                break;

            default:
                System.out.println("State unknown.");
        }
    }

    private void handleReadyTick()  {
        if (state!=1) {
            System.out.println("handleReadyTick() invoked when not in ready state.");
        }
        waitTime++;
    }

    private void handleRunTick()  {
        if (state!=2) {
            System.out.println("handleRunTick() invoked when not in ready state.");
        }

        // System.out.println("Running: Burst Left: " + burstLeft + " | Time Left: " + timeLeft);
        if (burstLeft >= 0) {
            timeLeft--;
            burstLeft--;
        }

        // Set state to terminate if process has finished all necessary CPU ticks
        if (timeLeft <= 0) {
            setTerminate();
            return;
        }
        // Set state to blocked if process has finished its burst cycle
        else if (burstLeft <= 0) {
            setBlocked();
            return;
        }

        // Set state to ready if quantum has been reached
        else if (quantumMax > 0 && burstTotal != burstLeft && (burstTotal - burstLeft)%quantumMax==0) {
            setReady();
            return;
        }
    }

    private void handleBlockTick() {
        if (state!=3){
            System.out.println("error state!");
        }

        if (blockLeft >= 0) {
            ioTime++;
            blockLeft--;
        }

        // Set state to ready if process has finished I/O
        if (blockLeft <= 0) {
            // System.out.println("Block is complete, setting to ready.");
            setReady();
            return;
        }
    }

    // Essentially does nothing
    private void handleTerminationTick() {
        return;
    }

    public String toString() {
        String s = "";
        s += "A: " + this.A + "\t| B: " + this.B + "\t| C: " + this.C + "\t| M: " + this.M;
        return s;
    }

    public String results() {
        // System.out.format("Process_FCFS %d Finish time: this.c(%d) + ioTime(%d) + waitTime(%d) = %d\n", this.id, this.C, ioTime, waitTime, this.C + ioTime + waitTime);
        finishTime = this.C + ioTime + waitTime + this.A;
        turnaroundTime = finishTime - A;

        StringBuilder sb = new StringBuilder();
        sb.append("        ");
        sb.append(String.format("(A, B, C, M) = (%d, %d, %d, %d)", this.A, this.B, this.C, this.M));
        sb.append("\n        ");
        sb.append("Finishing time: " + finishTime);
        sb.append("\n        ");
        sb.append("Turnaround time: " + turnaroundTime);
        sb.append("\n        ");
        sb.append("I/O time: " + ioTime);
        sb.append("\n        ");
        sb.append("Waiting time: " + waitTime);
        return sb.toString();
    }

    public int getA() { return this.A; }
    public int getB() { return this.B; }
    public int getC() { return this.C; }
    public int getM() { return this.M; }
    public int getBlockLeft() { return this.blockLeft; }
    public int getBurstLeft() { return this.burstLeft; }
    public int getWaitTime() { return this.waitTime; }
    public int getFinishTime() { return this.finishTime; }
    public int getIoTime() { return this.ioTime; }
    public int getTurnaroundTime() { return this.turnaroundTime; }
    public void setId(int id) { this.id = id; }
    public int getId() { return this.id; }
}
