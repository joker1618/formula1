package xxx.joker.migration.wrc6.nuevo.entities;

import xxx.joker.migration.wrc6.nuevo.enums.WrcDriver;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import java.util.List;

public class WrcRally extends RepoEntity {

    @RepoField
    private WrcNation nation;
    @RepoField
    private Long seasonID;
    @RepoField
    private Integer rallyProgrInSeason;
    @RepoField
    private List<WrcMatch> matches;

    public WrcRally() {

    }
    public WrcRally(WrcNation nation) {
        this.nation = nation;
    }
    public WrcRally(WrcNation nation, Long seasonID) {
        this.nation = nation;
        this.seasonID = seasonID;
    }

    public WrcDriver getWinner() {
        int wf = getStageWins(WrcDriver.FEDE);
        int wb = getStageWins(WrcDriver.BOMBER);
        if(wf > wb) return WrcDriver.FEDE;
        if(wf < wb) return WrcDriver.BOMBER;
        return WrcDriver.NONE;
    }

    public int getStageWins(WrcDriver driver) {
        return JkStreams.filter(matches, m -> m.getWinner() == driver).size();
    }

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityId());
    }

    public WrcNation getNation() {
        return nation;
    }

    public void setNation(WrcNation nation) {
        this.nation = nation;
    }

    public Long getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Long seasonID) {
        this.seasonID = seasonID;
    }

    public List<WrcMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<WrcMatch> matches) {
        this.matches = matches;
    }

    public Integer getRallyProgrInSeason() {
        return rallyProgrInSeason;
    }

    public void setRallyProgrInSeason(Integer rallyProgrInSeason) {
        this.rallyProgrInSeason = rallyProgrInSeason;
    }
}
