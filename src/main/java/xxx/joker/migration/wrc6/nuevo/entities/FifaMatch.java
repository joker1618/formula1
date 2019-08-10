package xxx.joker.migration.wrc6.nuevo.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class FifaMatch extends RepoEntity {

    @RepoField
    private String teamFede;
    @RepoField
    private int golFede;
    @RepoField
    private String teamBomber;
    @RepoField
    private int golBomber;

    public FifaMatch() {
    }

    public String strWinner() {
        int res = golFede - golBomber;
        return res == 0 ? "DRAW" : res < 0 ? "BOMBER" : "FEDE";
    }

    @Override
    public String getPrimaryKey() {
        return strf("fifa-match-%06d", getEntityId());
    }

    public String getTeamFede() {
        return teamFede;
    }

    public void setTeamFede(String teamFede) {
        this.teamFede = teamFede;
    }

    public String getTeamBomber() {
        return teamBomber;
    }

    public void setTeamBomber(String teamBomber) {
        this.teamBomber = teamBomber;
    }

    public int getGolFede() {
        return golFede;
    }

    public void setGolFede(int golFede) {
        this.golFede = golFede;
    }

    public int getGolBomber() {
        return golBomber;
    }

    public void setGolBomber(int golBomber) {
        this.golBomber = golBomber;
    }

}

