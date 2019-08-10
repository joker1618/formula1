package xxx.joker.apps.formula1.fxgui.fxmodel;

import xxx.joker.apps.formula1.model.entities.F1GranPrix;

import java.util.HashMap;
import java.util.Map;

public class ResultView implements Comparable<ResultView> {

    private String entryName;
//    private Map<F1GranPrix, F1Race> raceMap = new HashMap<>();
    private Map<F1GranPrix, ResultCellPoints> raceMap = new HashMap<>();
    private ResultCellPoints total;

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public Map<F1GranPrix, ResultCellPoints> getCellMap() {
        return raceMap;
    }

    public void setRaceMap(Map<F1GranPrix, ResultCellPoints> raceMap) {
        this.raceMap = raceMap;
    }

    public ResultCellPoints getTotal() {
        return total;
    }

    public void setTotal(ResultCellPoints total) {
        this.total = total;
    }

    @Override
    public int compareTo(ResultView o) {
        return total.compareTo(o.getTotal());
    }
}

