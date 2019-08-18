package xxx.joker.apps.formula1.fxgui.fxmodel;

import java.util.*;

public class StatsLine {

    public enum StatKind {
        NUM_SEASONS("Season"),
        WIN_SEASONS("Champion"),
        NUM_RACES("Race"),
        NUM_P1("P1"),
        NUM_P2("P2"),
        NUM_P3("P3"),
        NUM_PODIUMS("Podium"),
        NUM_POLE("Pole"),
        NUM_FAST_LAPS("Fast Lap"),
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
    private Map<StatKind, StatsCell> statsCells = new LinkedHashMap<>();

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


        public StatsCell() {
            this(-1, -1d);
        }

        public StatsCell(double value) {
            this.pos = -1;
            this.value = value;
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
