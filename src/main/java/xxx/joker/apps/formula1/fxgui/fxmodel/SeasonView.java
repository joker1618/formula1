package xxx.joker.apps.formula1.fxgui.fxmodel;

import java.util.ArrayList;
import java.util.List;

public class SeasonView {

    private int year;
    private List<ResultView> driverResults = new ArrayList<>();
    private List<ResultView> teamResults = new ArrayList<>();


    public SeasonView(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ResultView> getDriverResults() {
        return driverResults;
    }

    public void setDriverResults(List<ResultView> driverResults) {
        this.driverResults = driverResults;
    }

    public List<ResultView> getTeamResults() {
        return teamResults;
    }

    public void setTeamResults(List<ResultView> teamResults) {
        this.teamResults = teamResults;
    }
}
