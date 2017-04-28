package HW4;


public class Simulator {

    int machineSize;
    int pageSize;
    int processSize;
    int jobMix;
    int numberOfReferences;
    String replacementAlgo;
    RandomNumberProvider randomNumberProvider;

    int numberOfFrames;

    Process[] processes = new Process[4]; // jobMix = 1, only care about processes[0], otherwise with processess[1], [2], [3]
    int numberOfProcessess;

    FrameTable frameTable;

    int time_counter = 0;

    public Simulator(int machineSize, int pageSize, int processSize, int jobMix, int numberOfReferences, String replacementAlgo) {
        this.machineSize = machineSize;
        this.pageSize = pageSize;
        this.processSize = processSize;
        this.jobMix = jobMix;
        this.numberOfProcessess = this.jobMix == 1 ? 1 : 4;
        this.numberOfReferences = numberOfReferences;
        this.replacementAlgo = replacementAlgo;
        this.randomNumberProvider = new RandomNumberProvider();

        this.numberOfFrames = this.machineSize / this.pageSize;

        this.frameTable = new FrameTable(numberOfFrames, this.randomNumberProvider);
    }


    public void start() {

        if(this.jobMix == 1){
            this.processes[0] = new Process(1, processSize, pageSize);
            this.runSingleProcessSimulator();
        }
        else if(this.jobMix == 2 || this.jobMix == 3 || this.jobMix == 4){

            this.processes[0] = new Process(1, processSize, pageSize);
            this.processes[1] = new Process(2, processSize, pageSize);
            this.processes[2] = new Process(3, processSize, pageSize);
            this.processes[3] = new Process(4, processSize, pageSize);

            this.runFourProcessesSimulator();
        }
    }

    /*J is 1 then fully sequential*/
    public void runSingleProcessSimulator() {
        int processId = 0;
        for(int i=1;i<=numberOfReferences;i++){
            int currentWord = processes[0].getCurrentWord();
            int currentPage = currentWord / pageSize;
            boolean hasPageFault = frameTable.hasPageFault(currentPage,processId,i);
            if(hasPageFault){
                Utils.log("Process #"+processId+" | word #"+currentWord+" | page #"+currentPage+" HAS FAULT!");
                frameTable.replaceFrameRow(currentPage, processId, i, this.processes, this.replacementAlgo);
                processes[0].incrementPageFaultOccurance();
            }else{
                Utils.log("Process #"+processId+" | word #"+currentWord+" | page #"+currentPage+" Hit the frame");
            }
            processes[0].getNextWord_singleProcess();
        }
    }

    private void runFourProcessesSimulator() {
        int quantum = 3;

        double A[] = new double[4];
        double B[] = new double[4];
        double C[] = new double[4];
        if(this.jobMix == 2){
            A[0] = 1;A[1] = 1;A[2] = 1;A[3] = 1;
            B[0] = 0;B[1] = 0;B[2] = 0;B[3] = 0;
            C[0] = 0;C[1] = 0;C[2] = 0;C[3] = 0;
        } else if(this.jobMix == 3){ //jobMix is 3:A = 0,B = 0, C = 0;
            A[0] = 0;A[1] = 0;A[2] = 0;A[3] = 0;
            B[0] = 0;B[1] = 0;B[2] = 0;B[3] = 0;
            C[0] = 0;C[1] = 0;C[2] = 0;C[3] = 0;
        } else if(this.jobMix == 4){ //jobMix is 4:A,B,C differs
            A[0] = 0.75;	B[0] = 0.25;  	C[0] = 0;
            A[1] = 0.75;	B[1] = 0;       C[1] = 0.25;
            A[2] = 0.75;	B[2] = 0.125;	C[2] = 0.125;
            A[3] = 0.5; 	B[3] = 0.125;	C[3] = 0.125;
        }

        for(int i=0;i <= this.numberOfReferences / quantum; i++){ // the over all numberOfReference
            for(int processId=0;processId<4;processId++){
                if(i == this.numberOfReferences / quantum){ // last run, 1 or 2 iterations
                    run_one_process(processId,A[processId],B[processId],C[processId],this.numberOfReferences % quantum, i);
                }else{
                    run_one_process(processId,A[processId],B[processId],C[processId],quantum, i);
                }
            }
        }
    }

    public void run_one_process(int processId, double A, double B, double C, int iteration, int cycle){

        for(int i=0; i < iteration ; i++){ // 0, 1, 2
            int currentPageNumber = this.processes[processId].getCurrentWord() / this.pageSize;
            int currentWord = this.processes[processId].getCurrentWord();
            if(this.frameTable.hasPageFault(currentPageNumber, processId, time_counter)){
                int frameUsed = this.frameTable.replaceFrameRow(currentPageNumber, processId, time_counter, this.processes, this.replacementAlgo);
                Utils.log(this.time_counter+"\t| Process #"+processId+" | word #"+currentWord+"\t| page #"+currentPageNumber+"| HAS FAULT! using free frame "+frameUsed);
                this.processes[processId].incrementPageFaultOccurance();
            }else{
                Utils.log(this.time_counter+"\t| Process #"+processId+" | word #"+currentWord+"\t| page #"+currentPageNumber+"| Hit the previous frame");
            }

            this.processes[processId].getNextWord_multiProcess(A, B, C, randomNumberProvider);

            this.time_counter++;
        }
    }

    public void printInfo() {

        System.out.println("The Machine Size is "+this.machineSize);
        System.out.println("The Page Size is "+this.pageSize);
        System.out.println("The Process Size is "+this.processSize);
        System.out.println("The Job Mix Number is "+this.jobMix);
        System.out.println("The Number Of References Per Process is "+this.numberOfReferences);
        System.out.println("The Replacement Algorithm is "+this.replacementAlgo);
        System.out.println("The Level of Debugging output is 0\n");

        int totalNumberOfFaults = 0;
        double overallAverageResidency = 0;
        int count = 0;

        if(this.jobMix == 1){
            Process p = this.processes[0];
            totalNumberOfFaults += p.numberOfPageFaultOccurance;
            if(p.evictOccurance == 0){
                System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults. \n\tWith no evictions, and average residency is undefined.");
            }else{
                System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults and " + (double) p.overallResidencyTime / p.evictOccurance + " average residency.");
                overallAverageResidency += p.overallResidencyTime;
                count += p.evictOccurance;
            }
        }
        else{
            for(Process p : this.processes){
                totalNumberOfFaults += p.numberOfPageFaultOccurance;
                if(p.evictOccurance == 0){
                    System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults.\n\t With no evctions, the average residency is undefined.");
                }else{
                    System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults and " + (double)p.overallResidencyTime / p.evictOccurance + " average residency.");
                    overallAverageResidency += (double) p.overallResidencyTime;
                    count += p.evictOccurance;
                }
            }
        }

        if(overallAverageResidency > 0){
            System.out.println("\nThe total number of faults is "+totalNumberOfFaults+" and the overall average residency is "+overallAverageResidency/count);
        }else{
            System.out.println("\nThe total number of faults is "+totalNumberOfFaults+".\n\tWith no evictions, the overall average residency is undefined.");
        }
    }
}
