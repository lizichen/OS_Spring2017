package HW4;

/**
 * Created by lizichen1 on 4/27/17.
 */
public class Paging {
    public static void main(String[] args) {

//        args = new String[]{"10", "10", "20", "1", "10", "lru"}; //1
//        args = new String[]{"10", "10", "10", "1", "100", "lru"}; //2
//        args = new String[]{"10", "10", "10", "2", "10", "lru"}; //3
//        args = new String[]{"20", "10", "10", "2", "10", "lru"}; //4
//        args = new String[]{"20", "10", "10", "2", "10", "random"}; //5
//        args = new String[]{"20", "10", "10", "2", "10", "lifo"}; //6
//        args = new String[]{"20", "10", "10", "3", "10", "lru"}; //7
//        args = new String[]{"20", "10", "10", "3", "10", "lifo"}; //8
//        args = new String[]{"20", "10", "10", "4", "10", "lru"}; //9
//        args = new String[]{"20", "10", "10", "4", "10", "random"}; //10
//        args = new String[]{"90", "10", "40", "4", "100", "lru"}; //11
//        args = new String[]{"40", "10", "90", "1", "100", "lru"}; //12
//        args = new String[]{"40", "10", "90", "1", "100", "lifo"}; //13
//        args = new String[]{"800", "40", "400", "4", "5000", "lru"}; //14
//        args = new String[]{"10", "5", "30", "4", "3", "random"}; //15
//        args = new String[]{"1000", "40", "400", "4", "5000", "lifo"}; //16

        int machineSize = Integer.valueOf(args[0]);
        int pageSize = Integer.valueOf(args[1]);
        int processSize = Integer.valueOf(args[2]); // 0..S-1
        int jobMix = Integer.valueOf(args[3]);
        int numberOfReferences = Integer.valueOf(args[4]); // iteration for each process
        String replacementAlgo = args[5];

        Simulator newSimulator = new Simulator(machineSize, pageSize, processSize, jobMix, numberOfReferences, replacementAlgo);
        newSimulator.start();
        newSimulator.printInfo();

    }
}
