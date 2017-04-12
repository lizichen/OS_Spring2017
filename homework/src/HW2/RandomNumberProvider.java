package HW2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by lizichen1 on 3/3/17.
 */
public class RandomNumberProvider {

    public static final String RANDOM_NUMBERS_TXT_FILE = "./HW2/random-numbers.txt";
    String[] numbers;
    int index;
    Scanner random_integers;

    public RandomNumberProvider() throws IOException {
        // for FCFS
//        String filename = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/random-numbers.txt";
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String filename = RANDOM_NUMBERS_TXT_FILE;
        String inputString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        this.numbers = inputString.split("\\r?\\n");
        this.index = 0;

        // for RR, SJF, Uniprogrammed
        File randomNumberFile = new File(Utils.RANDOM_NUMBER_FILE);
        random_integers = new Scanner(randomNumberFile);
    }

    // for RR, SJF, Uniprogrammed
    public int randomOS(int B, boolean v){
        String next = random_integers.nextLine();
        if (v == true) {
            System.out.println(Utils.FIND_BURST_WHEN_CHOOSING_NEXT_PROCESS_TO_RUN + next);
        }
        return Integer.valueOf(next) % B + 1;
    }

    // for FCFS
    public int randomOS(int U){
        int r = Integer.valueOf(numbers[index++]);
        return 1 + r % U;

    }
}
