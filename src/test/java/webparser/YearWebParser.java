package webparser;

import org.junit.Test;
import xxx.joker.apps.formula1.webParser.AWebParser;
import xxx.joker.apps.formula1.webParser.WikiParser;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class YearWebParser extends AWebParser {

    @Test
    public void runYears() {
        int start = 1992;
        int end = 2018;
        for(int y = end; y >= start; y--) {
            runYear(y);
        }
        model.commit();
    }
    @Test
    public void runYear() {
        runYear(1991);
        model.commit();
    }

    private void runYear(int year) {
        display("####  Start parsing year {}", year);
        WikiParser parser = WikiParser.getParser(year);
        parser.parseYear();
    }

}
