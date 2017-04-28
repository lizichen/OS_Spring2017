package HW2;

import java.util.ArrayList;

import static HW2.Utils.EMPTY_SPACE;

/**
 * Created by lizichen1 on 3/3/17.
 */
public class SchedulerHelper {

    public static String getProcessInformation(int numProcesses, ArrayList<Process_RR> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(numProcesses + EMPTY_SPACE);
        for (int i = 0; i < list.size(); i++) {
            Process_RR oneprocess = list.get(i);
            stringBuilder.append("(");
            stringBuilder.append(oneprocess.A + EMPTY_SPACE); stringBuilder.append(oneprocess.B + EMPTY_SPACE); stringBuilder.append(oneprocess.C + EMPTY_SPACE);
            stringBuilder.append(oneprocess.M + ")" + EMPTY_SPACE);
        }
        return stringBuilder.toString();
    }
}
