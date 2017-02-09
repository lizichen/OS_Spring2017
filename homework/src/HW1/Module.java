package HW1;

import java.util.ArrayList;

/**
 * @Title: Lab 1
 * @Course : Operating System Lab 1
 * @Date : Feb 9, 2017
 * @Author : Lizi Chen
 * @Email: lc3397@nyu.edu
 * @Github: https://github.com/lizichen/OS_Spring2017
 */
public class Module {

    // Base Address - starts from 0
    int baseAddr;

    // Use List
    int numberOfUsage;
    ArrayList<UseUnit> useList;

    // Program Text
    int numberOfProgramText;
    ArrayList<ProgramTextUnit> programTextList;

    public Module(int newBaseAddr){
        this.baseAddr = newBaseAddr;
        this.numberOfUsage = 0;
        this.numberOfProgramText = 0;
        this.useList = new ArrayList<>();
        this.programTextList = new ArrayList<>();
    }

    public int getEndingAddress(){
        return this.baseAddr + this.numberOfProgramText;
    }

}
