package xxx.joker.migration.wrc6.nuevo.entities;

import xxx.joker.migration.wrc6.nuevo.enums.WrcDriver;
import xxx.joker.migration.wrc6.nuevo.enums.WrcTime;
import xxx.joker.migration.wrc6.nuevo.enums.WrcWeather;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class WrcMatch extends RepoEntity {

    @RepoField
    private WrcNation nation;
    @RepoField
    private Long seasonID;
    @RepoField
    private Long rallyID;
    @RepoField
    private Integer stageProgrInRally;
    @RepoField
    private WrcStage stage;
    @RepoField
    private WrcDriver winner;
    @RepoField
    private WrcCar carFede;
    @RepoField
    private WrcCar carBomber;
    @RepoField
    private WrcWeather weather;
    @RepoField
    private WrcTime time;

    public WrcMatch() {

    }
    public WrcMatch(WrcNation nation, WrcDriver winner) {
        this.nation = nation;
        this.winner = winner;
    }

    @Override
    public String getPrimaryKey() {
        return strf("match-%04d", getEntityId());
    }

    public WrcNation getNation() {
        return nation;
    }

    public void setNation(WrcNation nation) {
        this.nation = nation;
    }

    public Long getRallyID() {
        return rallyID;
    }

    public void setRallyID(Long rallyID) {
        this.rallyID = rallyID;
    }

    public WrcDriver getWinner() {
        return winner;
    }

    public void setWinner(WrcDriver winner) {
        this.winner = winner;
    }

    public Long getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Long seasonID) {
        this.seasonID = seasonID;
    }

    public WrcCar getCarFede() {
        return carFede;
    }

    public void setCarFede(WrcCar carFede) {
        this.carFede = carFede;
    }

    public Integer getStageProgrInRally() {
        return stageProgrInRally;
    }

    public void setStageProgrInRally(Integer stageProgrInRally) {
        this.stageProgrInRally = stageProgrInRally;
    }

    public WrcCar getCarBomber() {
        return carBomber;
    }

    public void setCarBomber(WrcCar carBomber) {
        this.carBomber = carBomber;
    }

    public WrcWeather getWeather() {
        return weather;
    }

    public void setWeather(WrcWeather weather) {
        this.weather = weather;
    }

    public WrcTime getTime() {
        return time;
    }

    public void setTime(WrcTime time) {
        this.time = time;
    }

    public WrcStage getStage() {
        return stage;
    }

    public void setStage(WrcStage stage) {
        this.stage = stage;
    }
}
