
- FCFS  

- RR with quantum 2.  

- Uniprogrammed. Just one process active. When it is blocked, the system waits.  

- SJF (This is not preemptive, but is not uniprogrammed, i.e., we do switch on I/O bursts). Recall that SJF is shortest
job first, not shortest burst first. So the time you use to determine priority is the total time remaining (i.e., the
input value C minus the number of cycles this process has run).