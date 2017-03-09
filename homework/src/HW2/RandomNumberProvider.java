package HW2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by lizichen1 on 3/3/17.
 */
public class RandomNumberProvider {

    String[] numbers;
    int index;

    public RandomNumberProvider() throws IOException {
        String filename = "/Users/lizichen1/Google_Drive/OS_Sp17/homework/src/HW2/random_integers-numbers.txt";
        String inputString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        this.numbers = inputString.split("\\r?\\n");
        this.index = 0;
    }

    public int randomOS(int U){
        int X = Integer.valueOf(numbers[index++]);
        return 1 + X % U;
    }
}
