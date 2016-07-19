package com.kem.Classification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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

    public void process(){
        readTestObject();
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
        String serv = String.join("|", severityList);
        allFile.
        for (File f : allFile) {
            if (f.getName().toLowerCase().matches(serv))
                try (Stream<String> stream = Files.lines(Paths.get(f.getAbsolutePath()))) {
                    stream.forEach(processText());

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private Consumer<String> processText(){
        return new Consumer<String>() {
            @Override
            public void accept(String s) {
                String[] temp = s.split(", ");
                ClassifiedObject clsObj = new ClassifiedObject(temp[0], temp[1], temp[2]);
                classifiedObject.add(clsObj);
            }
        };
    }

    public class ClassifiedObject {
        String id, subject, actual, classResult;

        public ClassifiedObject(String id, String subject, String actual) {
            this.id = id;
            this.subject = subject;
            this.actual = actual;
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
