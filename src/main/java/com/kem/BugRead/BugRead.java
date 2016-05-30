package com.kem.BugRead;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
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

/**
 * Created by KATSU on 26/5/2559.
 */
public class BugRead {
    Logger logger = Logger.getLogger(BugRead.class);
    File[] allFile;

    public BugRead(String dirLocation String severity){
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        File folder = new File(dirLocation);
        allFile = folder.listFiles();
        Arrays.sort( allFile, new Comparator()
        {
            public int compare(Object o1, Object o2) {
                int name1 = Integer.parseInt(((File)o1).getName().replace(".xml",""));
                int name2 = Integer.parseInt(((File)o2).getName().replace(".xml",""));
                if (name1 > name2) {
                    return -1;
                } else if (name1 < name2) {
                    return +1;
                } else {
                    return 0;
                }
            }

        });

    }

    public String ReadByXmlDom(int limit){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        ArrayList<String> resultList = new ArrayList<>();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            for(int i = 1; i<=limit; i++){
                File f = allFile[i];
                logger.info(String.format("Read file: %s", f.getName()));
                Document doc = dBuilder.parse(f);
                String value = doc.getElementsByTagName("short_desc").item(0).getTextContent();
                resultList.add(value);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        logger.info(Arrays.deepToString(resultList.toArray()));
        return Arrays.deepToString(resultList.toArray());
    }

    public ArrayList<String> ReadText(int limit){
        FileInputStream inputStream = null;
        Pattern pattern = Pattern.compile("<short_desc>(.*?)</short_desc>");
        ArrayList<String> resultList = new ArrayList<>();
        for(int i = 1; i<=limit; i++) {
            File f = allFile[i];
            logger.info(String.format("Read file: %s", f.getName()));
            try {

                inputStream = new FileInputStream(f.getAbsoluteFile());
                String everything = IOUtils.toString(inputStream, Charsets.toCharset("UTF-8"));

                Matcher matcher = pattern.matcher(everything);
                if(matcher.find()){
                    resultList.add(matcher.group(1));
                }
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

    public static void writeToFile(List<String> text,String path){

        Path file = Paths.get(path);
        try {
            Files.write(file, text, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}