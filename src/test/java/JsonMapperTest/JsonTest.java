package JsonMapperTest;

/**
 * Created by KATSU on 26/5/2559.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kem.BugRead.BugRead;
import com.kem.BugRead.BugReadForClass;
import com.kem.BugRead.BugReadForRapidMiner;
import com.kem.Classification.ClassificationByVisualizationResult;
import com.kem.Classification.ExcelClassResultWriter;
import com.kem.Export.ExcelWriter;
import com.kem.NgramCounter.NgramResult;
import com.kem.ReadVisualizeResult.ReadVisualizeResult;
import com.kem.Utils;
import com.kem.WordCountWebDriver.WordCountWeb;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class JsonTest {
    Logger logger = Logger.getLogger(JsonTest.class);
    @BeforeClass
    public void setUp() {
        Utils.initLog();
    }

    @Test
    public void testStringToOject(){
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "{\"results\":[\" how i wish:2\",\" wish it was:2\",\" i wish it:2\",\" i love rainy:1\",\" was snowing .:1\",\" it was snowing:1\",\" love rainy days:1\",\" was raining .:1\",\" rainy days .:1\",\" it was raining:1\"]}";
        try {
            NgramResult obj = mapper.readValue(jsonInString, NgramResult.class);
            logger.info(Arrays.deepToString(obj.results));
            HashMap map = obj.getProcessResult();
            logger.info(Arrays.deepToString(map.keySet().toArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

