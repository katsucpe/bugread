package com.kem.WordCountWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KEM-PC on 30/5/2559.
 */
public class WordCountWeb {
    private static WordCountWeb instance;
    private String url = "http://wordcounttools.com/";
    private WebDriver driver;

    private WordCountWeb() {
        /*String chromepath = String.join("\\",new String[]{System.getProperty("user.dir"),"target","chromedriver.exe"});
        System.setProperty("webdriver.chrome.driver", chromepath);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(capabilities);*/
        driver = new FirefoxDriver();
        driver.get(url);
    }

    public static WordCountWeb getInstance() {
        if (instance == null) {
            instance = new WordCountWeb();
        }
        return instance;
    }

    public void close(){
        driver.close();
        driver = null;
    }

    public ArrayList<WordDensity> getCountResult(String text) {
        WebElement textbox = driver.findElement(By.id("textbox"));
        textbox.click();
        textbox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        textbox.sendKeys(Keys.DELETE);
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        textbox.sendKeys(Keys.chord(Keys.CONTROL, "v"));
        return readResult();
    }


    public void setTopWord(int number) {
        WebElement textbox = driver.findElement(By.id("numTopKeyWord"));
        textbox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        textbox.sendKeys(Keys.DELETE);
        textbox.sendKeys(Integer.toString(number));
    }

    private ArrayList<WordDensity> readResult() {
        java.util.List<WebElement> wordElems = driver.findElements(By.xpath("//div[@id='densityList']//label"));
        String patternStr = "<strong>(.*?)<\\/strong> - (\\d+) .*?(\\d+\\.\\d+)%";
        Pattern pattern = Pattern.compile(patternStr);
        ArrayList<WordDensity> wordDensityMap = new ArrayList<>();
        for (WebElement ele : wordElems) {
            String html = ele.getAttribute("outerHTML");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                WordDensity d = new WordDensity(matcher.group(1),
                        Integer.parseInt(matcher.group(2)), Double.parseDouble(matcher.group((3))));
                wordDensityMap.add(d);
            }
        }
        return wordDensityMap;
    }

    public class WordDensity {
        String word;
        int density = 0;
        double densityPct = 0.0;

        public WordDensity(String word, int density, double densityPct) {
            this.word = word;
            this.density = density;
            this.densityPct = densityPct;
        }

        public String getWord() {
            return word;
        }

        public int getDensity() {
            return density;
        }

        public double getDensityPct() {
            return densityPct;
        }

        public String toString() {
            return String.format("%s - %s times (%s%\u0025)", word, density, densityPct);
        }
    }


}
