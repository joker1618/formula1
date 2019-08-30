package webparser;

import common.CommonTest;
import org.junit.BeforeClass;
import org.junit.Test;
import xxx.joker.apps.formula1.webParser.FlagsDownloader;
import xxx.joker.libs.core.files.JkFiles;

import java.net.URL;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class FlagsWebParser extends CommonTest {

    @BeforeClass
    public static void beforeClass() {
//        JkEnvironment.setAppsFolder("");
    }

    @Test
    public void imgUnavailableInsert() {
        URL url = getClass().getClassLoader().getResource("imageUnavailable.png");
        model.saveImageUnavailable(JkFiles.toPath(url));
        model.commit();
    }

    @Test
    public void downloadFlags() {
        URL url = getClass().getClassLoader().getResource("imageUnavailable.png");
        model.saveImageUnavailable(JkFiles.toPath(url));

        new FlagsDownloader().downloadCountries();
        model.commit();
    }
    @Test
    public void downloadFlagsImages() {
        URL url = getClass().getClassLoader().getResource("imageUnavailable.png");
        model.saveImageUnavailable(JkFiles.toPath(url));

        new FlagsDownloader().downloadCountryFlags();
        model.commit();
    }

}
