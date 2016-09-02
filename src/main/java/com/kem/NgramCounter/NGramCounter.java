package com.kem.NgramCounter;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

/**
 * Created by KATSU on 02/9/2016.
 */
public class NGramCounter {
    private String apiKey ="sufWjeu9CSmshJTVKq2ogwfitUJrp1CEOv8jsn7rGlYE3iIlfD";
    private static NGramCounter instance = null;
    private NGramCounter(){
        init();
    }

    public static NGramCounter getInstance(){
        if (instance ==  null){
            instance = new NGramCounter();
        }
        return instance;
    }

    private void init(){
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public NgramResult sendRequest(int ngram, String text){
        // These code snippets use an open-source library. http://unirest.io/java
        try {
            HttpResponse<NgramResult> response = Unirest.post("https://rxnlp-core.p.mashape.com/generateNGramCounts")
                    .header("X-Mashape-Key", apiKey)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .field("case-sensitive", "false")
                    .field("n-gram", ngram)
                    .field("text", text)
                    .asObject(NgramResult.class);
            NgramResult result = response.getBody();
            return result;
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }
}
