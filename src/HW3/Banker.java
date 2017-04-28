package HW3;

import java.util.*;

import static HW3.Utils.log;
import static HW3.Utils.info;

import static HW3.Utils.RELEASE;
import static HW3.Utils.REQUEST;
import static HW3.Utils.COMPUTE;
import static HW3.Utils.TERMINATE;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class Banker extends ParentClassForBankerAndFIFO {

    private int number_of_pre_aborted_task = 0;

    public Banker(Hashtable<Integer, Task> taskHashtable, int numberOfTasks, int typesOfReousces, int[] numberOfResourceUnit) {

        super();

        taskList.add(new Task());//dummy task

        this.numberOfResourceUnit = numberOfResourceUnit;
        this.numberOfTasks = numberOfTasks;

        ArrayList l = new ArrayList(taskHashtable.keySet());
        for (int i = l.size()-1; i >= 0; i--) {
            Task t = taskHashtable.get(l.get(i));
            for(int j = 0;j<typesOfReousces;j++){
                if(t.initialclaim[j] > numberOfResourceUnit[j]){
                    t.endCycle = -1;//Abort this useless one
                    t.FIFO_Aborted = true;
                    number_of_pre_aborted_task++;
                    this.resultTaskList.add(t);
                    break;
                }
            }
            taskList.add(t);
        }
    }

    /**
     * Stat the Banker's
     */
    public void start() {
        ArrayList<Integer> visitTaskOrder = new ArrayList<>(); // This arraylist keeps the order of checking each command
        for(int i=1;i<=numberOfTasks;i++){
            visitTaskOrder.add(i);
        }

        log("====== Cycle (0 - 1) ======");
        cycleZeroWork();
        int cycle = 1;

        while(numberOfTerminatedTasks < this.numberOfTasks-this.number_of_pre_aborted_task){
            log("====== Cycle ("+cycle+" - " + (cycle+1) + ") ======");
            ArrayList<Integer> failedRequests = new ArrayList();

            int[] releasedResourceUnit = new int[this.numberOfResourceUnit.length];
            boolean somethingHappenedInThisCycle = false;

            for(Integer i:visitTaskOrder){
                Task t = taskList.get(i);
                if(t.id!=-1 && t.endCycle==0) {//DO NOT CARE ABOUT the dummy task
                    Command c = t.getCurrentCommand();

                    if(c.commandType.equals(REQUEST)){
                        if(c.numberOfResouceUnit + t.holdingResource[c.resourceType-1] > t.initialclaim[c.resourceType-1]){
                            //abort this task!
                            t.FIFO_Aborted = true;
                            t.endCycle = -1;
                            somethingHappenedInThisCycle = true;
                            numberOfTerminatedTasks++;
                            resultTaskList.add(t);
                            log("Task #"+i+" is aborted!");
                            releasedResourceUnit[c.resourceType-1]+=c.numberOfResouceUnit;
                        }
                        else if(this.safeToProcessCommand(c, t)){
                            somethingHappenedInThisCycle = true;
                            log("Task #"+i+" is granted "+c.numberOfResouceUnit);
                            this.grantResourceForRequestCommand(c);
                            t.currentRunningCommandIndex++;
                            t.holdingResource[c.resourceType-1]+=c.numberOfResouceUnit;
                        }else{
                            log("Task #"+i+"\'s request for "+c.numberOfResouceUnit+" cannot be granted!");
                            t.waitedCycle++;
                            failedRequests.add(i);
                        }
                    }else if(c.commandType.equals(RELEASE)){
                        somethingHappenedInThisCycle = processReleaseCommand(releasedResourceUnit, i, t, c);
                    }else if(c.commandType.equals(COMPUTE)){
                        somethingHappenedInThisCycle = true;
                        c.resourceType--;
                        if(c.resourceType==0){
                            t.currentRunningCommandIndex++;
                        }
                    }else if(c.commandType.equals(TERMINATE)){
                        log("Task #"+i+" terminates");
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
            if(!failedRequests.isEmpty() && !somethingHappenedInThisCycle){
                returnResourceFromFailedRequests(failedRequests, cycle);
            }
            //reorder the visiting sequence:
            for(Integer failedRequestID:failedRequests){
                visitTaskOrder.remove(failedRequestID);
            }
            for(Integer r:visitTaskOrder){
                failedRequests.add(r);
            }
            visitTaskOrder = failedRequests;

            info("Resource Left: "+ Arrays.toString(this.numberOfResourceUnit));
            cycle++;
        }
    }



    private boolean safeToProcessCommand(Command c, Task t) {
        // for all resource types, the number of free units is at least equal to the max additional need of the process
        boolean allsatisfied = false;

        for(int i=0;i<t.resouceTypeID.length;i++){
            int remainingAvailableResouceUnit = this.numberOfResourceUnit[i];
            int remainingNeededResourceUnitForTheTask = t.initialclaim[i] - t.holdingResource[i];

            if(remainingAvailableResouceUnit >= remainingNeededResourceUnitForTheTask){
                allsatisfied = true;
            }else{
                allsatisfied = false;
                break;
            }

            if(c.resourceType-1 == i){
                if(c.numberOfResouceUnit <= remainingAvailableResouceUnit && remainingNeededResourceUnitForTheTask <= remainingAvailableResouceUnit){
                    info("Can Grant Resouce!");
                    allsatisfied = true;
                }else{
                    return false;
                }
            }
        }

        return allsatisfied;
    }


}
