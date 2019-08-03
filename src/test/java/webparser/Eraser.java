package webparser;

import org.junit.BeforeClass;
import org.junit.Test;
import xxx.joker.apps.formula1.webParser.FlagsDownloader;

public class Eraser extends CommonTest {

    @BeforeClass
    public static void beforeClass() {
//        JkEnvironment.setAppsFolder("");
    }

    @Test
    public void clearAll() {
        model.clearAll();
    }

}
