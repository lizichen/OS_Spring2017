package HW1;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Title: Lab 1
 * @Course : Operating System
 * @Date : Feb 9, 2017
 * @Author : Lizi Chen
 * @Email: lc3397@nyu.edu
 * @Github: https://github.com/lizichen/OS_Spring2017
 */
public class TwoPassLinker {
    private static final String DUPLICATION_ERROR = " Error: This variable is multiply defined; first value used.";
    private static final String MISS_DEFINE_ERROR = " Error: This variable is NOT defined; zero used.";
    public static final String FIRST_DIGIT_ERROR = "Something is wrong! The first item in each list MUST be a digit!";

    private static final String EMPTY_STRING = "";
    private static final String IMMEDIATE = "I";
    private static final String ABSOLUTE  = "A";
    private static final String RELATIVE  = "R";
    private static final String EXTERNAL  = "E";

    public static final String FILE_DIR = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/SampleData/inputs/";

    private String inputString;
    private String[] tokens;

    private ArrayList<DefinitionUnit> definitionList = new ArrayList<>();
    //TODO: remove after refactoring the Module class! used for storing the module order of a definition unit.
    private Hashtable<String, Integer> definitionSymModuleOrderNumberMap = new Hashtable<>();

    private Hashtable definitionTable = new Hashtable();
    private ArrayList<Module> moduleList = new ArrayList<>();

    private ArrayList<String> errors = new ArrayList<>();

    public TwoPassLinker(String filename) throws IOException {
        this.inputString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        String[] fulllist = this.inputString.split("\\s+");
        if(fulllist[0].equals(EMPTY_STRING)){
            //when the first token is an empty space
            this.tokens = Arrays.copyOfRange(fulllist, 2, fulllist.length);
        }else{
            this.tokens = Arrays.copyOfRange(fulllist, 1, fulllist.length);
        }
    }

