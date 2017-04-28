package HW4;

/**
 * Created by lizichen1 on 4/27/17.
 */
public class FrameRow {

    int pageNumber;
    int processId;
    int currentTime;
    int leastRecentTimeStamp; // for LRU only

    public FrameRow(int PageNumber, int ProcessId, int currentTime){
        this.pageNumber = PageNumber;
        this.processId = ProcessId;
        this.currentTime = currentTime;
    }
}
