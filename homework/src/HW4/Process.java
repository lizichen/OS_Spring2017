package HW4;

/**
 * Created by lizichen1 on 4/27/17.
 */
public class Process {

    int processSize;
    int pageSize;
    int currentWord;
    int processId;

    int numberOfPageFaultOccurance = 0;
    int evictOccurance = 0;

    int overallResidencyTime = 0;

    public Process(int processId, int processSize, int pageSize){
        this.processId = processId;
        this.processSize = processSize;
        this.pageSize = pageSize;
        this.currentWord = 111 * processId % processSize;
    }

    public int getCurrentWord(){
        return this.currentWord;
    }

    public void getNextWord_singleProcess() {
        this.currentWord += 1;
        this.currentWord = this.currentWord % this.processSize;
    }

    public void incrementPageFaultOccurance(){
        this.numberOfPageFaultOccurance++;
    }


    public void incrementEvictOccuranceByOne() {
        this.evictOccurance++;
    }

    public void addOverallResidencyTime(int currentResidencyTime) {
        this.overallResidencyTime += currentResidencyTime;
    }

    public void getNextWord_multiProcess(double A, double B, double C, RandomNumberProvider randomNumberProvider) {
        int randomNum = randomNumberProvider.nextRandomInt();
        double quotient = randomNum / (Integer.MAX_VALUE + 1d);
        if (quotient < A) {
            currentWord = (currentWord + 1) % processSize;
        } else if (quotient < A + B) {
            currentWord = (currentWord - 5 + processSize) % processSize;
        } else if (quotient < A + B + C) {
            currentWord = (currentWord + 4) % processSize;
        } else {
            int randomRef = randomNumberProvider.nextRandomInt() % processSize;
            currentWord = randomRef;
        }
    }
}
