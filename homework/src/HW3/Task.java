package HW3;

import java.util.ArrayList;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class Task {

    int id;
    int resouceTypeID;
    int initialclaim;
    ArrayList<Command> CommandList = new ArrayList<Command>();

    int holdingResource = 0;
    int currentRunningCommandIndex = -1;

    public void addCommand(String s, int i1, int i2, int i3) {
        if(s.equals("initiate")){
            this.id = i1;
            this.resouceTypeID = i2;
            this.initialclaim = i3;
        }else{
            Command command = new Command(s, i1, i2, i3);
            CommandList.add(command);
        }
    }

    public String toString(){
        StringBuilder tasksb = new StringBuilder();
        tasksb.append("TaskID:" + this.id + "\tResourceType:"+this.resouceTypeID +"\tInitialClaim:"+ this.initialclaim+"\n");
        for (Command c : this.CommandList) {
            tasksb.append(c.commandType+" "+c.taskID+" "+c.resourceType+" "+c.numberOfResouceUnit+"\n");
        }
        return tasksb.toString();
    }

    public Command getCurrentCommand(){
        return this.CommandList.get(currentRunningCommandIndex);
    }


}
