package BugReadTest;

/**
 * Created by KATSU on 26/5/2559.
 */

import com.kem.Export.ExcelWriter;
import com.kem.WordCountWebDriver.WordCountWeb;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.*;
import com.kem.BugRead.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleTest {
    private BugRead reader;

    @BeforeClass
    public void setUp() {
        String dirPath = "C:\\Users\\KATSU\\docker\\data\\apache\\blocker";
        reader = new BugRead(dirPath);
    }

    /*@Test(groups = { "fast" })
    public void XmlTest() {

        reader.ReadByXmlDom(50);
    }*/
    @Test(groups = {"fast"})
    public void PlainTextTest() throws IOException {
        String path = "C:\\Users\\KATSU\\docker\\data\\apache";
        processBug(path, "blocker", 50);
        processBug(path, "critical", 50);
        processBug(path, "major", 50);
        processBug(path, "normal", 50);
        processBug(path, "minor", 50);
        processBug(path, "trivial", 50);
        WordCountWeb.getInstance().quit();
    }

    private void processBug(String path, String severity, int limit)  throws IOException {
        reader = new BugRead(String.join("\\", new String[]{path, severity}));
        ArrayList<String> result = reader.ReadText(limit);
        WordCountWeb.getInstance().setTopWord(50);
        String allText = BugRead.writeToFile(result, String.format("%s\\0.%s%s.txt", path, severity, limit));
        ArrayList<WordCountWeb.WordDensity> countResultCritical = WordCountWeb.getInstance().getCountResult(allText);
        ExcelWriter writer = new ExcelWriter(path + "\\0.WordCount.xlsx");
        writer.AddData(countResultCritical, severity);
    }

}

