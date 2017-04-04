# OS Lab 2 : Scheduling

### Java File and Sample Data:
```bash
-- root/
    -- HW2/
        -- FCFS.java
        -- Process_FCFS.java
        
        -- RR.java
        -- Process_RR.java
        
        -- SJF.java
        -- Process_SJF.java
        
        -- Uniprogrammed.java
        
        -- Utils.java [Can change random-numbers directory here]
        -- ProcessComparator [To Sort the original processes]
        
    -- input_data/
        -- random-numbers
        -- input-1.txt
        -- input-2.txt
        -- etc
```

### Compile:
- Under root directory, compile all java file:
    ```bash
    javac -sourcepath HW2/ HW2/*.java
    ```
    
- Run the program with specific input.txt file:
    ```bash
    java HW2/RR /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt  
    
    or with --verbose:
    
    java HW2/RR --verbose /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt  
    ```
    
- FCFS  
```bash
    java HW2/FCFS /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt    
```
- RR with quantum 2.  
```bash
    java HW2/RR --verbose /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt    
```

- Uniprogrammed. Just one process active. When it is blocked, the system waits.  
```bash
    java HW2/Uniprogrammed --verbose /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt    
```

- SJF (This is not preemptive, but is not uniprogrammed, i.e., we do switch on I/O bursts). Recall that SJF is shortest
job first, not shortest burst first. So the time you use to determine priority is the total time remaining (i.e., the
input value C minus the number of cycles this process has run).
```bash
    java HW2/SJF --verbose /Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/input_data/input-6.txt    
```

#### TODO:
1. Refactor the Module Class for better data structure and time efficiency.
2. Refactor FCFS into the Scheduler