package xxx.joker.apps.formula1.fxgui.fxmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsView {

    public enum StatKind {
        NUM_RACES("NR"),
        NUM_FIRST("P1"),
//        NUM_PODIUM("Pod"),
        NUM_POLE("Pole"),
        NUM_FAST_LAPS("FLap"),

        ;
        private String label;

        StatKind(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private String statName;
    private Map<StatKind, StatsCell> statsCells = new HashMap<>();

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public Map<StatKind, StatsCell> getStatsCells() {
        return statsCells;
    }
    public StatsCell getStatsCell(StatKind statKind) {
        return statsCells.get(statKind);
    }

    public void setStatsCells(Map<StatKind, StatsCell> statsCells) {
        this.statsCells = statsCells;
    }

    public static class StatsCell implements Comparable<StatsCell> {
        private int pos;
        private double value;
//        private double perc;


        public StatsCell() {
        }

        public StatsCell(int pos, double value) {
            this.pos = pos;
            this.value = value;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

//        public double getPerc() {
//            return perc;
//        }
//
//        public void setPerc(double perc) {
//            this.perc = perc;
//        }

        @Override
        public int compareTo(StatsCell o) {
            if(o.getPos() <= 0) {
                return pos > 0 ? -1 : pos - o.getPos();
            }
            if(pos <= 0) return 1;
            return pos - o.getPos();
        }
    }
}
