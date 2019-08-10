package xxx.joker.migration.wrc6.nuevo.entities;

import xxx.joker.migration.wrc6.nuevo.enums.WrcDriver;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import java.util.List;

import static xxx.joker.migration.wrc6.nuevo.enums.WrcDriver.*;

public class WrcSeason extends RepoEntity {

    @RepoField
    private boolean finished;
    @RepoField
    private List<WrcRally> rallyList;

    /**
     * The winner is the driver that:
     * - Win more rallies
     * - If equals rally win, the driver with more stages win
     * - If equals rally and stage win, the driver that win the last rally before
     */
    public WrcDriver getWinner() {
        if(isFinished()) {
            int res = getRallyWins(FEDE) - getRallyWins(BOMBER);
            if(res == 0) {
                res = getStageWins(FEDE) - getStageWins(BOMBER);
            }
            if(res != 0) {
                return res > 0 ? FEDE : BOMBER;
            }

            int idx = getRallyList().size() - 1;
            WrcDriver lastWinner = NONE;
            for(int i = getRallyList().size() - 1; lastWinner == NONE && i >= 0; i--) {
                lastWinner = getRallyList().get(i).getWinner();
            }
            if(lastWinner != NONE) {
                return lastWinner == BOMBER ? FEDE : BOMBER;
            }
        }
        return NONE;
    }

    public int getRallyWins(WrcDriver driver) {
        return JkStreams.filter(rallyList, m -> m.getWinner() == driver).size();
    }

    public int getStageWins(WrcDriver driver) {
        return (int) rallyList.stream().flatMap(r -> r.getMatches().stream()).filter(m -> m.getWinner() == driver).count();
    }

    @Override
    public String getPrimaryKey() {
        return String.valueOf(getEntityId());
    }

    public List<WrcRally> getRallyList() {
        return rallyList;
    }

    public void setRallyList(List<WrcRally> rallyList) {
        this.rallyList = rallyList;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
