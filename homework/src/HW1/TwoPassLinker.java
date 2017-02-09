package HW1;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Set;

/*
1 xy 2 2 z xy 5 R 1004 I 5678 E 2000 R 8002 E 7001 0 1 z 6 R 8001 E 1000 E 1000 E 3000 R 1002 A 1010 0 1 z 2 R 5001 E 4000 1 z 2 2 xy z 3 A 8000 E 1001 E 2000
 */

/**
 * @Title: Lab 1
 * @Course : Operating System
 * @Date : Feb 9, 2017
 * @Author : Lizi Chen
 * @Email: lc3397@nyu.edu
 */
public class TwoPassLinker {
    private static final String DUPLICATION_ERROR = " Error: This variable is multiply defined; first value used.";
    private static final String MISS_DEFINE_ERROR = " Error: This variable is NOT defined; zero used.";

    private static final String FILE_DIR = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/Two-Pass-Linker-master/inputs/";
    private String inputString;
    private String[] tokens;

    private ArrayList<DefinitionUnit> definitionList = new ArrayList<>();
    private Hashtable definitionTable = new Hashtable();
    private ArrayList<Module> moduleList = new ArrayList<>();

    private ArrayList<String> errors = new ArrayList<>();

    public TwoPassLinker(String filename) throws IOException {
        this.inputString = new String(Files.readAllBytes(Paths.get(FILE_DIR + filename)), StandardCharsets.UTF_8);
        this.tokens = this.inputString.split("\\s+");
    }

    private void firstPass(){
        int baseAdd = 0;
        int passingMode = 1; // 1 for definition, 2 for use list, 3 for program text
        int i=0;

        ArrayList<UseUnit> currentUseList = new ArrayList<>();
        int currentNumberOfUsages = 0;

        while(i<this.tokens.length) {

            if(passingMode == 1){
                if(isDigit(tokens[i])){
                    int numberOfDef = Integer.valueOf(tokens[i]);
                    if(numberOfDef == 0){
                        passingMode = 2;
                        i++;
                    }
                    else{
                        for(int j=1;j<=numberOfDef;j++){
                            i++;
                            String sym = tokens[i];
                            i++;
                            String loc = tokens[i];
                            DefinitionUnit newDef = new DefinitionUnit(sym, Integer.valueOf(loc)+baseAdd);
                            this.definitionList.add(newDef);
                        }
                        passingMode = 2;
                        i++;
                    }
                }else{
                    System.out.println("Something is wrong! The first item in each list MUST be a digit!");
                    System.exit(-1);
                }
            }else if(passingMode == 2){
                if(isDigit(tokens[i])){
                    currentNumberOfUsages = Integer.valueOf(tokens[i]);
                    i++;
                    if(currentNumberOfUsages != 0){
                        i = parseUseList(i, currentNumberOfUsages, currentUseList);
                    }
                    passingMode = 3;
                }else{
                    System.out.println("Something is wrong! The first item in each list MUST be a digit!");
                    System.exit(-1);
                }
            }else if(passingMode == 3){
                if(isDigit(tokens[i])){
                    Module newModule = new Module(baseAdd);
                    newModule.numberOfUsage = currentNumberOfUsages;
                    newModule.useList = currentUseList;
                    int numberOfProgramTextUnits = Integer.valueOf(tokens[i]);
                    newModule.numberOfProgramText = numberOfProgramTextUnits;
                    newModule.programTextList = parseProgramText(i+1, numberOfProgramTextUnits);
                    this.moduleList.add(newModule);

                    currentUseList = new ArrayList<>();
                    baseAdd += numberOfProgramTextUnits;
                    i = i + (numberOfProgramTextUnits*2) + 1;
                    passingMode = 1;
                }else{
                    System.out.println("Something is wrong! The first item in each list MUST be a digit!");
                    System.exit(-1);
                }
            }
        }
    }


    private static boolean isDigit(String s){
        char c = s.charAt(0);
        if (c >= 48 && c <= 57){
            return true;
        }
        else if (c == 45 && s.length() > 1){
            return true;
        }else{
            return false;
        }
    }

