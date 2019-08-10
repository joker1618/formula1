package common;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.format.JkFormatter;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.datalayer.design.RepoEntity;

import java.util.Collection;

import static xxx.joker.libs.core.utils.JkConsole.display;

public abstract class CommonTest {

    protected F1Model model;
    protected JkFormatter fmt = JkFormatter.get();

    @BeforeClass
    public static void beforeClass() {
//        JkEnvironment.setAppsFolder("");
        JkDebug.startTimer("global");
    }

    @Before
    public void before() {
        model = F1ModelImpl.getInstance();
        // csv configs
        JkDebug.startTimer("unitTest");
    }

    @AfterClass
    public static void afterClass() {
        JkDebug.stopTimer("global");
        JkDebug.displayTimes();
    }

    @After
    public void after() {
        JkDebug.stopTimer("unitTest");
    }

    public void printAllRepo() {
        display(model.toStringRepo());
    }
    public void printRepo(Class<?>... classes) {
        display(model.toStringRepo(classes));
    }
    public <T extends RepoEntity> void printRepo(Collection<T> entities) {
        if(entities.isEmpty()) {
            display("<empty list>");
        }
        Class<?> c = JkConvert.toList(entities).get(0).getClass();
        display(model.toStringRepo(c));
    }

}
