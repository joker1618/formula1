package checkers;

import org.junit.Test;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.apps.formula1.webParser.AWebParser;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class Finder extends AWebParser {

    @Test
    public void findNonIntPoints() {
        display("RACES WITH NON-INT POINTS");
        for (F1GranPrix gp : model.getGranPrixs()) {
            for (F1Race race : gp.getRaces()) {
                int intCasted = race.getPoints().intValue();
                if(intCasted != race.getPoints()) {
                    display("{}", race);
                }
            }
        }
    }
}
