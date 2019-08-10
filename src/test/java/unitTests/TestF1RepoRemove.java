package unitTests;

import common.CommonTest;
import org.junit.Test;
import xxx.joker.apps.formula1.model.entities.F1Circuit;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.utils.JkConsole;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.export.TmpFmt;
import xxx.joker.libs.datalayer.util.RepoUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkConsole.displayColl;

public class TestF1RepoRemove extends CommonTest {

    @Test
    public void doTestRemoveCircuits() {
        printRepo(F1Circuit.class, F1GranPrix.class);

        Set<F1Circuit> ds = model.getDataSet(F1Circuit.class);
//        ds.forEach(model::remove);  // concurrent exception
//        JkConvert.toList(ds).forEach(model::remove);
//        model.removeAll(ds);
//        ds.clear();
        ds.removeIf(c -> c.getCountry().contains("i"));
//        List<F1Circuit> clist = model.getList(F1Circuit.class).subList(0, 10);
//        clist.forEach(model::remove);  // concurrent exception
//        model.removeAll(clist);

        display("Removed circuits");
        printRepo(F1Circuit.class, F1GranPrix.class);

    }

    @Test
    public void doTestRemoveRaces() {
        printRepo(F1GranPrix.class, F1Race.class);

//        Set<F1Race> ds = model.getDataSet(F1Race.class);
//        model.removeAll(ds);
//        ds.clear();
//        JkConvert.toList(ds).forEach(model::remove);
//        ds.removeIf(c -> c.getPos() == 1);
        List<F1Race> clist = model.getList(F1Race.class).subList(0, 44);
        clist.forEach(model::remove);  // concurrent exception
//        model.removeAll(clist);

        display("Removed races");
        printRepo(F1GranPrix.class, F1Race.class);

    }
}
