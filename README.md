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
6. Bander Algorithms
7. Readers and Writers Problem
8. Deadlock
9. Multi-threaded and Multi-core chips.
10. Multiprogramming & Spooling & Time-sharing
11. Trap instruction
    The trap instruction, like a procedure call, is a synchronous transfer of control: We can see where, and hence when, it is executed. In this respect, there are no surprises. Although not surprising, the **trap** instruction does have an unusual effect: **processor execution** is switched from **user-mode** to **kernel-mode**. That is, the trap instruction normally is itself executed in user-mode (it is naturally an UNprivileged instruction), but the next instruction executed (which is NOT the instruction written after the trap) is executed in kernel-mode.
12. Daemon Process - A program that is not invoked explicitly, but lies dormant waiting for some condition(s) to occur. Wikipedia: In Unix and other computer multitasking operating systems, a daemon is a computer program that runs in the background, rather than under the direct control of a user; they are usually instantiated as processes. Typically daemons have names that end with the letter "d"; for example, syslogd is the daemon which handles the system log.
13. **kill()** (poorly named in my view) sends a signal to another process. For many types of signals, if the signal is not caught (via the signal() or sigaction() system call) the process is terminated. There is also an uncatchable signal.
14. **exit() system call** is used for self termination and can indicate success or failure.



One can organize an OS around the scheduler.  
- Write a minimal kernel (a micro-kernel) consisting of the scheduler, interrupt handlers, and IPC (interprocess communication).
- The rest of the OS consists of kernel processes (e.g. memory, filesystem) that act as servers for the user processes (which of course act as clients).
- The system processes also act as clients of other system processes.
- The above is called the client-server model and is one Tanenbaum likes. His Minix operating system works this way.
- Indeed, there was reason to believe that the client-server model would dominate OS design. But that hasn't happened, in large part due to performance considerations (process switching is expensive).
- Such an OS is sometimes called server based.
- Systems like traditional Unix or Linux are then called self-service since the user process serves itself. That is, the user process switches to kernel mode (via the TRAP instruction) and performs the system call itself without transferring control to another process.






