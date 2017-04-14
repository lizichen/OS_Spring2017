package HW3;


import java.io.*;
import java.util.Hashtable;

import static HW3.Utils.INITIATE;

/**
 * Created by lizichen1 on 4/11/17.
 */
public class TaskFileReader {

    int numberOfTasks = 0;
    int typesOfResources = 0;
    int[] numberOfResourceUnit;
    Hashtable<Integer, Task> taskHashtable = new Hashtable<Integer, Task>();
    String inputFile;

    public TaskFileReader(String inputFile){
        this.inputFile = inputFile;
    }

    /**
     * Read in the inputFile and return all the detailed Tasks and Commands
     */
    public void readInput()  {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(this.inputFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.length() != 0){
                        if(Character.isDigit(line.charAt(0))){
                            String[] s = line.split("\\s+");
                            this.numberOfTasks =        Integer.valueOf(s[0]);
                            this.typesOfResources =     Integer.valueOf(s[1]);
                            this.numberOfResourceUnit = new int[Integer.valueOf(s[1])];
                            for(int i=2 ; i < 2+Integer.valueOf(s[1]) ; i++){
                                this.numberOfResourceUnit[i-2] = Integer.valueOf(s[i]);
                            }
                        }else{
                            String[] s = line.split("\\s+");
                            int taskID = Integer.valueOf(s[1]);

                            if(taskHashtable.get(taskID)==null){
                                //Create a Task
                                Task newTask = new Task();
                                newTask.addInitCommand(s[0], Integer.valueOf(s[1]), Integer.valueOf(s[2]), Integer.valueOf(s[3]), this.numberOfResourceUnit);
                                taskHashtable.put(taskID, newTask);
                            }else{
                                Task aTask = taskHashtable.get(taskID);
                                if(s[0].equals(INITIATE)){
                                    aTask.addInitCommand(s[0], Integer.valueOf(s[1]), Integer.valueOf(s[2]), Integer.valueOf(s[3]), this.numberOfResourceUnit);
                                }else{
                                    aTask.addCommand(s[0], Integer.valueOf(s[1]), Integer.valueOf(s[2]), Integer.valueOf(s[3]));
                                    taskHashtable.put(taskID, aTask);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
