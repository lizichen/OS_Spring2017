package HW3;


import java.util.*;

public class FIFO {
    private int numberOfTasks;
    private int typesOfResources;
    private int[] numberOfResourceUnit;
    private ArrayList<Task> taskList = new ArrayList<Task>();

    private int numberOfTerminatedTasks = 0;

    public FIFO(Hashtable<Integer, Task> taskHashtable, int numberOfTasks, int typesOfResources, int[] numberOfResourceUnit) {
        taskList.add(new Task());//dummy task

        ArrayList l = new ArrayList(taskHashtable.keySet());
        for (int i = l.size()-1; i >= 0; i--) {
            taskList.add(taskHashtable.get(l.get(i)));
        }

        this.numberOfResourceUnit = numberOfResourceUnit;
        this.numberOfTasks = numberOfTasks;
        this.typesOfResources = typesOfResources;
    }

    public void start(){

        int cycle = 0;

        ArrayList<Integer> visitTaskOrder = new ArrayList<>();
        for(int i=1;i<=numberOfTasks;i++){
            visitTaskOrder.add(i);
        }

        while(numberOfTerminatedTasks < this.numberOfTasks){
            log("==== Cycle: "+cycle+"====");

            ArrayList<Integer> failedRequests = new ArrayList();
            int[] releasedResourceUnit = new int[this.numberOfResourceUnit.length];
            boolean somethingHappenedInThisCycle = false;

            if(cycle == 0){
                this.cycleZeroWork();
            }else{
                //for(Integer i:visitTaskOrder)
                //for(int i=1;i<=numberOfTasks;i++)
                //Loop though all tasks in each cycle
                for(Integer i:visitTaskOrder){
                    Task t = taskList.get(i);
                    if(t.id!=-1){//DO NOT CARE ABOUT the dummy task
                        Command c = t.getCurrentCommand();
                        if(c.commandType.equals("request")){
                            if(this.hasEnoughResourceUnitLeftForRequestCommand(c)){
                                somethingHappenedInThisCycle = true;
                                log("Task #"+i+" is granted "+c.numberOfResouceUnit);
                                this.grantResourceForRequestCommand(c);
                                t.currentRunningCommandIndex++;
                                t.holdingResource+=c.numberOfResouceUnit;
                            }else{
                                log("Task #"+i+"\'s request for "+c.numberOfResouceUnit+" cannot be granted!");
                                failedRequests.add(i);
                            }
                        }else if(c.commandType.equals("release")){
                            somethingHappenedInThisCycle = true;
                            log("Task #"+i+" releases "+c.numberOfResouceUnit);
//                            this.numberOfResourceUnit[c.resourceType-1]+=c.numberOfResouceUnit;
                            releasedResourceUnit[c.resourceType-1]+=c.numberOfResouceUnit;
                            t.holdingResource -= c.numberOfResouceUnit;
                            t.currentRunningCommandIndex++;
                        }else if(c.commandType.equals("compute")){
                            somethingHappenedInThisCycle = true;
                            //TODO: Next Step!
                        }else if(c.commandType.equals("terminate")){
                            log("Task #"+i+" terminates");
                            somethingHappenedInThisCycle = true;
                            t.id = -1;
                            this.numberOfTerminatedTasks++;
                        }
                    }
                }

                //add the released resource
                for(int i=0;i<this.numberOfResourceUnit.length;i++){
                    this.numberOfResourceUnit[i] += releasedResourceUnit[i];
                }
                //Return resources so that at least one task can move
                if(!failedRequests.isEmpty() && !somethingHappenedInThisCycle){
                    returnResourceFromFailedRequests(failedRequests);
                }
                //reorder the visiting sequence:
                for(Integer failedRequestID:failedRequests){
                    visitTaskOrder.remove(failedRequestID);
                }
                for(Integer r:visitTaskOrder){
                    failedRequests.add(r);
                }
                visitTaskOrder = failedRequests;
            }
            log("Resource Left: "+ Arrays.toString(this.numberOfResourceUnit));
            cycle++;
        }
        log("Complete!");

    }

    private void returnResourceFromFailedRequests(ArrayList<Integer> failedRequests) {
        int numberOfTasksToBeAborted = 0;

        Collections.sort(failedRequests); //FROM small index

        for(int i=0;i<failedRequests.size();i++){

            int failedRequestTaskID = failedRequests.get(i);
            Task t = taskList.get(failedRequestTaskID);
            this.numberOfResourceUnit[t.resouceTypeID-1] += t.holdingResource;

            Task dummyTask = new Task();
            dummyTask.id = -1;
            taskList.set(failedRequestTaskID, dummyTask);
            numberOfTasksToBeAborted++;

            log("@@@ Task #"+failedRequestTaskID+" aborted and released "+t.holdingResource+" @@@");

            if(failedRequests.get(i+1)!=null){
                int nextFailedRequestTaskID = failedRequests.get(i+1);
                Task t1 = taskList.get(nextFailedRequestTaskID);
                if(this.numberOfResourceUnit[t1.resouceTypeID-1] >= t1.getCurrentCommand().numberOfResouceUnit){
                    break;// have enough resource!
                }
            }
        }

        this.numberOfTerminatedTasks += numberOfTasksToBeAborted;
    }


    public boolean hasEnoughResourceUnitLeftForRequestCommand(Command c){
        return c.numberOfResouceUnit <= this.numberOfResourceUnit[c.resourceType-1];
    }

    public void grantResourceForRequestCommand(Command c){
        int leftResourceUnit = this.numberOfResourceUnit[c.resourceType-1];
        this.numberOfResourceUnit[c.resourceType-1] = leftResourceUnit - c.numberOfResouceUnit;
    }

    public void cycleZeroWork(){
        log("initiating...");
        for (Task t:taskList) {
            t.currentRunningCommandIndex++;
        }
    }

    public static void log(String log){
        System.out.println(log);
    }

}
