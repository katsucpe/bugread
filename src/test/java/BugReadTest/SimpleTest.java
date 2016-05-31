package BugReadTest;

/**
 * Created by KATSU on 26/5/2559.
 */
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
        String dirPath = "C:\\Users\\KEM-PC\\docker\\data\\apache\\blocker";
        reader = new BugRead(dirPath);
    }

    /*@Test(groups = { "fast" })
    public void XmlTest() {

        reader.ReadByXmlDom(50);
    }*/
    @Test(groups = { "fast" })
    public void PlainTextTest() throws IOException {
        ArrayList<String> result = reader.ReadText(50);
        String allText = BugRead.writeToFile(result, "C:\\Users\\KEM-PC\\docker\\data\\apache\\0.blocker50.txt");
        WordCountWeb.getInstance().setTopWord(50);
        ArrayList<WordCountWeb.WordDensity> countResult = WordCountWeb.getInstance().getCountResult(allText);
    }

}

