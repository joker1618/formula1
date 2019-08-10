package common;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.entities.RepoUri;
import xxx.joker.libs.datalayer.export.TmpFmt;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public abstract class CommonTest {

    protected F1Model model;
    protected TmpFmt csvParser = TmpFmt.get();

    @BeforeClass
    public static void beforeClass() {
//        JkEnvironment.setAppsFolder("");
        JkDebug.startTimer("global");
    }

    @Before
    public void before() {
        model = F1ModelImpl.getInstance();
        // csv configs
        csvParser.addCustomClassFormat(JkDateTime.class, d -> d.format("yyyyMMdd_HHmmss"));
        csvParser.addCustomClassFormat(LocalDateTime.class, d -> JkDateTime.of(d).format("yyyyMMdd_HHmmss"));
        csvParser.addCustomClassFormat(RepoUri.class, u -> strf("uri[{}]", u.getEntityId()));
        csvParser.addCustomInstanceFormat(RepoEntity.class, RepoEntity::strMini);
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
