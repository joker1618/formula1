package xxx.joker.apps.formula1.fxgui.fxservice;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine;
import static xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine.StatKind.*;
import static xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine.StatKind;
import static xxx.joker.libs.core.utils.JkConsole.display;

import xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine.StatsCell;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.libs.core.datetime.JkTimer;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.datalayer.design.RepoEntity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsComputer {

    private static final Logger LOG = LoggerFactory.getLogger(StatsComputer.class);

    private F1Model model = F1ModelImpl.getInstance();

    public Pair<List<StatKind>, List<StatsLine>> computeByDriver(List<F1GranPrix> gpList) {

        List<F1Driver> allDrivers = extractDrivers(gpList);
        List<RepoEntity> allKeys = JkStreams.map(allDrivers, d -> d);

        Map<F1Driver, Integer> seasonNums = JkStreams.toMapSingle(allDrivers, d -> d, d -> JkStreams.filterMapSortUniq(model.getEntrants(), e -> e.getDriver().equals(d), F1Entrant::getYear).size());
        Map<RepoEntity, StatsCell> seasonMap = JkStreams.toMapSingle(seasonNums.entrySet(), Map.Entry::getKey, e -> new StatsCell(e.getValue()));
        AtomicInteger pos = new AtomicInteger(1);
        JkStreams.reverseOrder(seasonMap.values(), Comparator.comparingDouble(StatsCell::getValue)).forEach(sc -> sc.setPos(pos.getAndIncrement()));

        Map<String, List<F1SeasonPoints>> winSeasons = JkStreams.toMap(model.getSeasonPoints(), F1SeasonPoints::getName, sp -> sp, sp -> sp.getFinalPos() == 1);
        Map<RepoEntity, StatsCell> winSeasonMap = JkStreams.toMapSingle(allDrivers, d -> d, d -> new StatsCell(winSeasons.getOrDefault(d.getFullName(), Collections.emptyList()).size()));
        pos.set(1);
        JkStreams.reverseOrder(winSeasonMap.values(), Comparator.comparingDouble(StatsCell::getValue)).forEach(sc -> sc.setPos(pos.getAndIncrement()));

        List<F1Race> raceList = JkStreams.flatMap(gpList, F1GranPrix::getRaces);
        Map<RepoEntity, StatsCell> raceNumMap = countElems(raceList, r -> r.getEntrant().getDriver(), allKeys);
        Map<RepoEntity, StatsCell> poleMap = countElems(gpList, this::getPoleDriver, allKeys);
        Map<RepoEntity, StatsCell> p1Map = countElems(gpList, gp -> getDriverAtPos(gp, 1), allKeys);
        Map<RepoEntity, StatsCell> p2Map = countElems(gpList, gp -> getDriverAtPos(gp, 2), allKeys);
        Map<RepoEntity, StatsCell> p3Map = countElems(gpList, gp -> getDriverAtPos(gp, 3), allKeys);
        Map<RepoEntity, StatsCell> fastLapMap = countElems(gpList, this::getFastLapDriver, allKeys);

        List<F1Race> filterRaces = JkStreams.filter(raceList, r -> r.getPos() > 0 && r.getPos() <= 3);
        Map<RepoEntity, StatsCell> podiumMap = countElems(filterRaces, r -> r.getEntrant().getDriver(), allKeys);

        List<StatsLine> svList = new ArrayList<>();
        List<StatKind> skList = new ArrayList<>();
        for (F1Driver d : allDrivers) {
            StatsLine sv = new StatsLine();
            sv.setStatName(d.getFullName());
            sv.getStatsCells().put(NUM_SEASONS, seasonMap.get(d));
            sv.getStatsCells().put(WIN_SEASONS, winSeasonMap.get(d));
            sv.getStatsCells().put(NUM_RACES, raceNumMap.get(d));
            sv.getStatsCells().put(NUM_P1, p1Map.get(d));
            sv.getStatsCells().put(NUM_P2, p2Map.get(d));
            sv.getStatsCells().put(NUM_P3, p3Map.get(d));
            sv.getStatsCells().put(NUM_POLE, poleMap.get(d));
            sv.getStatsCells().put(NUM_PODIUMS, podiumMap.get(d));
            sv.getStatsCells().put(NUM_FAST_LAPS, fastLapMap.get(d));
            svList.add(sv);
            if(skList.isEmpty()) {
                sv.getStatsCells().forEach((k,v) -> skList.add(k));
            }
        }

        return Pair.of(skList, svList);
    }

    private <T> Map<RepoEntity, StatsCell> countElems(List<T> list, Function<T, RepoEntity> keyMapper, List<RepoEntity> allKeys) {
        Map<RepoEntity, List<T>> map = JkStreams.toMap(list, keyMapper);
        Map<RepoEntity, Integer> numMap = JkStreams.toMapSingle(map.entrySet(), Map.Entry::getKey, e -> e.getValue().size());
        allKeys.forEach(d -> numMap.putIfAbsent(d, 0));
        Map<RepoEntity, StatsCell> statCells = new TreeMap<>();
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
    private F1Driver getDriverAtPos(F1GranPrix gp, int pos) {
        return gp.getRaces().get(pos-1).getEntrant().getDriver();
    }
    private F1Driver getFastLapDriver(F1GranPrix gp) {
        String dpk = gp.getFastLap().getDriverPK();
        F1Driver driver = model.get(F1Driver.class, d -> dpk.equals(d.getPrimaryKey()));
        return driver;
    }
}
