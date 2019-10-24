package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class F1Entrant extends RepoEntity {

    @EntityField
    private Integer year;
    @EntityField
    private F1Team team;
    @EntityField
    private Integer carNo;
    @EntityField
    private F1Driver driver;

    @Override
    public String getPrimaryKey() {
        return strf("{}-{}-{}-{}", year, team.getTeamName(), driver.getFullName(), carNo);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public F1Team getTeam() {
        return team;
    }

    public void setTeam(F1Team team) {
        this.team = team;
    }

    public Integer getCarNo() {
        return carNo;
    }

    public void setCarNo(Integer carNo) {
        this.carNo = carNo;
    }

    public F1Driver getDriver() {
        return driver;
    }

    public void setDriver(F1Driver driver) {
        this.driver = driver;
    }
}
