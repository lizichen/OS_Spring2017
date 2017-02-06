package HW1;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Paths;

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
    private static final String FILE_DIR = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/Two-Pass-Linker-master/inputs/";
    private String inputString;
    private String[] tokens;
    private ArrayList<DefinitionUnit> definitionList = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();

    public TwoPassLinker(String filename) throws IOException {
       this.inputString = new String(Files.readAllBytes(Paths.get(FILE_DIR + filename)), StandardCharsets.UTF_8);
        this.tokens = this.inputString.split("\\s+");
    }

    private void firstPass(){
        int baseAdd = 0;
        int passingMode = 1; // 1 for definition, 2 for use list, 3 for program text
        int i=0;
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
                            i = i + j;
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
                    int numberOfUsages = Integer.valueOf(tokens[i]);
                    i++;
                    while(numberOfUsages > 0){
                        if(isDigit(tokens[i])){
                            if(Integer.valueOf(tokens[i]) == -1){
                                numberOfUsages--;
                            }
                        }
                        i++;
                    }
                    passingMode = 3;
                }else{
                    System.out.println("Something is wrong! The first item in each list MUST be a digit!");
                    System.exit(-1);
                }
            }else if(passingMode == 3){
                if(isDigit(tokens[i])){
                    int numberOfProgramTextUnits = Integer.valueOf(tokens[i]);
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

    public static void main(String[] args) throws IOException {
        TwoPassLinker newLinker = new TwoPassLinker("input-2.txt");
        newLinker.firstPass();
        newLinker.printDefinitionList();
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

    private ArrayList<DefinitionUnit> getDefinitionList(){
        return this.definitionList;
    }

    private void printDefinitionList(){
        for (DefinitionUnit i:this.definitionList) {
            System.out.println(i.sym+" "+i.loc);
        }
    }
}
