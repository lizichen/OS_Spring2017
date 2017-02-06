package HW1;

import java.util.ArrayList;

/**
 * @Title: Lab 1
 * @Course : Operating System Lab 1
 * @Date : Feb 9, 2017
 * @Author : Lizi Chen
 * @Email: lc3397@nyu.edu
 */
public class Module {

    // Base Address - starts from 0
    int baseAddr;

    // Definition List
    int numberOfDefinition;
    ArrayList<DefinitionUnit> definitionList;

    // Use List
    int numberOfUsage;
    ArrayList<UseUnit> useList;

    // Program Text
    int numberOfProgramText;
    ArrayList<ProgramTextUnit> programTextList;

    public Module(int newBaseAddr){
        this.baseAddr = newBaseAddr;
        this.numberOfDefinition = 0;
        this.numberOfUsage = 0;
        this.numberOfProgramText = 0;
        this.definitionList = new ArrayList<>();
        this.useList = new ArrayList<>();
        this.programTextList = new ArrayList<>();
    }

    public int getEndingAddress(){
        return this.baseAddr + this.numberOfProgramText;
    }

}
