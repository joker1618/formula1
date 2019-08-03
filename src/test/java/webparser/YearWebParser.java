package webparser;

import org.junit.Test;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.webParser.AWebParser;
import xxx.joker.apps.formula1.webParser.WikiParser;

import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class YearWebParser extends CommonTest {

    @Test
    public void runYears() {
        int start = 2014;
        int end = 2018;
        for(int y = end; y >= start; y--) {
            runYear(y);
        }
        model.commit();
    }
    @Test
    public void runYear() {
        int y = 1986;

        List<F1GranPrix> gpList = model.getGranPrixs(y);
        model.removeAll(gpList);
        gpList.forEach(gp -> model.removeAll(gp.getQualifies()));
        gpList.forEach(gp -> model.removeAll(gp.getRaces()));

        runYear(y);
        model.commit();
    }

    private void runYear(int year) {
        display("####  Start parsing year {}", year);
        WikiParser parser = WikiParser.getParser(year);
        parser.parseYear();
    }

}
