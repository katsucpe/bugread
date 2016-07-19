package com.kem.ReadVisualizeResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KATSU on 17/7/2559.
 */
public class ReadVisualizeResult {
    private String dirLocation;
    public ReadVisualizeResult(String dirLocation){
        this.dirLocation = dirLocation;
    }

    public void process(){

        try {
            readUniqueWord();
            readHighProb();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, List<String>> uniqueWords = new HashMap<>();
    public void readUniqueWord() throws IOException {
        String path = String.format("%s//uniqueWord.txt", dirLocation);
        for (String line : Files.readAllLines(Paths.get(path))) {
            String[] temp = line.split("=");
            String[] wordInSeverity = temp[1].split(", |,");
            uniqueWords.put(temp[0], Arrays.asList(wordInSeverity));
        }
    }
    private HashMap<String, List<String>> highProbWords = new HashMap<>();
    public void readHighProb() throws IOException {
        String path = String.format("%s//HighProp.txt", dirLocation);
        for (String line : Files.readAllLines(Paths.get(path))) {
            String[] temp = line.split("=");
            String[] wordInSeverity = temp[1].split(", |,");

            highProbWords.put(temp[0], Arrays.asList(wordInSeverity));
        }
    }

    public HashMap<String, List<String>> getUniqueWords(){
        return uniqueWords;
    }

    public HashMap<String, List<String>> getHighProbWords(){
        return highProbWords;
    }
}
