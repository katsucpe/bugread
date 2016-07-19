package com.kem.Classification;

import com.kem.Utils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by KATSU on 19/7/2559.
 */
public class ClassificationByVisualizationResult {
    private HashMap<String, List<String>> uniqueWords = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> highPropWords = new HashMap<String, List<String>>();
    private String testDir = "";

    public ClassificationByVisualizationResult(HashMap<String, List<String>> uniqueWords, HashMap<String, List<String>> highPropWords, String testDir) {
        this.uniqueWords = uniqueWords;
        this.highPropWords = highPropWords;
        this.testDir = testDir;
    }

    public void process() {
        readTestObject();
        classify(highPropWords);
        classify(uniqueWords);

    }

    private String[] severityList = new String[]{"blocker", "critical", "major", "normal", "minor", "trivial"};
    private List<ClassifiedObject> classifiedObject = new ArrayList<>();

    public List<ClassifiedObject> getClassifiedObject() {
        return classifiedObject;
    }

    private void readTestObject() {
        File folder = new File(testDir);
        File[] allFile = folder.listFiles();
        assert allFile != null;
        //String serv = String.join("|", severityList);
        for (File f : allFile) {
            if (Utils.stringContainWordInList(f.getName(), severityList))
                try (Stream<String> stream = Files.lines(Paths.get(f.getAbsolutePath()))) {
                    stream.forEach(processText());

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private Consumer<String> processText() {
        return new Consumer<String>() {
            @Override
            public void accept(String s) {

                String[] temp = StringEscapeUtils.unescapeCsv(s).split(", |,");
                ClassifiedObject clsObj;
                if(temp.length == 3) {
                    clsObj = new ClassifiedObject(temp[0], temp[1], temp[2]);
                }else{
                    String id = temp[0];
                    String actual = temp[temp.length - 1];
                    String subject = String.join(",",Arrays.copyOfRange(temp, 1, temp.length - 1));
                    clsObj = new ClassifiedObject(id, subject, actual);
                }
                classifiedObject.add(clsObj);
            }
        };
    }


    private void classify(HashMap<String, List<String>> wordList) {
        for (ClassifiedObject clsobj : classifiedObject) {

            if (clsobj.getClassResult().isEmpty()) {
                String subject = clsobj.getSubject();
                for (String key : wordList.keySet()) {
                    if (Utils.stringContainWordInList(subject, wordList.get(key).toArray(new String[0]))) {
                        clsobj.setClassResult(key);
                        break;
                    }
                }
            }
        }
    }

    public class ClassifiedObject {
        String id, subject, actual, classResult;

        public ClassifiedObject(String id, String subject, String actual) {
            this.id = id;
            this.subject = subject;
            this.actual = actual;
            this.classResult = "";
        }

        public ClassifiedObject(String id, String subject, String actual, String classResult) {
            this.id = id;
            this.subject = subject;
            this.actual = actual;
            this.classResult = classResult;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getActual() {
            return actual;
        }

        public void setActual(String actual) {
            this.actual = actual;
        }

        public String getClassResult() {
            return classResult;
        }

        public void setClassResult(String classResult) {
            this.classResult = classResult;
        }
    }
}
