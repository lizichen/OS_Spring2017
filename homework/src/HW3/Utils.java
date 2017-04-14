package HW3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by lizichen1 on 4/12/17.
 */
public class Utils {

    public final static boolean PRINT_LOG = false;
    public final static boolean PRINT_INFO = false;

    public static final String INITIATE = "initiate";
    public static final String REQUEST = "request";
    public static final String RELEASE = "release";
    public static final String COMPUTE = "compute";
    public static final String TERMINATE = "terminate";

    /**
     * After both processes are done, process fiforesult and bankerresult
     */
    public static void printResult(ArrayList<Task> fifoResult, ArrayList<Task> bankerResult, String inputfile) {
        int fifo_totalRunningTime = 0;
        int fifo_totalWaitingTime = 0;
        int banker_totalRunningTime = 0;
        int banker_totalWaitingTime = 0;

        System.out.println();

        System.out.println(inputfile);
        System.out.println("==== FIFO ================ BANKER'S ========");

        for(int i=0;i<fifoResult.size();i++){
            Task fifo = fifoResult.get(i);
            Task banker = bankerResult.get(i);
            if(fifo.FIFO_Aborted == false){
                fifo_totalRunningTime += fifo.getCycleLength();
                fifo_totalWaitingTime += fifo.waitedCycle;
            }
            if(banker.FIFO_Aborted == false){
                banker_totalRunningTime += banker.getCycleLength();
                banker_totalWaitingTime += banker.waitedCycle;
            }
            System.out.println(fifo.printResult() + "\t | \t" + banker.printResult());
        }

        System.out.print("Total:\t"+fifo_totalRunningTime+"\t"+fifo_totalWaitingTime+"\t"
                +new DecimalFormat("#0").format((double)fifo_totalWaitingTime*100.0/fifo_totalRunningTime)+"%");
        System.out.print("\t | \t" + "Total:\t"+banker_totalRunningTime+"\t"+banker_totalWaitingTime+"\t"
                +new DecimalFormat("#0").format((double)banker_totalWaitingTime*100.0/banker_totalRunningTime)+"%");

        System.out.println("\n============================================");
    }

    /**
     * Simple switch for printing the log
     */
    public static void log(String toprint){
        if(PRINT_LOG){
            System.out.println(toprint);
        }
    }

    /**
     * Simple switch for printing the log
     */
    public static void info(String toprint){
        if(PRINT_INFO){
            System.out.println(toprint);
        }
    }
}
