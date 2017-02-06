package HW1;

import java.util.ArrayList;

/**
 * @Title: Lab 1
 * @Course : Operating System Lab 1
 * @Date : Feb 9, 2017
 * @Author : Lizi Chen
 * @Email: lc3397@nyu.edu
 */
public class UseUnit {

    String symbol;
    ArrayList positions;

    public UseUnit(String symbol){
        this.symbol = symbol;
        positions = new ArrayList();
    }

    public void addUsage(int newPosition){
        this.positions.add(newPosition);
    }

}
