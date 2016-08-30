package BugReadTest;

/**
 * Created by KATSU on 26/5/2559.
 */

import com.kem.Classification.ClassificationByVisualizationResult;
import com.kem.Classification.ExcelClassResultWriter;
import com.kem.Export.ExcelWriter;
import com.kem.ReadVisualizeResult.ReadVisualizeResult;
import com.kem.Utils;
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

        Utils.initLog();
    }

    private String rootPath = "C:\\Users\\KATSU\\docker\\data\\";
    private String projectName = "Eclipse";

    @Test(groups = {"fast"})
    public void ProcessTextForRapidMinerTest() throws IOException {
        String path = rootPath + projectName;
        String[] severityList = new String[] {"blocker", "critical", "major", "normal", "minor", "trivial" };
        for (String serv: severityList ) {
            processBugRapidMiner(path, serv, 300);
        }

    }

    @Test(groups = {"fast"})
    public void PlainTextTest() throws IOException {
        String path = rootPath + projectName;
        String[] severityList = new String[] {"blocker", "critical", "major", "normal", "minor", "trivial" };
        for (String serv: severityList ) {
            processBug(path, serv, 500);
        }
        WordCountWeb.getInstance().close();
    }

    private void processBug(String path, String severity, int limit)  throws IOException {
        reader = new BugRead(String.join("\\", new String[]{path, "Train",severity}));
        int topWords = 100;
        ArrayList<String> result = reader.ReadText(limit);
        WordCountWeb.getInstance().setTopWord(topWords);
        String allText = reader.writeToFile(result, String.format("%s\\0.%s.%s.txt", path, limit, severity));
        ArrayList<WordCountWeb.WordDensity> countResultCritical = WordCountWeb.getInstance().getCountResult(allText);
        ExcelWriter writer = new ExcelWriter(path + String.format("\\0.WordCount.%s.topW%s.xlsx", limit, topWords));
        writer.AddData(countResultCritical, severity);
    }

    private void processBugRapidMiner(String path, String severity, int limit)  throws IOException {
        reader = new BugReadForRapidMiner(String.join("\\", new String[]{path, "Train",severity}));
        ArrayList<String> result = reader.ReadText(limit);
        String allText = reader.writeToFile(result, String.format("%s\\ForRP\\2.%s.%s", path, limit, severity));

    }


    @Test(groups = {"fast"})
    public void ProcessTextForClassificationTest() throws IOException {
        String path = rootPath + projectName;
        String[] severityList = new String[] {"blocker", "critical", "major", "normal", "minor", "trivial" };
        for (String serv: severityList ) {
            processBugClassification(path + "\\TestData", serv, 150);
        }


    }
    @Test(groups = {"fast"})
    public void ProcessVisualizationResult() throws IOException {
        ReadVisualizeResult readVisualizeResult = new ReadVisualizeResult(rootPath + projectName + "\\Model");
        readVisualizeResult.process();
        String testDir = rootPath + projectName + "\\TestResult";
        ClassificationByVisualizationResult clsResult = new ClassificationByVisualizationResult(readVisualizeResult.getUniqueWords(), readVisualizeResult.getHighProbWords(), testDir);
        clsResult.process();
        ExcelClassResultWriter resultWriter = new ExcelClassResultWriter(testDir + "\\classResult.6Class.xlsx", clsResult.getClassifiedObject());
        resultWriter.writeDataToFile();
        ClassificationByVisualizationResult clsResult2 = new ClassificationByVisualizationResult(readVisualizeResult.getUniqueWords(), readVisualizeResult.getHighProbWords(), testDir);
        clsResult2.process(true);
        ExcelClassResultWriter resultWriter2 = new ExcelClassResultWriter(testDir + "\\classResult.2Class.xlsx", clsResult2.getClassifiedObject());
        resultWriter2.writeDataToFile();

    }

    private void processBugClassification(String path, String severity, int limit)  throws IOException {
        reader = new BugReadForClass(String.join("\\", new String[]{path, severity}));
        ArrayList<String> result = reader.ReadText(limit);
        String allText = reader.writeToFile(result, String.format("%s\\..\\TestResult\\1.%s.%s.ForTest.csv", path, limit, severity));

    }
}

