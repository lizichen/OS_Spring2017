package HW3;

import java.util.*;

import static HW3.Utils.log;
import static HW3.Utils.info;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class Banker {

    private int numberOfTasks;
    private int typesOfResources;
    private int[] numberOfResourceUnit;

    private ArrayList<Task> taskList = new ArrayList<Task>();
    private ArrayList<Task> resultTaskList = new ArrayList<Task>();

    private int numberOfTerminatedTasks = 0;

    private int number_of_pre_aborted_task = 0;

    public Banker(Hashtable<Integer, Task> taskHashtable, int numberOfTasks, int typesOfReousces, int[] numberOfResourceUnit) {

        taskList.add(new Task());//dummy task

        this.numberOfResourceUnit = numberOfResourceUnit;
        this.numberOfTasks = numberOfTasks;
        this.typesOfResources = typesOfResources;

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

    public void start() {
        ArrayList<Integer> visitTaskOrder = new ArrayList<>();
        for(int i=1;i<=numberOfTasks;i++){
            visitTaskOrder.add(i);
        }

        log("====== Cycle (0 - 1) ======");
        this.cycleZeroWork();
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

                    if(c.commandType.equals("request")){
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
                    }else if(c.commandType.equals("release")){
                        somethingHappenedInThisCycle = true;
                        log("Task #"+i+" releases "+c.numberOfResouceUnit);
//                            this.numberOfResourceUnit[c.resourceType-1]+=c.numberOfResouceUnit;
                        releasedResourceUnit[c.resourceType-1]+=c.numberOfResouceUnit;
                        t.holdingResource[c.resourceType-1] -= c.numberOfResouceUnit;
                        t.currentRunningCommandIndex++;
                    }else if(c.commandType.equals("compute")){
                        somethingHappenedInThisCycle = true;
                        c.resourceType--;
                        if(c.resourceType==0){
                            t.currentRunningCommandIndex++;
                        }
                    }else if(c.commandType.equals("terminate")){
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
            info("\tTask 1 HoldingResource:"+Arrays.toString(this.taskList.get(1).holdingResource));
            info("\tTask 2 HoldingResource:"+Arrays.toString(this.taskList.get(2).holdingResource));
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


    private void updateReleasedResourceUnits(int[] releasedResourceUnit) {
        for(int i=0;i<this.numberOfResourceUnit.length;i++){
            this.numberOfResourceUnit[i] += releasedResourceUnit[i];
        }
    }


    private void returnResourceFromFailedRequests(ArrayList<Integer> failedRequests, int cycle) {
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

            log("@@@ Task #"+failedRequestTaskID+" aborted and released "+Arrays.toString(t.holdingResource)+" @@@");

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


    public void grantResourceForRequestCommand(Command c){
        int leftResourceUnit = this.numberOfResourceUnit[c.resourceType-1];
        this.numberOfResourceUnit[c.resourceType-1] = leftResourceUnit - c.numberOfResouceUnit;
    }

    private void cycleZeroWork() {
        log("initiating...");
        for (Task t:taskList) {
            t.currentRunningCommandIndex++;
            t.startCycle = 0;
        }
    }

    public ArrayList<Task> getResultTaskList(){
        Collections.sort(this.resultTaskList, new Comparator<Task>(){
            public int compare(Task o1, Task o2){
                return o1.id < o2.id ? -1 : 1;
            }
        });

        return this.resultTaskList;
    }
}
