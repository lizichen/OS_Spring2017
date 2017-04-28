package HW4;

/**
 * Created by lizichen1 on 4/27/17.
 */
public class Utils {

    static boolean PRINT_LOG = false;

    public static void log(String toprint){
        if(PRINT_LOG){
            System.out.println(toprint);
        }
    }
}
