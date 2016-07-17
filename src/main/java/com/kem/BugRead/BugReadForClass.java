package com.kem.BugRead;

import com.kem.Utils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.log4j.PropertyConfigurator.*;

/**
 * Created by KATSU on 26/5/2559.
 */
public class BugReadForClass extends BugRead {
    private String dirLocation;
    Logger logger = Logger.getLogger(BugReadForClass.class);

    String severity;

    public BugReadForClass(String dirLocation) {
        super(dirLocation);
    }


    public ArrayList<String> ReadText(int limit) {
        readFile();
        FileInputStream inputStream = null;
        Pattern patternSubject = Pattern.compile("<short_desc>(.*?)</short_desc>");
        Pattern patternSeverity = Pattern.compile("<bug_severity>(.*?)</bug_severity>");
        Pattern patternId = Pattern.compile("<bug_id>(.*?)</bug_id>");
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 1; i <= limit; i++) {
            if (i == allFile.length) {
                break;
            }
            File f = allFile[allFile.length - i];
            logger.info(String.format("Read file: %s", f.getName()));
            try {

                inputStream = new FileInputStream(f.getAbsoluteFile());
                String everything = StringEscapeUtils.unescapeXml(IOUtils.toString(inputStream, Charsets.toCharset("UTF-8")));
                String result = "";
                Matcher matcherID = patternId.matcher(everything);
                if (matcherID.find()) {
                    result += matcherID.group(1).toLowerCase();
                }
                Matcher matcherSubject = patternSubject.matcher(everything);
                if (matcherSubject.find()) {
                    result += "," + matcherSubject.group(1).toLowerCase();
                }
                Matcher matcherServ = patternSeverity.matcher(everything);
                if (matcherServ.find()) {
                    result += "," + matcherServ.group(1).toLowerCase();
                }
                resultList.add(result);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(Arrays.deepToString(resultList.toArray()));
        return resultList;
    }

    public String writeToFile(List<String> text, String path) throws IOException {

        Path file = Paths.get(path);
        try {
            Files.write(file, text, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream inputStream = new FileInputStream(new File(path).getAbsoluteFile());
        return IOUtils.toString(inputStream, "UTF-8");
    }
}
