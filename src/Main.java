import rune.Cache;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Cache.fromFile  (args.length > 0 ? args[0] : "Cache.ini")
             .extractTo (args.length > 1 ? args[1] : "extracted");
    }
}
