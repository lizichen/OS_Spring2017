# OS Lab 2 : Scheduling

### Java Files and Sample Data Files:
```bash
-- root/
    -- HW2/
        -- FCFS.java
        -- RR.java
        -- SJF.java
        -- Uniprogrammed.java
        
        -- ProcessState.java
        -- Process_FCFS.java
        -- Process_RR.java
        -- RR_Scheduler.java
        -- SchedulerHelper.java
        -- Utils.java [Can change random-numbers directory here]
        -- ProcessComparator.java [To Sort the original processes]
        -- RandomNumberProvider.java
        
    -- input_data/
        -- random-numbers
        -- input-1.txt
        -- input-2.txt
        -- ...
        -- input-7.txt
        -- input-test.txt
```

### Compile:
- Under **root** directory, compile all java file:
    ```bash
    javac -sourcepath HW2/ HW2/*.java
    ```

### Run: 
**Please always run under the root directory, otherwise, you may have to change the random-number path in the RandomNumberProvider.java file** 
   
- **RR** with quantum 2.  
```bash
    java HW2/RR ./HW2/input_data/input-6.txt
    java HW2/RR --verbose ./HW2/input_data/input-6.txt    
```

- **FCFS**  
```bash
    java HW2/FCFS ./HW2/input_data/input-6.txt    
```

- **Uniprogrammed**. Just one process active. When it is blocked, the system waits.  
```bash
    java HW2/Uniprogrammed ./HW2/input_data/input-6.txt
    java HW2/Uniprogrammed --verbose ./HW2/input_data/input-6.txt
```

- **SJF** (This is not preemptive, but is not uniprogrammed, i.e., we do switch on I/O bursts). Recall that SJF is shortest
job first, not shortest burst first. So the time you use to determine priority is the total time remaining (i.e., the
input value C minus the number of cycles this process has run).
```bash
    java HW2/SJF ./HW2/input_data/input-6.txt
    java HW2/SJF --verbose ./HW2/input_data/input-6.txt
```