    private void firstPass(){
        int baseAdd = 0;
        int passingMode = 1; // 1 for definition, 2 for use list, 3 for program text
        int i=0;

        ArrayList<UseUnit> currentUseList = new ArrayList<>();
        int currentNumberOfUsages = 0;
        int moduleOrder = 1;

        DefinitionUnit newDef = null; //TODO: this is a make-up approach, should include definition inside a module! Refactor later!
        while(i<this.tokens.length) {
            if(passingMode == 1){
                if(isDigit(tokens[i])){
                    int numberOfDef = Integer.valueOf(tokens[i]);
                    if(numberOfDef == 0){
                        passingMode = 2;
                        i++;
                    }
                    else{
                        for(int j=1;j<=numberOfDef;j++){ // it's possible to have multiple definitions
                            i++;
                            String sym = tokens[i];
                            i++;
                            String loc = tokens[i];
                            newDef = new DefinitionUnit(sym, Integer.valueOf(loc)+baseAdd);
                            this.definitionList.add(newDef);
                            this.definitionSymModuleOrderNumberMap.put(sym, moduleOrder);
                        }
                        System.out.println("number of definitions: "+numberOfDef);
                        passingMode = 2;
                        i++;
                    }
                    moduleOrder++;
                }else{
                    System.out.println(FIRST_DIGIT_ERROR);
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
                    System.out.println(FIRST_DIGIT_ERROR);
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

                    //TODO: after refactor the definition unit inside the Modele, we should eliminate this:
                    if(newDef!=null){
                        //System.out.println("evaluate if definition exceeds module size!");
                        if(newDef.loc - baseAdd >= newModule.numberOfProgramText){ //newDef.loc!
                            String sym = newDef.sym;
                            int loc = newModule.baseAddr;

                            this.definitionList.remove(this.definitionList.size()-1);
                            this.definitionList.add(new DefinitionUnit(sym, loc));
                            this.errors.add("ERROR! Definition of [" + sym + "] exceeds module size; first word in module used: ["+sym+"="+loc+"]");
                        }
                    }
                    //TODO: Remove the above after refactoring the Module class.

                    currentUseList = new ArrayList<>();
                    baseAdd += numberOfProgramTextUnits;
                    i = i + (numberOfProgramTextUnits*2) + 1;
                    passingMode = 1;
                    newDef = null;
                }else{
                    System.out.println(FIRST_DIGIT_ERROR);
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
        System.out.println("Symbol Table");
        for (DefinitionUnit i:this.definitionList) {
            System.out.println(i.sym+" = "+i.loc);
        }
    }

    private void printErrorAndWarnings(){
        System.out.println();
        for(String e:this.errors){
            System.out.println(e);
        }
    }

    private void printMemoryMap(){
        HashMap memoryresultMap = new HashMap();
        int line = 0;
        int moduleOrder = 0;
        for(Module m : this.moduleList){
            int baseAddr = m.baseAddr;

            String[] useArr = convertListToArray(m.useList, m.numberOfProgramText);

            for(int i=0; i<m.numberOfProgramText; i++){
                ProgramTextUnit currentP = m.programTextList.get(i);
                String type = currentP.type;
                int address = 0;

                if(type.equals(RELATIVE)){
                    if(currentP.word % 1000 >= m.numberOfProgramText){
                        this.errors.add("Error: Module "+moduleOrder+" has instruction: [R "+currentP.word+"] that exceeds the module size "+m.numberOfProgramText+"; zero used.");
                        address = currentP.word / 1000 * 1000;
                    }else{
                        address = baseAddr + currentP.word;
                    }
                }else if(type.equals(IMMEDIATE)){
                    address = currentP.word;
                }else if(type.equals(EXTERNAL)){
                    String defSym = useArr[i];
                    int extraValue = (int)this.definitionTable.get(defSym);
                    address = currentP.word / 1000 * 1000 + extraValue;
                }else if(type.equals(ABSOLUTE)){
                    address = currentP.word;
                    int errorAdd = address;
                    if(address%1000 >= 200){
                        address = address / 1000 * 1000 ;
                        this.errors.add("Error: Module "+moduleOrder+" has instruction: [A "+errorAdd+"] that has absolute address exceeds machine size; zero used.");
                    }
                }else{
                    System.out.println("ERROR! Type is not defined!");
                }
                memoryresultMap.put(line, address);
                line++;
            }
            moduleOrder++;
        }
        System.out.println("\nMemory Map");
        Iterator it = memoryresultMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + ":\t" + pair.getValue());
            it.remove();
        }
    }

    //TODO: very bad design, should change the Module to have a hashtable first.
    public static String[] convertListToArray(ArrayList<UseUnit> useUnitList, int numberOfProgramTextUnit){
        String[] newArr = new String[numberOfProgramTextUnit];
        for(UseUnit u : useUnitList){
            for(Object i :  u.positions){
                 newArr[(int)i] = u.symbol;
            }
        }
        return newArr;
    }

    /**If a symbol is multiply defined, print an error message and use the value given in the first definition.
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
                this.definitionSymModuleOrderNumberMap.remove(symbol);
                isValid = false;
            }else{
                this.definitionTable.put(symbol, location);
            }
        }
        return isValid;
    }

    /**If a symbol is used but not defined, print an error message and use the value zero.
     */
    private boolean hasAllNeededSymbol(){
        boolean hasAll = true;
        int moduleOrder = 1;
        for(Module module : this.moduleList){
            for(UseUnit unit : module.useList){
                if(!this.definitionTable.containsKey(unit.symbol)){
                    this.definitionList.add(new DefinitionUnit(unit.symbol, 0));
                    this.definitionSymModuleOrderNumberMap.put(unit.symbol, moduleOrder);
                    this.errors.add(unit.symbol+MISS_DEFINE_ERROR);
                    this.definitionTable.put(unit.symbol, 0);
                    hasAll = false;
                }
            }
            moduleOrder++;
        }
        return hasAll;
    }

    /**If a symbol is defined but not used, print a warning message and continue.
     */
    private boolean hasNoRedundantDefine(){
        boolean hasNoRedundancy = true;
        Hashtable cloneDefTable = (Hashtable) this.definitionTable.clone();

        for(Module m : this.moduleList){
            for(UseUnit u : m.useList){
                if(cloneDefTable.containsKey(u.symbol)){
                    cloneDefTable.put(u.symbol, -1);
                }
            }
        }
        for(Object key : cloneDefTable.keySet()){
            if((int)cloneDefTable.get(key) != -1){
                int moduleOrder = this.definitionSymModuleOrderNumberMap.get(key.toString());
                this.errors.add("Warning: "+key.toString()+" was defined in module " +moduleOrder+" but never used.");
                hasNoRedundancy = false;
            }
        }
        return hasNoRedundancy;
    }
    /**If multiple symbols are listed as used in the same instruction, print an error message and ignore all but the first usage given.
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
                        this.errors.add("ERROR: "+u.symbol+" is trying to apply on "+pos+" instruction that has been applied. Multiple variables used, first apply symbol will be used.");
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

        String filePath;
        TwoPassLinker newLinker = null;
        if(args.length == 1){ // java TwoPassLinker input-5.txt
            filePath = args[0];
            newLinker = new TwoPassLinker(filePath);
        }else if (args.length == 2){ // for testing java TwoPassLinker -d input-5.txt
            if(args[0].equals("-t")){
                filePath = args[1];
                newLinker = new TwoPassLinker(FILE_DIR+filePath);
            }
        }else{
            //filePath="input-9.txt";
            //newLinker = new TwoPassLinker(FILE_DIR+filePath);
            throw new IllegalArgumentException("\nERROR! Wrong Input File Path!\nTry input format as:\njava TwoPassLinker input-5.txt");
        }

        newLinker.firstPass();
        //For testing only:
        //newLinker.printModules();

        boolean verify_1 = newLinker.isValidDefinitionList();
        boolean verify_2 = newLinker.hasAllNeededSymbol();
        boolean verify_3 = newLinker.hasNoContradictedUsePosition();
        boolean verify_4 = newLinker.hasNoRedundantDefine();

        newLinker.printDefinitionList();
        newLinker.printMemoryMap();

        if(!(verify_1 && verify_2 && verify_3 && verify_4)){
            newLinker.printErrorAndWarnings();
        }
    }

    // For testing use only
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
}

//TODO: Refactor the Module Class!!!!
