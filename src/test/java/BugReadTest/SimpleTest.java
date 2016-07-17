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
        String[] severityList = new String[] {"blocker", "critical", "major", "normal", "minor", "trivial" };
        for (String serv: severityList ) {
            processBug(path, serv, 500);
        }
        WordCountWeb.getInstance().close();
    }

    @Test(groups = {"fast"})
    public void ProcessTextForRapidMinerTest() throws IOException {
        String path = "C:\\Users\\KATSU\\docker\\data\\Eclipse";
        String[] severityList = new String[] {"blocker", "critical", "major", "normal", "minor", "trivial" };
        for (String serv: severityList ) {
            processBugRapidMiner(path, serv, 500);
        }

    }

    private void processBug(String path, String severity, int limit)  throws IOException {
        reader = new BugRead(String.join("\\", new String[]{path, severity}));
        ArrayList<String> result = reader.ReadText(limit);
        WordCountWeb.getInstance().setTopWord(500);
        String allText = reader.writeToFile(result, String.format("%s\\0.%s.%s.txt", path, limit, severity));
        ArrayList<WordCountWeb.WordDensity> countResultCritical = WordCountWeb.getInstance().getCountResult(allText);
        ExcelWriter writer = new ExcelWriter(path + String.format("\\0.WordCount.%s.xlsx", limit));
        writer.AddData(countResultCritical, severity);
    }

    private void processBugRapidMiner(String path, String severity, int limit)  throws IOException {
        reader = new BugReadForRapidMiner(String.join("\\", new String[]{path, severity}));
        ArrayList<String> result = reader.ReadText(limit);
        String allText = reader.writeToFile(result, String.format("%s\\0.%s.%s", path, limit, severity));

    }


    @Test(groups = {"fast"})
    public void ProcessTextForClassificationTest() throws IOException {
        String path = "C:\\Users\\KATSU\\docker\\data\\Eclipse";
        String[] severityList = new String[] {"blocker", "critical", "major", "normal", "minor", "trivial" };
        for (String serv: severityList ) {
            processBugClassification(path, serv, 100);
        }
    }

    private void processBugClassification(String path, String severity, int limit)  throws IOException {
        reader = new BugReadForClass(String.join("\\", new String[]{path, severity}));
        ArrayList<String> result = reader.ReadText(limit);
        String allText = reader.writeToFile(result, String.format("%s\\1.%s.%s.ForClass.csv", path, limit, severity));
    }
}

