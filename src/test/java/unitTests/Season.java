package unitTests;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class Season extends RepoEntity {

    @RepoField
    private Driver winner;
    @RepoField
    private List<Driver> drivers;
    @RepoField
    private Country country;

    public Season() {
    }

    public Season(Driver winner, List<Driver> drivers, Country country) {
        this.winner = winner;
        this.drivers = drivers;
        this.country = country;
    }

    @Override
    public String getPrimaryKey() {
        return strf("season-{}", entityId);
    }


    public Driver getWinner() {
        return winner;
    }

    public void setWinner(Driver winner) {
        this.winner = winner;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
