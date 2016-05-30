package com.kem.WordCountWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;

/**
 * Created by KEM-PC on 30/5/2559.
 */
public class WordCountWeb {
    private static WordCountWeb instance;
    private String url = "http://wordcounttools.com/";
    private WebDriver driver;
    private WordCountWeb(){
        driver = new FirefoxDriver();
        driver.get(url);
    }

    public static WordCountWeb getInstance() {
        if(instance == null) {
            instance = new WordCountWeb();
        }
        return instance;
    }

    public HashMap<String, Integer> getCountResult(String text){
        WebElement textbox = driver.findElement(By.id("textbox"));
        textbox.sendKeys(text);
        return null;
    }
}
