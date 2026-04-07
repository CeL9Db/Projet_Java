import java.util.Random;

public class Personnalisation {
    public static int color(){
        Random i = new Random();
        return i.nextInt(256);
    }
}