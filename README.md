# Implementation of Operating System Functions

### 1. HW1 - Linkers
> Originally called a 'Linkage Editor' by IBM.

- A **linker** is an example of a utility program an os. 
- Like a compiler, the linker is not part of the operating system per se. Linker does not run in supervisor mode. 
- Unlike a compiler, it is OS dependent (what object/load file format is used) and is not (normally) language dependent.

- When the compiler and assembler have finished processing a module, they produce an object module that is almost runnable. There are two remaining tasks to be accomplished before object modules can be run. Both are involved with linking together multiple object modules. The tasks are *relocating relative addresses* and *resolving external references*.
- The output of a linker is called a load module because, with relative addresses relocated and the external addresses resolved, the module is ready to be loaded and run.

### 2. HW2 - Scheduler
A **Process** is characterized by four non-negative integers A, B, C, and M.
- A: Arrival Time of the Process
- C: Total CPU Time needed for the Process
- B: **CPU Burst time** is uniformly distributed random integers in the interval (0, B]
- M: The **I/O Burst Time** for a process is its preceding *CPU burst time* multiplied by **M**.
- The **CPU Time** is the time the process is in the **Running State**. It does NOT include the time in the Blocked State.

• Finishing time.
• Turnaround time (i.e., finishing time - A).
• I/O time (i.e., Total amount of time spent by the process in Blocked state.).
• Waiting time (i.e., time in Ready state).

### 3. HW3 - Banker's Algorithm
Implements resource allocation using optimistic resource manager and banker's algorithm of Dijkstra.

### 4. HW4 - Demand Paging  
Simulate demand paging and see how the number of page faults depends on page size, program size, replacement algorithm, and job mix.


