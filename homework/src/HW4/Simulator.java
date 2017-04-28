package HW4;


import java.io.FileNotFoundException;

/**
 * Created by lizichen1 on 4/27/17.
 */
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
                System.out.println("Process #"+processId+" | word #"+currentWord+" | page #"+currentPage+" HAS FAULT!");
                frameTable.replaceFrameRow(currentPage, processId, i, this.processes, this.replacementAlgo);
                processes[0].incrementPageFaultOccurance();
            }else{
                System.out.println("Process #"+processId+" | word #"+currentWord+" | page #"+currentPage+" Hit the frame");
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
            for(int i=0;i<4;i++){
                A[i] = 1;
                B[i] = 0;
                C[i] = 0;
            }
        } else if(this.jobMix == 3){ //jobMix is 3:A = 0,B = 0, C = 0;
            for(int i=0;i<4;i++){
                A[i] = 0;
                B[i] = 0;
                C[i] = 0;
            }
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
                System.out.println("Process #"+processId+" | word #"+currentWord+" | page #"+currentPageNumber+" HAS FAULT!");
                this.frameTable.replaceFrameRow(currentPageNumber, processId, time_counter, this.processes, this.replacementAlgo);
                this.processes[processId].incrementPageFaultOccurance();
            }else{
                System.out.println("Process #"+processId+" | word #"+currentWord+" | page #"+currentPageNumber+" Hit the frame");
            }

            this.processes[processId].getNextWord_multiProcess(A, B, C, randomNumberProvider);

            this.time_counter++;
        }
    }

    public void printInfo() {

        if(this.jobMix == 1){
            Process p = this.processes[0];
            if(p.evictOccurance == 0){
                System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults and average residency is undefined.");
            }else{
                System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults and " + (double) p.overallResidencyTime / p.evictOccurance + " average residency.");
            }
        }
        else{
            for(Process p : this.processes){
                if(p.evictOccurance == 0){
                    System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults and average residency is undefined.");
                }else{
                    System.out.println("Process #" + p.processId + " had "+p.numberOfPageFaultOccurance + " faults and " + (double)p.overallResidencyTime / p.evictOccurance + " average residency.");
                }
            }
        }
    }
}
