package HW2;

import java.util.Comparator;
/**
 * Created by lizichen1 on 3/1/17.
 */
public class ProcessComparator implements Comparator<Process_RR> {
    @Override
    public int compare(Process_RR process_A, Process_RR process_B) {

        int a = process_A.A;
        int b = process_B.A;

        if (process_A.A > process_B.A){
            return 1;
        }
        else if (process_A.A < process_B.A) {
            return -1;
        }
        else if ( (a^b) == 0 ){
            return 0;
        }
        else{
            return 0;
        }

    }
}
