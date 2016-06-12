package com.kem.BugRead;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KATSU on 12/6/2559.
 */
public class BugReadForRapidMiner extends BugRead {
    Logger logger = Logger.getLogger(BugReadForRapidMiner.class);
    public BugReadForRapidMiner(String dirLocation) {
        super(dirLocation);
    }

    public String writeToFile(List<String> text, String path) throws IOException
    {
        if(Files.notExists(Paths.get(path))){
            new File(path).mkdirs();
        }
        int index = 0;
        for (String textItem : text ) {
            Path file = Paths.get(path + "\\" + allFile[index].getName().replace(".xml", ".txt"));
            try {
                FileUtils.write(file.toFile(), textItem, Charset.forName("UTF-8"));

            } catch (IOException e) {
                e.printStackTrace();
            }
            index++;
        }

        return Arrays.deepToString(text.toArray());
    }
}
