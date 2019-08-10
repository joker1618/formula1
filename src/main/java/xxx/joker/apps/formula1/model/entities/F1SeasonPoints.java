package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class F1SeasonPoints extends RepoEntity {

    @RepoField
    private int year;
    @RepoField
    private String name;
    @RepoField
    private int finalPos;
    // Final points: some years this value differ from the computed one
    @RepoField
    private double finalPoints;

    public F1SeasonPoints() {

    }

    @Override
    public String getPrimaryKey() {
        return strf("{}-{}", year, name);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFinalPoints() {
        return finalPoints;
    }

    public int getFinalPos() {
        return finalPos;
    }

    public void setFinalPos(int finalPos) {
        this.finalPos = finalPos;
    }

    public void setFinalPoints(double finalPoints) {
        this.finalPoints = finalPoints;
    }
}