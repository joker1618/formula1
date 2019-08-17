package webparser;

import common.CommonTest;
import org.junit.Test;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.model.entities.F1Qualify;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.apps.formula1.webParser.WikiParser;
import xxx.joker.libs.core.debug.JkDebug;

import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class YearWebParser extends CommonTest {

    @Test
    public void runYears() {
//        model.getSeasonPoints().clear();

//        int start = 1982;
//        int end = 2018;

        int start = 1982;
        int end = 1984;

        for(int y = end; y >= start; y--) {
            runYear(y);
        }

        model.commit();
    }
    @Test
    public void runYear() {
        int y = 2009;

        JkDebug.startTimer("1111");
        List<F1GranPrix> gpList = model.getGranPrixs(y);
        model.removeAll(model.getSeasonPointsMap(y).values());
        JkDebug.stopAndStartTimer("1111", "2222");
        gpList.forEach(gp -> model.removeAll(gp.getRaces()));
        JkDebug.stopAndStartTimer("2222", "3333");
        gpList.forEach(gp -> model.removeAll(gp.getQualifies()));
        JkDebug.stopAndStartTimer("3333", "4444");
        model.removeAll(gpList);
        JkDebug.stopTimer("4444");

//        display(model.toStringClass(F1GranPrix.class));
//        display(model.toStringClass(F1Qualify.class));
//        display(model.toStringClass(F1Race.class));
        display(model.toStringEntities(model.getGranPrixs(y)));

//        runYear(y);
//        model.commit();

//        JkDebug.startTimer("1111");
//        gpList = model.getGranPrixs(y);
//        model.removeAll(model.getSeasonPointsMap(y).values());
//        JkDebug.stopAndStartTimer("1111", "2222");
//        gpList.forEach(gp -> model.removeAll(gp.getRaces()));
//        JkDebug.stopAndStartTimer("2222", "3333");
//        gpList.forEach(gp -> model.removeAll(gp.getQualifies()));
//        JkDebug.stopAndStartTimer("3333", "4444");
//        model.removeAll(gpList);
//        JkDebug.stopTimer("4444");


    }

    private void runYear(int year) {
        display("####  Start parsing year {}", year);
        WikiParser parser = WikiParser.getParser(year);
        parser.parseYear();
    }

}
