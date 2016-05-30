package BugReadTest;

/**
 * Created by KATSU on 26/5/2559.
 */
import org.testng.annotations.*;
import com.kem.BugRead.*;

import java.util.ArrayList;

public class SimpleTest {
    private BugRead reader;
    @BeforeClass
    public void setUp() {
        String dirPath = "C:\\Users\\KATSU\\docker\\ubuntu\\data\\apache\\blocker";
        reader = new BugRead(dirPath);
    }

    /*@Test(groups = { "fast" })
    public void XmlTest() {

        reader.ReadByXmlDom(50);
    }*/
    @Test(groups = { "fast" })
    public void PlainTextTest() {
        ArrayList<String> result = reader.ReadText(50);
        BugRead.writeToFile(result, "C:\\Users\\KATSU\\docker\\ubuntu\\data\\apache\\0.blocker50.txt");
    }


}

