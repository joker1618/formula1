package webparser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import xxx.joker.apps.formula1.model.F1ModelNew;
import xxx.joker.apps.formula1.model.F1ModelNewImpl;
import xxx.joker.libs.core.datetime.JkDateTime;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.entities.RepoUri;
import xxx.joker.libs.repository.export.TMPCsvParser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public abstract class CommonTest {

    protected F1ModelNew model = F1ModelNewImpl.getInstance();
    protected TMPCsvParser csvParser = TMPCsvParser.get();

    @BeforeClass
    public static void beforeClass() {
//        JkEnvironment.setAppsFolder("");
        JkDebug.startTimer("global");
    }

    @Before
    public void before() {
        // csv configs
        csvParser.addCustomClassFormat(JkDateTime.class, d -> d.format("yyyyMMdd_HHmmss"));
        csvParser.addCustomClassFormat(LocalDateTime.class, d -> JkDateTime.of(d).format("yyyyMMdd_HHmmss"));
        csvParser.addCustomClassFormat(RepoUri.class, u -> strf("uri[{}]", u.getEntityId()));
        csvParser.addCustomInstanceFormat(RepoEntity.class, RepoEntity::strMini);
    }

    @AfterClass
    public static void afterClass() {
        JkDebug.stopTimer("global");
        JkDebug.displayTimes();
    }

    public void printAllRepo() {
        model.getDataSets().forEach((c, elist) -> {
            display("{}  ({})", c.getSimpleName(), elist.size());
            List<String> lines = csvParser.formatCsv(elist);
            display("{}\n", JkOutput.columnsView(lines));
        });
    }
    public void printRepo(Class<?>... classes) {
        for (Class<?> c : classes) {
            Set<RepoEntity> elist = model.getDataSet((Class<RepoEntity>) c);
            display("{}  ({})", c.getSimpleName(), elist.size());
            List<String> lines = csvParser.formatCsv(elist);
            display("{}\n", JkOutput.columnsView(lines));
        }
    }
    public <T extends RepoEntity> void printRepo(Collection<T> entities) {
        if(!entities.isEmpty()) {
            List<T> eList = JkConvert.toList(entities);
            Class<? extends RepoEntity> clazz = eList.get(0).getClass();
            display("{}  ({})", clazz.getSimpleName(), eList.size());
            List<String> lines = csvParser.formatCsv(eList);
            display("{}\n", JkOutput.columnsView(lines));
        }
    }

}
