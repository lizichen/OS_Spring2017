package HW4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by lizichen1 on 4/27/17.
 */
public class RandomNumberProvider {

    public static final String RANDOM_NUMBERS_TXT_FILE = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/random-numbers.txt";
    Scanner random_integers;

    public RandomNumberProvider()  {
        File randomNumberFile = new File(RANDOM_NUMBERS_TXT_FILE);
        try {
            random_integers = new Scanner(randomNumberFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int nextRandomInt(){
        return Integer.valueOf(random_integers.nextLine());
    }
}
