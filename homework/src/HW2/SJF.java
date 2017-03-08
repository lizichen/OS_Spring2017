package HW2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * Created by lizichen1 on 3/8/17.
 */
public class SJF extends RR_Scheduler{


    public SJF(boolean verbose, ArrayList<Process_RR> processes, int quantum) {
        super(verbose, processes);
        this.name = "Shortest Job First";
        this.quantum = quantum;
        setQuantum(processes);
    }

    private static void setQuantum(ArrayList<Process_RR> list) {
        for (Process_RR p : list) {
            p.setQuantumMax(1000);
        }
    }

    protected void maintainReadyQueue() {
        Process_RR p = new Process_RR(0,0,0,0);
        p.setTimeLeft(10000);
        boolean shouldStop = true;
        for (Process_RR temp : ready) {
            if (terminated.contains(temp)) continue;
            if (temp.getBurstLeft() > 0) {
                return;
            }
            // System.out.printf("Process_FCFS %d timeLeft: %d", temp.getId(), temp.timeLeft);
            if (temp.timeLeft < p.timeLeft) {
                p = temp;
                shouldStop = false;
            }
        }
        if (shouldStop) return;

        // Now that we have the shortest ready process, set it to the ready queue
        // Only if it is not yet terminated, not blocked, and not running
        if (!readyQueue.contains(p) && !blockedList.contains(p) && p.getState() != 2) readyQueue.addFirst(p);
        // dPrint();

        ListIterator<Process_RR> i = ready.listIterator();
    }

    protected ArrayList<Process_RR> sort(ArrayList<Process_RR> processes) {
        Collections.sort(processes, new Comparator<Process_RR>() {
            public int compare(Process_RR a, Process_RR b) {
                int arrivalA = a.getA();
                int arrivalB = b.getA();
                if (arrivalA > arrivalB) return 1;
                else if (arrivalA < arrivalB) return -1;
                else return 0;
            }
        });
        return processes;
    }

    public static void main(String[] args) {

    }

}
