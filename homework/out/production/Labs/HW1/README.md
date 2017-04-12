# OS Lab 1 : Linker

### Java File and Sample Data:
```bash
-- root/
    -- HW1/
        -- TwoPassLinker.java
        -- Module.java
        -- DefinitionUnit.java
        -- UseUnit.java
        -- ProgramTextUnit.java
        -- README.md
    -- SampleData/
        -- inputs/
            -- input-1.txt
            -- input-2.txt
            -- etc
```
### Run the following command to compile:
- Under root directory, compile all java file:
    ```bash
    javac -sourcepath HW1/ HW1/*.java
    ```
    
- Run the program with specific input.txt file:
    ```bash
    java HW1/TwoPassLinker SampleData/inputs/input-1.txt
    ```
    
    or any directory that has the input .txt file:
    ```bash
    java HW1/TwoPassLinker yourDir/input-1.txt
    ```

### Sample output format:
```bash
lc3397@linax1[src]$ java HW1/TwoPassLinker SampleData/inputs/input-4.txt
    Symbol Table
    X21 = 3

    Memory Map
    0:	1003
    1:	1003
    2:	1003
    3:	2000
    4:	3456

    X21 Error: This variable is multiply defined; first value used.
    Error: Module 1 has instruction: [A 2456] that has absolute address exceeds machine size; zero used.
```


#### TODO:
Refactor the Module Class for better data structure and time efficiency.