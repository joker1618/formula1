package webparser;

import org.junit.BeforeClass;
import org.junit.Test;
import xxx.joker.apps.formula1.webParser.FlagsDownloader;
import xxx.joker.libs.core.debug.JkDebug;

public class FlagsWebParser extends CommonTest {

    @BeforeClass
    public static void beforeClass() {
//        JkEnvironment.setAppsFolder("");
    }

    @Test
    public void downloadFlags() {
        new FlagsDownloader().downloadCountries();
        model.commit();
    }

}
