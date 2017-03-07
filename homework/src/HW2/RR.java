package HW2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RR {

    public RR(String inputString, int quantum){




    }

    public RR(boolean verbose, ArrayList<Process> processes, int quantum) {




        super(verbose, processes);
        setQuantum(processes);
    }

    private static void setQuantum(ArrayList<Process> list) {
        for (Process p : list) {
            p.setQuantumMax(2);
        }
    }

    protected void maintainReadyQueue() {
        ListIterator<Process> i = ready.listIterator();
        // LinkedList<Process> newReadyList = (LinkedList<Process>) ready.clone();
        while (i.hasNext()) {
            Process p = i.next();
            if (!readyQueue.contains(p)) readyQueue.addFirst(p);
        }
        i = readyQueue.listIterator();
        while (i.hasNext()) {
            Process p = i.next();
            if (ready.contains(p)) ready.remove(p);
        }
		/*
		for (Process p : ready) {
			readyQueue.addFirst(p);
		}
		*/
    }

    protected ArrayList<Process> sort(ArrayList<Process> processes) {
        Collections.sort(processes, new Comparator<Process>() {
            public int compare(Process a, Process b) {
                int arrivalA = a.getA();
                int arrivalB = b.getA();
                if (arrivalA > arrivalB) return 1;
                else if (arrivalA < arrivalB) return -1;
                else return 0;
            }
        });

        return processes;
    }

    public static void main(String[] args) throws IOException {

//        String filename = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";
//
//        String inputString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
//
//        int quantum = 2;
//
//        RR newscheduler = new RR(inputString, quantum);
//        System.out.println("The original input was: "+inputString);
//
//        newscheduler.sortProcessesByArrivalTime();
//        newscheduler.start();
//        newscheduler.printProcesses();


        String input = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt";

        try {
            File inFile = new File(input);
            Scanner scanner = new Scanner(inFile);
            String data = scanner.useDelimiter("\\Z").next();


            scanner.close();



            data = data.replaceAll("[()]", "");
            System.out.println(data);
            processList = new LinkedList<String>(Arrays.asList(data.split("\\s+")));

            numProcs = Integer.parseInt(processList.get(0));
            System.out.println("numProces: " + numProcs);
            processList.remove(0);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }



}
