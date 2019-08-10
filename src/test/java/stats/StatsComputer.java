package stats;

import common.CommonTest;
import org.junit.Test;
import xxx.joker.apps.formula1.model.entities.F1Circuit;
import xxx.joker.apps.formula1.model.entities.F1Driver;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.libs.core.format.JkFormatter;
import xxx.joker.libs.core.format.JkOutput;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class StatsComputer extends CommonTest {

    @Test
    public void moreWins() {

        Map<F1Driver, Stats> statsMap = new TreeMap<>();
        for (F1GranPrix gp : model.getGranPrixs()) {
            F1Driver dqual = gp.getQualifies().get(0).getEntrant().getDriver();
            statsMap.putIfAbsent(dqual, new Stats(dqual.getFullName()));
            statsMap.get(dqual).incrNumPole();

            F1Driver drace = gp.getRaces().get(0).getEntrant().getDriver();
            statsMap.putIfAbsent(drace, new Stats(drace.getFullName()));
            statsMap.get(drace).incrNumWin();

            gp.getRaces().forEach(r -> {
                F1Driver d = r.getEntrant().getDriver();
                statsMap.putIfAbsent(d, new Stats(d.getFullName()));
                statsMap.get(d).incrNumRace();
            } );
        }

        List<String> lines = fmt.formatCsvExclude(statsMap.values(), "this$0");
        display(JkOutput.columnsView(lines));

    }

    private class Stats {
        private String driverName;
        private int numRace;
        private int numWin;
        private int numPole;

        public Stats() {
        }

        public Stats(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public int getNumRace() {
            return numRace;
        }

        public void setNumRace(int numRace) {
            this.numRace = numRace;
        }
        public void incrNumRace() {
            this.numRace++;
        }

        public int getNumWin() {
            return numWin;
        }

        public void incrNumWin() {
            this.numWin++;
        }

        public int getNumPole() {
            return numPole;
        }

        public void incrNumPole() {
            this.numPole++;
        }
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
