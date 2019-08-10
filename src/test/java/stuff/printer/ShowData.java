package stuff.printer;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import common.CommonTest;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class ShowData extends CommonTest {

    static int defYear = -1;

    @BeforeClass
    public static void beforeClazz() {
//        defYear = 2015;
//        defYear = JkStruct.getLastElem(F1ModelNewImpl.getInstance().getAvailableYears());
    }

    @Test
    public void printAvailableYears() {
        List<Integer> years = model.getAvailableYears();
        if(years.isEmpty()) {
            display("No years available");
        } else {
            int totNum = years.size();
            display("Available years from {} to {}  ({})", years.get(0), years.get(totNum -1), totNum);
        }
    }

    @Test
    public void printAll() {
        printAllRepo();
    }

    @Test
    public void printCountries() {
        printRepo(F1Country.class);
    }

    @Test
    public void printSeason() {
        int year = 1987;
        printRepo(model.getEntrants(year));
        List<F1GranPrix> gpList = model.getGranPrixs(year);
        printRepo(gpList);
        printRepo(JkStreams.flatMap(gpList, F1GranPrix::getQualifies));
        printRepo(JkStreams.flatMap(gpList, F1GranPrix::getRaces));
    }

    @Test
    public void singleEntrants() {
        int year = 2018;
        List<F1Entrant> entrants = model.getEntrants(defYear == -1 ? year : defYear);
        printRepo(entrants);
    }

    @Test
    public void doShowCircuits() {
        printRepo(model.getCircuits());
    }

    @Test
    public void doShowTeams() {
        printRepo(model.getTeams());
    }

    @Test
    public void doShowDrivers() {
        printRepo(model.getDrivers());
    }

    @Test
    public void allGranPrixs() {
        model.getAvailableYears().forEach(year -> {
            List<F1GranPrix> gpList = model.getGranPrixs(year);
            printRepo(gpList);
        });
    }
    @Test
    public void singleGranPrixs() {
        int year = 2018;
        List<F1GranPrix> gpList = model.getGranPrixs(defYear == -1 ? year : defYear);
        printRepo(gpList);
    }

    @Test
    public void singleQualifiesRaces() {
        int year = 2018;
        List<F1GranPrix> gpList = model.getGranPrixs(defYear == -1 ? year : defYear);
        for (F1GranPrix gp : gpList) {
            printRepo(gp.getQualifies());
            printRepo(gp.getRaces());
        }
    }

    @Test
    public void allCheckPoints() {
        model.getAvailableYears().forEach(this::checkPoints);
    }

    @Test
    public void singleCheckPoints() {
        int year = 1983;
        checkPoints(defYear == -1 ? year : defYear);
    }
    public void checkPoints(int year) {
        List<F1GranPrix> gp = model.getGranPrixs(year);
        List<F1Race> races = gp.stream().flatMap(g -> g.getRaces().stream()).collect(Collectors.toList());

        List<F1SeasonPoints> seasonPoints = model.getSeasonPoints(year);
        Map<String, Double> expected = JkStreams.toMapSingle(seasonPoints, F1SeasonPoints::getName, F1SeasonPoints::getFinalPoints);

        Map<String, List<F1Race>> byDriver = JkStreams.toMap(races, r -> r.getEntrant().getDriver().getFullName());
        printList(year, "DRIVER", byDriver, expected);

        Map<String, List<F1Race>> byTeam = JkStreams.toMap(races, r -> r.getEntrant().getTeam().getTeamName());
        printList(year, "TEAM", byTeam, expected);
    }

    public void printList(int year, String label, Map<String, List<F1Race>> raceMap, Map<String, Double> expected) {
        List<Pair<String,Double>> list = new ArrayList<>();
        raceMap.forEach((k,v) -> list.add(Pair.of(k, v.stream().mapToDouble(F1Race::getPoints).sum())));
        List<Pair<String, Double>> sorted = JkStreams.reverseOrder(list, Comparator.comparingDouble(Pair::getValue));
        List<String> lines = new ArrayList<>();
        lines.add(label+"|EXPECTED||COMPUTED");
        for (Pair<String, Double> pair : sorted) {
            Double exp = expected.get(pair.getKey());
            String line = strf("{}|{}|{}|{}", pair.getKey(), exp, exp.doubleValue() == pair.getValue() ? "" : "<>", pair.getValue());
            lines.add(line);
        }
        display("{} POINT CHECK {}\n{}", label, year, JkStrings.leftPadLines(JkOutput.columnsView(lines, "|", 2), " ", 2));
    }

}