    private static boolean isLetter(String s){
        char c = s.charAt(0);
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122) ;
    }

    private void printDefinitionList(){
        for (DefinitionUnit i:this.definitionList) {
            System.out.println(i.sym+" "+i.loc);
        }
    }

    private void printModules(){
        for (Module module : this.moduleList){
            System.out.println(module.baseAddr);
            System.out.print(module.numberOfUsage+" ");
            for(UseUnit use : module.useList){
                System.out.print(use.symbol+" "+use.positions+" -1 ");
            }
            System.out.print("\n");
            System.out.print(module.numberOfProgramText+" ");
            for(ProgramTextUnit unit : module.programTextList){
                System.out.print(unit.type+" "+unit.word+" ");
            }
            System.out.print("\n\n");
        }
    }

    private void printErrorAndWarnings(){
        for(String e:this.errors){
            System.out.println(e);
        }
    }

    /**
     • If a symbol is multiply defined, print an error message and use the value given in the first definition.
     */
    private boolean isValidDefinitionList(){
        boolean isValid = true;
        ArrayList<DefinitionUnit> currentDefinitionListCopy = (ArrayList<DefinitionUnit>) this.definitionList.clone();

        for(DefinitionUnit def : currentDefinitionListCopy){ //this.definitionList
            String symbol = def.sym;
            int location = def.loc;
            if(this.definitionTable.containsKey(symbol)){
                this.errors.add(symbol + DUPLICATION_ERROR);
                this.definitionList.remove(def);
                isValid = false;
            }else{
                this.definitionTable.put(symbol, location);
            }
        }
        return isValid;
    }

    /**
     • If a symbol is used but not defined, print an error message and use the value zero.
     */
    private boolean hasAllNeededSymbol(){
        boolean hasAll = true;
        for(Module module : this.moduleList){
            for(UseUnit unit : module.useList){
                if(!this.definitionTable.containsKey(unit.symbol)){
                    this.definitionList.add(new DefinitionUnit(unit.symbol, 0));
                    this.errors.add(unit.symbol+MISS_DEFINE_ERROR);
                    hasAll = false;
                }
            }
        }
        return hasAll;
    }

    /**
     • If a symbol is defined but not used, print a warning message and continue.
     */
    private boolean hasNoRedundantDefine(){
        boolean hasNoRedundancy = true;

        for(Module m : this.moduleList){
            for(UseUnit u : m.useList){
                if(this.definitionTable.containsKey(u.symbol)){
                    this.definitionTable.put(u.symbol, -1);
                }
            }
        }
        for(Object key : this.definitionTable.keySet()){
            if((int)this.definitionTable.get(key) != -1){
                this.errors.add("Warning: "+key.toString()+" was defined but never used."); //TODO: do we need to specify which module?
                hasNoRedundancy = false;
            }
        }
        return hasNoRedundancy;
    }
    /**
    • If multiple symbols are listed as used in the same instruction, print an error message and ignore all but the first usage given.
    */
    private boolean hasNoContradictedUsePosition(){
        boolean hasNoContradictedUsePosition = true;
        for(Module m : this.moduleList){
            int[] availablePositions = new int[m.numberOfProgramText];
            for(UseUnit u : m.useList){
                for(int idx = 0;idx < u.positions.size();idx++){
                    int pos = (int) u.positions.get(idx);
                    /*
                     If an address appearing in a use list exceeds the size of the module, print an error message and ignore this particular use.
                     */
                    if(pos >= m.numberOfProgramText){
                        this.errors.add("ERROR: Use of " + u.symbol +" exceeds module size; use ignored.");
                        u.positions.remove(idx);
                        hasNoContradictedUsePosition = false;
                    }
                    else if(availablePositions[pos]!=1){
                        availablePositions[pos] = 1;
                    }
                    else{
                        // remove the useage
                        u.positions.remove(idx);
                        this.errors.add("ERROR: "+u.symbol+" is trying to apply on "+pos+" instruction that has been applied, first apply symbol will be used.");
                        hasNoContradictedUsePosition = false;
                    }
                }
            }
        }
        return hasNoContradictedUsePosition;
    }

    private int parseUseList(int i, int numberOfUsages, ArrayList<UseUnit> useList){
        while(numberOfUsages > 0){
            if(isLetter(tokens[i])){
                UseUnit newUse = new UseUnit(tokens[i]);
                i++;
                while(true){
                    if(isDigit(tokens[i])){
                        int pos = Integer.valueOf(tokens[i]);
                        if(pos == -1){
                            numberOfUsages--;
                            i++;
                            useList.add(newUse);
                            break;
                        }
                        else{
                            newUse.positions.add(pos);
                            i++;
                        }
                    }
                }
            }else{
                System.out.println("Something is wrong! parseUseList");
            }
        }
        return i;
    }


    private ArrayList<ProgramTextUnit> parseProgramText(int i, int numberOfPairs){
        ArrayList<ProgramTextUnit> currentProgramList = new ArrayList<>();
        for(int t = 0; t < numberOfPairs; t++){
            if(isLetter(tokens[i])){
                String type = tokens[i];
                i++;
                int word = Integer.valueOf(tokens[i]);
                ProgramTextUnit newProgramTextUnit = new ProgramTextUnit(type, word);
                currentProgramList.add(newProgramTextUnit);
                i++;
            } else{
                System.out.println("Something is wrong! parseProgramText");
            }
        }
        return currentProgramList;
    }

    public static void main(String[] args) throws IOException {
        TwoPassLinker newLinker = new TwoPassLinker("input-9.txt");
        newLinker.firstPass();
        newLinker.printDefinitionList();
        newLinker.printModules();

        boolean verify_1 = newLinker.isValidDefinitionList();
        boolean verify_2 = newLinker.hasAllNeededSymbol();
        boolean verify_3 = newLinker.hasNoContradictedUsePosition();
        boolean verify_4 = newLinker.hasNoRedundantDefine();

        if(!(verify_1 && verify_2 && verify_3 && verify_4)){
            newLinker.printErrorAndWarnings();
        }
    }

}
