package xxx.joker.apps.formula1.fxgui.fxmodel;

import java.util.Comparator;

import static xxx.joker.apps.formula1.common.F1Util.safePrint;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class ResultCellPoints implements Comparable<ResultCellPoints> {
    private int pos;
    private double expectedPoints;
    private double computedPoints;

    public ResultCellPoints() {
    }

    public ResultCellPoints(int pos, double computedPoints) {
        this.pos = pos;
        this.computedPoints = computedPoints;
    }

    public ResultCellPoints(int pos, double expectedPoints, double computedPoints) {
        this.pos = pos;
        this.expectedPoints = expectedPoints;
        this.computedPoints = computedPoints;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public double getExpectedPoints() {
        return expectedPoints;
    }

    public void setExpectedPoints(double expectedPoints) {
        this.expectedPoints = expectedPoints;
    }

    public double getComputedPoints() {
        return computedPoints;
    }

    public void setComputedPoints(double computedPoints) {
        this.computedPoints = computedPoints;
    }

    @Override
    public int compareTo(ResultCellPoints o) {
        return pos - o.getPos();
    }

    public String toStringTotPoints() {
        String str = strf("{}", safePrint(getExpectedPoints()));
        if(getExpectedPoints() != getComputedPoints()) {
            str += strf(" ({})", safePrint(getComputedPoints()));
        }
        return str;
    }

}