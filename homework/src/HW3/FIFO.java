package HW3;

import java.util.*;

import static HW3.Utils.log;
import static HW3.Utils.RELEASE;
import static HW3.Utils.REQUEST;
import static HW3.Utils.COMPUTE;
import static HW3.Utils.TERMINATE;

public class FIFO extends  ParentClassForBankerAndFIFO{

    public FIFO(Hashtable<Integer, Task> taskHashtable, int numberOfTasks, int typesOfResources, int[] numberOfResourceUnit) {

        super();

        taskList.add(new Task());//dummy task

        ArrayList l = new ArrayList(taskHashtable.keySet());
        for (int i = l.size()-1; i >= 0; i--) {
            taskList.add(taskHashtable.get(l.get(i)));
        }

        this.numberOfResourceUnit = numberOfResourceUnit;
        this.numberOfTasks = numberOfTasks;
    }

    /**
     * Start the work here
     */
    public void start() {
        ArrayList<Integer> visitTaskOrder = new ArrayList<>(); // This arraylist keeps the order of checking each command
        for (int i = 1; i <= numberOfTasks; i++) {
            visitTaskOrder.add(i);
        }

        log("====== Cycle (0 - 1) ======");
        cycleZeroWork();
        int cycle = 1;

        while (numberOfTerminatedTasks < this.numberOfTasks) {
            log("====== Cycle (" + cycle + " - " + (cycle + 1) + ") ======");

            ArrayList<Integer> failedRequests = new ArrayList();
            int[] releasedResourceUnit = new int[this.numberOfResourceUnit.length];
            boolean somethingHappenedInThisCycle = false;

            for (Integer i : visitTaskOrder) {
                Task t = taskList.get(i);
                if (t.id != -1 && t.endCycle == 0) {//DO NOT CARE ABOUT the dummy task
                    Command c = t.getCurrentCommand();
                    if (c.commandType.equals(REQUEST)) {
                        if (this.hasEnoughResourceUnitLeftForRequestCommand(c)) {
                            somethingHappenedInThisCycle = true;
                            log("Task #" + i + " is granted " + c.numberOfResouceUnit);
                            this.grantResourceForRequestCommand(c);
                            t.currentRunningCommandIndex++;
                            t.holdingResource[c.resourceType - 1] += c.numberOfResouceUnit;
                        } else {
                            log("Task #" + i + "\'s request for " + c.numberOfResouceUnit + " cannot be granted!");
                            t.waitedCycle++;
                            failedRequests.add(i);
                        }
                    } else if (c.commandType.equals(RELEASE)) {
                        somethingHappenedInThisCycle = processReleaseCommand(releasedResourceUnit, i, t, c);
                    } else if (c.commandType.equals(COMPUTE)) {
                        somethingHappenedInThisCycle = true;
                        c.resourceType--;
                        if (c.resourceType == 0) {
                            t.currentRunningCommandIndex++;
                        }
                    } else if (c.commandType.equals(TERMINATE)) {
                        log("Task #" + i + " terminates");
                        somethingHappenedInThisCycle = true;
                        t.endCycle = cycle;
                        resultTaskList.add(t);
                        this.numberOfTerminatedTasks++;
                    }
                }
            }

            //add the released resource
            updateReleasedResourceUnits(releasedResourceUnit);

            //Return resources so that at least one task can move
            if (!failedRequests.isEmpty() && !somethingHappenedInThisCycle) {
                returnResourceFromFailedRequests(failedRequests, cycle);
            }
            //reorder the visiting sequence:
            for (Integer failedRequestID : failedRequests) {
                visitTaskOrder.remove(failedRequestID);
            }
            for (Integer r : visitTaskOrder) {
                failedRequests.add(r);
            }
            visitTaskOrder = failedRequests;

            log("Resource Left: " + Arrays.toString(this.numberOfResourceUnit));
            cycle++;
        }
    }

    /**
     * Verify that there are enought ResourceUnit left for this command
     */
    public boolean hasEnoughResourceUnitLeftForRequestCommand(Command c){
        return c.numberOfResouceUnit <= this.numberOfResourceUnit[c.resourceType-1];
    }
}
