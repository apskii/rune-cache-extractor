package rune;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.*;

import java.util.*;

import static java.lang.System.out;

public class Cache
{
    private final Map<String, String> entries = new HashMap<>();
    
    private final Path cacheFilePath;
    
    private Cache (String cacheFilePathname)
    {
        cacheFilePath = Paths.get(cacheFilePathname);
    }
    
    public static Cache fromFile (String pathname) throws IOException
    {   
        Cache cache        = new Cache(pathname);        
        List<String> lines = Files.readAllLines(cache.cacheFilePath, StandardCharsets.UTF_16LE);
        
        for (String line : lines.subList(1, lines.size()))
        {                        
            String kv[] = line.split("=");
            
            if (kv.length > 1)            
                cache.entries.put(kv[0], kv[1]);
        }
        
        return cache;
    }
    
    public void extractTo (String dirname) throws IOException
    {
        Path sourceDir = cacheFilePath.toAbsolutePath().getParent(),
             targetDir = Paths.get(dirname);
        
        if (!Files.exists(targetDir))
            Files.createDirectory(targetDir);
        
        for (Map.Entry<String, String> entry : entries.entrySet())
        {                        
            Path source = null, target = null;
            
            try { source = sourceDir.resolve(entry.getKey() + ".uxx"); }
            catch (InvalidPathException _)
            {
                out.println("Skipping `" + entry.getValue() + "` because of malformed hash-name `" + entry.getKey() + "`...");
                continue;
            }
            
            try { target = targetDir.resolve(entry.getValue() + ".u"); }
            catch (InvalidPathException _)
            {
                out.println("Skipping `" + entry.getKey() + "` because of malformed target name `" + entry.getValue() + "`...");
                continue;
            }
            
            if (!Files.exists(source))
            {
                out.println("Skipping non-existing `" + entry.getValue() + "`...");
                continue;
            }

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            out.println("Extracting `" + entry.getValue() + "`...");
        }
    }
}
