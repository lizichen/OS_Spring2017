package HW4;

/**
 * Created by lizichen1 on 4/27/17.
 */
public class FrameTable {

    RandomNumberProvider randomNumberProvider;
    int numberOfFrames;
    FrameRow[] frameRows;

    public FrameTable(int numberOfFrames, RandomNumberProvider randomNumberProvider) {
        this.randomNumberProvider = randomNumberProvider;
        this.numberOfFrames = numberOfFrames;
        this.frameRows = new FrameRow[this.numberOfFrames];
        for(int i=0;i<this.numberOfFrames;i++){
            this.frameRows[i] = new FrameRow(-1,-1,-1); // empty
        }
    }

    public boolean hasPageFault(int currentPage, int processId, int currentTime){

        for(int i = 0; i< this.numberOfFrames; i++){
            if(frameRows[i].processId == processId){
                if(frameRows[i].pageNumber == currentPage){
                    return false;
                }
            }
        }
        System.out.println("Fault!");
        // no such page for the processId in the frames!
        return true;
    }

    public void replaceFrameRow(int currentPage, int processId, int currentTime, Process[] processes, String replacementAlgo) {

        // find the un-used frame if exists
        for(int i=numberOfFrames-1;i>=0;i--){
            if ((frameRows[i].pageNumber == -1) && (frameRows[i].processId == -1)) { // meaning this frame is un-used
                frameRows[i].pageNumber = currentPage;
                frameRows[i].processId = processId;
                frameRows[i].currentTime = currentTime;
                return;
            }
        }

        // if there is no empty frame, then use the replacementAlgo to evict a frameRow
        if(replacementAlgo.equals("random")){
            //Process the evicted process page: add eviction time by one, and add its total resident time
            //find the evicted by generating random number
            int frame_id_ToBeEvicted = this.randomNumberProvider.nextRandomInt() % this.numberOfFrames;
            FrameRow frameToBeEvicted = this.frameRows[frame_id_ToBeEvicted];

            int evictPageNumber = frameToBeEvicted.pageNumber;
            int evictProcessId = frameToBeEvicted.processId;
            Process evictProcess = processes[evictProcessId];
            evictProcess.incrementEvictOccuranceByOne();
            int currentResidencyTime = currentTime - frameToBeEvicted.currentTime;
            evictProcess.addOverallResidencyTime(currentResidencyTime);

            this.frameRows[frame_id_ToBeEvicted].processId = processId;
            this.frameRows[frame_id_ToBeEvicted].pageNumber = currentPage;
            this.frameRows[frame_id_ToBeEvicted].currentTime = currentTime;
        }else if(replacementAlgo.equals("lru")){

            int leastRecentUsedFrame_TimeStamp = currentTime;

            int frame_id_ToBeEvicted = -1;
            for(int i=numberOfFrames-1;i>=0;i--){
                if(leastRecentUsedFrame_TimeStamp > frameRows[i].leastRecentTimeStamp){
                    leastRecentUsedFrame_TimeStamp = frameRows[i].leastRecentTimeStamp;
                    frame_id_ToBeEvicted = i;
                }
            }

            FrameRow frameToBeEvicted = this.frameRows[frame_id_ToBeEvicted];
            int evictProcessId = frameToBeEvicted.processId;
            Process evictProcess = processes[evictProcessId];
            evictProcess.incrementEvictOccuranceByOne();

            int currentResidencyTime = currentTime - frameToBeEvicted.currentTime;
            evictProcess.addOverallResidencyTime(currentResidencyTime);

            this.frameRows[frame_id_ToBeEvicted].processId = processId;
            this.frameRows[frame_id_ToBeEvicted].pageNumber = currentPage;
            this.frameRows[frame_id_ToBeEvicted].currentTime = currentTime;
            this.frameRows[frame_id_ToBeEvicted].leastRecentTimeStamp = currentTime;
        }else if(replacementAlgo.equals("lifo")){

        }


    }
}
