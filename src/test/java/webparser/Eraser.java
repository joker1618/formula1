package webparser;

import common.CommonTest;
import org.junit.BeforeClass;
import org.junit.Test;

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
