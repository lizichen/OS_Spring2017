package HW2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lizichen1 on 3/8/17.
 */
public class Uniprogrammed extends RR_Scheduler {

    //(allprocess, quantum, verbose, numberOfProcesses);
    public Uniprogrammed( ArrayList<Process_RR> processes, boolean verbose, int numberOfProcesses) throws IOException {
        super(verbose, processes);
    }

    protected void updateReadyQueue() {
        if (numProcesses == terminatedProcess.size()){
            System.out.println("Scheudling Completes!");
        }else{
            Process_RR p = new Process_RR(0,0,0,0);

            Iterator<Process_RR> iter = processes.iterator();
            while(iter.hasNext()){
                Process_RR o = iter.next();
                if(!terminatedProcess.contains(o)){
                    p = o;
                    break;
                }
            }

            addFirstReadyQueue(p);
        }
    }

    public static void main(String[] args) {
        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";
        boolean verbose = true;

        // java RR --verbose input-6.txt
        // java RR input-6.txt
        if(args.length == 2){
            if(args[0].equals("--verbose")){
                verbose = true;
                input = args[1];
            }
            else{
                verbose = false;
                System.out.println("Please type\n java RR --verbose input.txt or java RR input.txt");
                System.exit(-1);
            }
        }else if(args.length == 1){
            input = args[0];
            verbose = false;
        }

        try {
            ArrayList<Process_RR> allprocess = Utils.fromInputFileToListOfProcesses(input);
            int numberOfProcesses = Utils.getNumberOfProcess(input);

            Uniprogrammed uni = new Uniprogrammed(allprocess, verbose, numberOfProcesses);

            System.out.println(Utils.THE_ORIGINAL_INPUT_WAS + uni.originalProcessesDetailString);
            System.out.println(Utils.THE_SORTED_INPUT_WAS + uni.sortedProcesses);
            System.out.println();

            while (uni.terminatedProcess.size() < uni.numProcesses) {
                uni.cycle();
            }
            System.out.println();
            uni.printFinalSummary();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
