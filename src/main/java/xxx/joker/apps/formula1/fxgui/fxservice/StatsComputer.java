package xxx.joker.apps.formula1.fxgui.fxservice;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsView;
import static xxx.joker.apps.formula1.fxgui.fxmodel.StatsView.StatKind;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsView.StatsCell;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.apps.formula1.model.entities.F1Driver;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.model.entities.F1Qualify;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.libs.core.datetime.JkDuration;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsComputer {

    private static final Logger LOG = LoggerFactory.getLogger(StatsComputer.class);

//    public enum StatKind {
//        NUM_RACES("NR"),
//        NUM_FIRST("P1"),
//        NUM_PODIUM("Pod"),
//        NUM_POLE("Pole"),
//        NUM_FAST_LAPS("FLap"),
//
//        ;
//        private String label;
//
//        StatKind(String label) {
//            this.label = label;
//        }
//    }

    private F1Model model = F1ModelImpl.getInstance();

    public List<StatsView> computeByDriver(List<F1GranPrix> gpList) {
        List<StatsView> svList = new ArrayList<>();
        List<F1Driver> allDrivers = extractDrivers(gpList);

        Map<F1Driver, StatsCell> raceNumMap = countElems(JkStreams.flatMap(gpList, F1GranPrix::getRaces), r -> r.getEntrant().getDriver(), allDrivers);
        Map<F1Driver, StatsCell> poleMap = countElems(gpList, this::getPoleDriver, allDrivers);
        Map<F1Driver, StatsCell> p1Map = countElems(gpList, gp -> getNumPosition(gp, 1), allDrivers);
        Map<F1Driver, StatsCell> p2Map = countElems(gpList, gp -> getNumPosition(gp, 2), allDrivers);
        Map<F1Driver, StatsCell> p3Map = countElems(gpList, gp -> getNumPosition(gp, 3), allDrivers);
        Map<F1Driver, StatsCell> fastLapMap = countElems(gpList, this::getFastLapDriver, allDrivers);

//        for (F1Driver d : allDrivers) {
//            double numPodium = p1Map.get(d).getValue() + p2Map.get(d).getValue() + p3Map.get(d).getValue();
//            new Sta
//        }
        for (F1Driver d : allDrivers) {
            StatsView sv = new StatsView();
            sv.setStatName(d.getFullName());
            sv.getStatsCells().put(StatKind.NUM_RACES, raceNumMap.get(d));
            sv.getStatsCells().put(StatKind.NUM_FIRST, p1Map.get(d));
            sv.getStatsCells().put(StatKind.NUM_POLE, poleMap.get(d));
            sv.getStatsCells().put(StatKind.NUM_FAST_LAPS, fastLapMap.get(d));
//            double numPodium = p1Map.get(d).getValue() + p2Map.get(d).getValue() + p3Map.get(d).getValue();
            svList.add(sv);
        }

        return svList;
    }

    private <T> Map<F1Driver, StatsCell> countElems(List<T> list, Function<T, F1Driver> keyMapper, List<F1Driver> allDrivers) {
        Map<F1Driver, List<T>> map = JkStreams.toMap(list, keyMapper);
        Map<F1Driver, Integer> numMap = JkStreams.toMapSingle(map.entrySet(), Map.Entry::getKey, e -> e.getValue().size());
        allDrivers.forEach(d -> numMap.putIfAbsent(d, 0));
//        numMap.remove(null);
        Map<F1Driver, StatsCell> statCells = new TreeMap<>();
        AtomicInteger counter = new AtomicInteger(1);
        JkStreams.reverseOrder(numMap.entrySet(), Comparator.comparing(Map.Entry::getValue)).forEach(e -> {
            StatsCell sc = new StatsCell(counter.getAndIncrement(), e.getValue());
            statCells.put(e.getKey(), sc);
        });
        return statCells;
    }

    private List<F1Driver> extractDrivers(Collection<F1GranPrix> gpList) {
        List<F1Driver> drivers = gpList.stream()
                .flatMap(gp -> gp.getQualifies().stream())
                .map(q -> q.getEntrant().getDriver())
                .sorted()
                .collect(Collectors.toList());
        drivers.addAll(gpList.stream()
                .flatMap(gp -> gp.getRaces().stream())
                .map(q -> q.getEntrant().getDriver())
                .sorted()
                .collect(Collectors.toList()));
        return JkStreams.sortUniq(drivers);
    }

    private F1Driver getPoleDriver(F1GranPrix gp) {
        F1Qualify qual = gp.getQualifies().get(0);
        return qual.getEntrant().getDriver();
    }
    private F1Driver getNumPosition(F1GranPrix gp, int pos) {
        return gp.getRaces().get(pos-1).getEntrant().getDriver();
    }
    private F1Driver getFastLapDriver(F1GranPrix gp) {
        String dpk = gp.getFastLap().getDriverPK();
        F1Driver driver = model.get(F1Driver.class, d -> dpk.equals(d.getPrimaryKey()));
        return driver;
    }
}
