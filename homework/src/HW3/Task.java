package HW3;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class Task {

    int id;
    int[] resouceTypeID;
    int[] initialclaim;
    ArrayList<Command> CommandList = new ArrayList<Command>();

    int[] holdingResource;
    int currentRunningCommandIndex = -1;

    boolean initialized = false;
    int initOrderID = 0;

    boolean FIFO_Aborted = false;

    int startCycle = 0;
    int endCycle = 0;
    int waitedCycle = 0;

    public void addInitCommand(String s, Integer integer, Integer integer1, Integer integer2, int[] numberOfResourceUnit) {
        if(initialized == false){
            this.resouceTypeID = new int[numberOfResourceUnit.length];
            this.initialclaim = new int[numberOfResourceUnit.length];
            this.holdingResource = new int[numberOfResourceUnit.length];
            initialized = true;
        }

        this.id = integer;
        this.resouceTypeID[this.initOrderID] = integer1;
        this.initialclaim[this.initOrderID] = integer2;
        this.holdingResource[this.initOrderID] = 0;

        this.initOrderID++;
    }

    public void addCommand(String s, int i1, int i2, int i3) {
        Command command = new Command(s, i1, i2, i3);
        CommandList.add(command);
    }

    public String toString(){
        StringBuilder tasksb = new StringBuilder();
        tasksb.append("TaskID:" + this.id + "\tResourceType:"+ Arrays.toString(this.resouceTypeID) +"\tInitialClaim:"+ Arrays.toString(this.initialclaim)+"\n");
        for (Command c : this.CommandList) {
            tasksb.append(c.commandType+" "+c.taskID+" "+c.resourceType+" "+c.numberOfResouceUnit+"\n");
        }
        return tasksb.toString();
    }

    public Command getCurrentCommand(){
        return this.CommandList.get(currentRunningCommandIndex);
    }

    public String printResult(){
        if(this.FIFO_Aborted == true){
            return "Task["+this.id+"]\taborted!";
        }else{
            int cyclelength = getCycleLength();
            return "Task[" + this.id + "]\t" + cyclelength + "\t" + this.waitedCycle + "\t" +  new DecimalFormat("#0").format((double)this.waitedCycle*100.0/cyclelength) +"%";
        }
    }

    public int getCycleLength(){
        return (this.endCycle-this.startCycle)+(this.initOrderID-1);
    }
}
