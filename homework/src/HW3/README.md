## Lab 3. Banker's Algorithms
Github Repo: https://github.com/lizichen/OS_Spring2017/tree/master/homework/src/HW3 

### Compile the Program:
```bash
    javac -sourcepath HW3/ HW3/*.java
```

### Run with input-XX.txt Commands:
```sh
    java HW3/HW3 input-01.txt
    java HW3/HW3 input-10.txt
    java HW3/HW3 input-13.txt
```

### Structure of the Program:
- **HW3.java**
    - **TaskFileReader.java** (Read input-XX.txt file and construct objects)
        - **Task.java** (Banker and FIFO both have a list of Task objects)
        - **Command.java** (A Task has a list of Command objects)

    - **ParentClassForBankerAndFIFO.java** (Abstract Class)
        - **FIFO.java**
        - **Banker.java**

- **Utils.java** (Many generic functions, switches, and constants)







