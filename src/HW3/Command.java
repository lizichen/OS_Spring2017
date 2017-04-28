package HW3;

/**
 * Created by lizichen1 on 4/12/17.
 */
public class Command {

    String commandType; // initiate, request, release, compute, terminate
    int taskID;
    int resourceType; // number of compute cycles when commandType is 'compute'
    int numberOfResouceUnit;

    public Command(String s, int i1, int i2, int i3) {
        this.commandType = s;
        this.taskID = i1;
        this.resourceType = i2;
        this.numberOfResouceUnit = i3;
    }
}
