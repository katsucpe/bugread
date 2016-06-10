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

    }

    /*@Test(groups = { "fast" })
    public void XmlTest() {

        reader.ReadByXmlDom(50);
    }*/
    @Test(groups = {"fast"})
    public void PlainTextTest() throws IOException {
        String path = "C:\\Users\\KATSU\\docker\\data\\Eclipse";
        processBug(path, "blocker", 150);
        processBug(path, "critical", 150);
        processBug(path, "major", 150);
        processBug(path, "normal", 150);
        processBug(path, "minor", 150);
        processBug(path, "trivial", 150);
        WordCountWeb.getInstance().close();
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

