package HW3;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by lizichen1 on 4/12/17.
 */
public class Utils {

    public final static boolean PRINT_LOG = false;
    public final static boolean PRINT_INFO = false;

    public static void printResult(ArrayList<Task> resultsToPrint, String inputFileName, String method) {
        int totalRunningTime = 0;
        int totalWaitingTime = 0;

        System.out.println();
        System.out.println("======================= "+method+" =====================");
        System.out.println("=============Print Result "+ inputFileName+" =============");
        for(Task result_task:resultsToPrint){
            if (result_task.FIFO_Aborted == false) {
                totalRunningTime += result_task.getCycleLength();
                totalWaitingTime += result_task.waitedCycle;
            }
            System.out.println(result_task.printResult());
        }
        System.out.println("Total:\t"+totalRunningTime+"\t"+totalWaitingTime+"\t"
                +new DecimalFormat("#0").format((double)totalWaitingTime*100.0/totalRunningTime)+"%");

    }

    public static void log(String toprint){
        if(PRINT_LOG){
            System.out.println(toprint);
        }
    }

    public static void info(String toprint){
        if(PRINT_INFO){
            System.out.println(toprint);
        }
    }
}
