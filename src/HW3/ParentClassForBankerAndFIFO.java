package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static HW3.Utils.log;

/**
 * Created by lizichen1 on 4/13/17.
 *
 * This is the abstract class that keeps the common functions for both Banker's and FIFO.
 */
public abstract class ParentClassForBankerAndFIFO {

    public int numberOfTasks;
    public int[] numberOfResourceUnit;

    public ArrayList<Task> taskList = new ArrayList<Task>();
    public ArrayList<Task> resultTaskList = new ArrayList<Task>();

    public int numberOfTerminatedTasks = 0;

    public ParentClassForBankerAndFIFO(){
    }

    public boolean processReleaseCommand(int[] releasedResourceUnit, Integer i, Task t, Command c) {
        boolean somethingHappenedInThisCycle;
        somethingHappenedInThisCycle = true;
        log("Task #"+i+" releases "+c.numberOfResouceUnit);
        releasedResourceUnit[c.resourceType-1]+=c.numberOfResouceUnit;
        t.holdingResource[c.resourceType-1] -= c.numberOfResouceUnit;
        t.currentRunningCommandIndex++;
        return somethingHappenedInThisCycle;
    }

    public void returnResourceFromFailedRequests(ArrayList<Integer> failedRequests, int cycle) {
        int numberOfTasksToBeAborted = 0;

        Collections.sort(failedRequests); //FROM small index

        for(int i=0;i<failedRequests.size();i++){

            int failedRequestTaskID = failedRequests.get(i);
            Task t = taskList.get(failedRequestTaskID);

            t.FIFO_Aborted = true;
            t.endCycle = cycle;
            this.resultTaskList.add(t);

            for(int index = 0;index<t.resouceTypeID.length;index++){
                int return_resourceType = t.resouceTypeID[index];
                this.numberOfResourceUnit[return_resourceType-1] += t.holdingResource[return_resourceType-1];
            }

            Task dummyTask = new Task();
            dummyTask.id = -1;
            taskList.set(failedRequestTaskID, dummyTask);
            numberOfTasksToBeAborted++;

            log("@@@ Task #"+failedRequestTaskID+" aborted and released "+ Arrays.toString(t.holdingResource)+" @@@");

            if(failedRequests.get(i+1)!=null){
                int nextFailedRequestTaskID = failedRequests.get(i+1);
                Task t1 = taskList.get(nextFailedRequestTaskID);
                int t1_resourceType = t1.getCurrentCommand().resourceType;
                if(this.numberOfResourceUnit[t1.resouceTypeID[t1_resourceType-1]-1] >= t1.getCurrentCommand().numberOfResouceUnit){
                    break;// have enough resource!
                }
            }
        }

        this.numberOfTerminatedTasks += numberOfTasksToBeAborted;
    }

    public void cycleZeroWork(){
        log("initiating...");
        for (Task t:taskList) {
            t.currentRunningCommandIndex++;
            t.startCycle = 0;
        }
    }

    public void updateReleasedResourceUnits(int[] releasedResourceUnit) {
        for(int i=0;i<this.numberOfResourceUnit.length;i++){
            this.numberOfResourceUnit[i] += releasedResourceUnit[i];
        }
    }

    public void grantResourceForRequestCommand(Command c){
        int leftResourceUnit = this.numberOfResourceUnit[c.resourceType-1];
        this.numberOfResourceUnit[c.resourceType-1] = leftResourceUnit - c.numberOfResouceUnit;
    }

    public ArrayList<Task> getResultTaskList(){
        Collections.sort(this.resultTaskList, new Comparator<Task>(){
            public int compare(Task o1, Task o2){
                return o1.id < o2.id ? -1 : 1;
            }
        });

        return this.resultTaskList;
    }

    public abstract void start();

}
