package xxx.joker.apps.formula1.fxgui.fxmodel;

import static xxx.joker.apps.formula1.common.F1Util.safePrint;
import static xxx.joker.apps.formula1.common.F1Util.safePrint0;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class ResultCellPoints implements Comparable<ResultCellPoints> {
    private int pos;
    private double value;
    private double computedPoints;

    public ResultCellPoints() {
    }

    public ResultCellPoints(int pos, double computedPoints) {
        this.pos = pos;
        this.computedPoints = computedPoints;
    }

    public ResultCellPoints(int pos, double value, double computedPoints) {
        this.pos = pos;
        this.value = value;
        this.computedPoints = computedPoints;
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

    public double getComputedPoints() {
        return computedPoints;
    }

    public void setComputedPoints(double computedPoints) {
        this.computedPoints = computedPoints;
    }

    @Override
    public int compareTo(ResultCellPoints o) {
        if(o.getPos() <= 0) return pos > 0 ? -1 : pos - o.getPos();
        if(pos <= 0) return 1;
        return pos - o.getPos();
    }

    public String toStringTotPoints() {
        String str = strf("{}", safePrint(getValue()));
        if(getValue() != getComputedPoints()) {
            str += strf(" ({})", safePrint(getComputedPoints()));
        }
        return str;
    }

}