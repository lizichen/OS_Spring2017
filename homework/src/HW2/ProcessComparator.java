package HW2;

import java.util.Comparator;

/**
 * Created by lizichen1 on 4/2/17.
 */
public class ProcessComparator implements Comparator<Process_RR> {
    @Override
    public int compare(Process_RR process_A, Process_RR process_B) {
        if (process_A.A > process_B.A)
            return 1;
        else if (process_A.A < process_B.A)
            return -1;
        else
            return 0;
    }
}