![meme](https://github.com/lizichen/OS_Spring2017/blob/master/meme.jpg "meme")

### Mid-term Review:
1. Lecture 4: Numerical Scheduling Problems (the problem did in class just before section 2.4.4 (which is grayed out))
2. OS Levels:
    - Applications (e.g., web browser) and utilities (e.g., compiler, linker).
    - User interface (UI). It may be text oriented (Unix/Linux shell, DOS prompt) or graphical (GUI, e.g., MS Windows, Gnome/KDE, MAC).
    - Libraries (e.g., libc).
    - The OS proper (the kernel). Kernel runs in privileged/kernel/supervisor mode
    - Hardware.
3. A process is a program in execution.
4. Linker
5. Scheduling Schemes
6. Banker Algorithms
7. Readers and Writers Problem
8. Deadlock
9. Multi-threaded and Multi-core chips.
10. Multiprogramming & Spooling & Time-sharing
11. Trap instruction
    The trap instruction, like a procedure call, is a synchronous transfer of control: We can see where, and hence when, it is executed. In this respect, there are no surprises. Although not surprising, the **trap** instruction does have an unusual effect: **processor execution** is switched from **user-mode** to **kernel-mode**. That is, the trap instruction normally is itself executed in user-mode (it is naturally an UNprivileged instruction), but the next instruction executed (which is NOT the instruction written after the trap) is executed in kernel-mode.
12. Daemon Process - A program that is not invoked explicitly, but lies dormant waiting for some condition(s) to occur. Wikipedia: In Unix and other computer multitasking operating systems, a daemon is a computer program that runs in the background, rather than under the direct control of a user; they are usually instantiated as processes. Typically daemons have names that end with the letter "d"; for example, syslogd is the daemon which handles the system log.
13. **kill()** (poorly named in my view) sends a signal to another process. For many types of signals, if the signal is not caught (via the signal() or sigaction() system call) the process is terminated. There is also an uncatchable signal.
14. **exit() system call** is used for self termination and can indicate success or failure.
15. Two Threads in One Process vs Two Processes
    - Threads in the same process share memory; whereas separate processes do not.
    - Switching execution from one thread to another thread in the same process is much faster than switching execution from one process to another processes.
16. Producer and Consumer Pipeline
17. Page Fault?
18. Advantages and Disadvantages in implementing threads in user space.
19. Preemption:
    - A preemptive scheduler means the operating system can move a process from **Running** to **Ready** without the process requesting it.
    - Without preemption, the system implements run until completion, or block (or yield, if there is threading).
    - The preempt arc in the diagram is present for preemptive scheduling algorithms.
    - Preemption needs a clock interrupt (or equivalent).
    - Preemption is needed to guarantee fairness.
    - Preemption is found in all modern general purpose operating systems.
    - Preemption is expensive.
20. Scheduling in Batch Systems
    1. FCFS(First Come First Serve)
    2. **Shortest Job First (SJF)**
        + Non-preemptive algorithm
        + Can **starve** a process that requires a **long burst**.
    3. Uniprogrammed - monoprogrammed - Run to completion
    4. Shortest Remaining Time Next (Preemptive shortest job first)
        + Permit a process that enters the ready list to preempt the running process if the time for the new process is **less** than the remaining time for the running process.
21. Scheduling in Interactive Systems
    1. Round Robin (preemptive)
        + When a process is put into the **Running State**, a timer is set to **q** milliseconds; a key parameter of the policy, called the **quantum**. If the timer goes off and the process is still *running*, the OS **preempts** the process.
            * Preemption requires a **clock interrupt** so that **OS** can take control when the quantum expires.
            * The currently running process is moved to the **Ready State**, where it is placed at the **rear** of the **Ready List**.
            * The process at the front of the **Ready List** is popped from the list and run (i.e., moves to **Running State**).
22. **Selfish RR** (SRR) - SRR is a preemptive policy in which **unblocked (i.e. ready and running) processes** are divided into two classes the **"Accepted processes"**, which are scheduled using RR and the **"others"**, which are not run until they become **Accepted**.
    + A new process starts at priority 0.
    + Accepted process have their priority increase at rate **a**≥0.
    + A non-accepted process has its priority increases at rate **b**≥0.
    + A **non-accepted process** becomes **accepted** when its priority reaches that of the accepted processes *(or when there are no accepted processes and it has the highest priority of the unaccepted processes)*.
    + Hence, once a process is accepted, it remains accepted until it terminates.
    + When the only accepted process terminates (or blocks, see below), all the process with the next highest priority become accepted.
    + If **a = 0**, then it's regular RR.
    + If **a ≥ b > 0**, it's FCFS.
    + If **a > b =0**, you get **RR** in *batches*. 

###2.3 Interprocess Communication (IPC) and Coordination/Synchronization
###2.5 Classical IPC Problems
1. A **race condition** occurs when
    - Two processes A and B are each about to perform some (possibly different) action.
    - The program does not determine which process goes first.
    - The result if A goes first differs from the result if B goes first.
2. Tanenbaum gives four requirements for a **critical section** implementation.
    1. No two processes may be simultaneously inside their critical section.
    2. **No assumption** may be made about the speeds or the number of concurrent threads/processes.
    3. No process outside its critical section (i.e., executing 'ordinary' code) may block other processes.
    4. No process should have to **wait forever** to enter its critical section.


### To Look-up and Review:
- Section 2.3 that having multiple threads concurrently accessing the same memory can cause subtle bugs is programs that look too simple to be wrong.


### One can organize an OS around the scheduler.  
- Write a minimal kernel (a micro-kernel) consisting of the scheduler, interrupt handlers, and IPC (interprocess communication).
- The rest of the OS consists of kernel processes (e.g. memory, filesystem) that act as servers for the user processes (which of course act as clients).
- The system processes also act as clients of other system processes.
- The above is called the client-server model and is one Tanenbaum likes. His Minix operating system works this way.
- Indeed, there was reason to believe that the client-server model would dominate OS design. But that hasn't happened, in large part due to performance considerations (process switching is expensive).
- Such an OS is sometimes called server based.
- Systems like traditional Unix or Linux are then called self-service since the user process serves itself. That is, the user process switches to kernel mode (via the TRAP instruction) and performs the system call itself without transferring control to another process.






