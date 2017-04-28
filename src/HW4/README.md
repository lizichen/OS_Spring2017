## Lab 4. Demand Paging
Github Repo: https://github.com/lizichen/OS_Spring2017/tree/master/homework/src/HW4

### Compile the Program:
```bash
    javac -sourcepath HW4/ HW4/*.java
```

### Run with input-XX.txt Commands:
```sh
    java HW4/Paging 10 10 20 1 10 lru 0
    java HW4/Paging 10 10 10 1 100 lru 0
    java HW4/Paging 20 10 10 2 10 random 0
    java HW4/Paging 90 10 40 4 100 lru 0
    ...
```

### Turn On Logs
In the Utils.java, set:
```java
        static boolean PRINT_LOG = true;
```
will enable printing logs.

### Structure of the Program:
- **Paging.java**
    - **Simulator.java** 
        - **Process.java** 
        - **FrameTable.java** 
            - **FrameRow.java**
    - **RandomNumberProvider.java** 
    - **Utils.java** 

### Input Data:

<table border="1">
  <tr>
    <th>Input</th>
    <th>Machine Size</th>
    <th>Page Size</th>
    <th>Size of Process</th>
    <th>Job Mix</th>
    <th>Number of Reference</th>
    <th>Replacement Algorithm (LIFO, Random, LRU)</th>
  </tr>
  <tr>
    <th>Input</th>
    <th>M</th>
    <th>P</th>
    <th>S</th>
    <th>J</th>
    <th>N</th>
    <th>R</th>
  </tr>
  <tr>
    <td>1</td>
    <td>10</td>
    <td>10</td>
    <td>20</td>
    <td>1</td>
    <td>10 </td>
    <td>lru</td>
  </tr>
  <tr>
      <td>2</td>
    <td>10 </td>
    <td>10 </td>
    <td>10</td>
    <td>1</td>
    <td>100</td>
    <td>lru</td>
  </tr>
  <tr>
      <td>3</td>
    <td>10</td>
    <td>10</td>
    <td>10</td>
    <td>2</td>
    <td>10</td>
    <td>lru</td>
  </tr>
  <tr>
      <td>4</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>2</td>
    <td>10</td>
    <td>lru</td>
  </tr>
  <tr>
      <td>5</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>2</td>
    <td>10</td>
    <td>random</td>
  </tr>
  <tr>
      <td>6</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>2</td>
    <td>10</td>
    <td>lifo</td>
  </tr>
  <tr>
      <td>7</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>3</td>
    <td>10</td>
    <td>lru</td>
  </tr>
  <tr>
      <td>8</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>3</td>
    <td>10</td>
    <td>lifo</td>
  </tr>
  <tr>
      <td>9</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>4</td>
    <td>10</td>
    <td>lru</td>
  </tr>
  <tr>
      <td>10</td>
    <td>20</td>
    <td>10</td>
    <td>10</td>
    <td>4</td>
    <td>10</td>
    <td>random</td>
  </tr>
  <tr>
      <td>11</td>
    <td>90</td>
    <td>10</td>
    <td>40</td>
    <td>4</td>
    <td>100</td>
    <td>lru</td>
  </tr>
  <tr>
  <td>12</td>
    <td>40</td>
    <td>10</td>
    <td>90</td>
    <td>1</td>
    <td>100</td>
    <td>lru</td>
  </tr>
  <tr>
  <td>13</td>
    <td>40</td>
    <td>10</td>
    <td>90</td>
    <td>1</td>
    <td>100</td>
    <td>lifo</td>
  </tr>
  <tr>
  <td>14</td>
    <td>800</td>
    <td>40</td>
    <td>400</td>
    <td>4</td>
    <td>5000</td>
    <td>lru</td>
  </tr>
  <tr>
  <td>15</td>
    <td>10</td>
    <td>5</td>
    <td>30</td>
    <td>4</td>
    <td>3</td>
    <td>random</td>
  </tr>
  <tr>
  <td>16</td>
    <td>1000</td>
    <td>40</td>
    <td>400</td>
    <td>4</td>
    <td>5000</td>
    <td>lifo</td>
  </tr>
</table>

