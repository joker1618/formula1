package xxx.joker.apps.formula1.fxgui.fxmodel;

import java.util.ArrayList;
import java.util.List;

public class SeasonView {

    private int year;
    private List<ResultView> driverResViews = new ArrayList<>();


    public SeasonView(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ResultView> getDriverResViews() {
        return driverResViews;
    }

    public void setDriverResViews(List<ResultView> driverResViews) {
        this.driverResViews = driverResViews;
    }
}
