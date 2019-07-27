package xxx.joker.apps.formula1.fxgui.fxmodel;

import xxx.joker.apps.formula1.model.entities.F1Driver;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.apps.formula1.model.entities.F1Race;

import java.util.HashMap;
import java.util.Map;

public class ResultView {

    private F1Driver driver;
    private Map<F1GranPrix, F1Race> raceMap = new HashMap<>();

    public F1Driver getDriver() {
        return driver;
    }

    public void setDriver(F1Driver driver) {
        this.driver = driver;
    }

    public Double getRacePoints(F1GranPrix gp) {
        return raceMap.get(gp) != null ? raceMap.get(gp).getPoints() : 0d;
    }

    public Double getTotalPoints() {
        double sum = 0d;
        for (F1Race race : raceMap.values()) {
            if(race != null && race.getPoints() != null) {
                sum += race.getPoints();
            }
        }
        return sum;
    }

    public Map<F1GranPrix, F1Race> getRaceMap() {
        return raceMap;
    }

    public void setRaceMap(Map<F1GranPrix, F1Race> raceMap) {
        this.raceMap = raceMap;
    }
}
