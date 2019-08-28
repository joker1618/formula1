package xxx.joker.apps.formula1.fxgui.fxview.panes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.JfxBorderPane;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Team;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TeamsPane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(TeamsPane.class);

    private JfxBorderPane<F1Team> leftBox;

    public TeamsPane() {
        getStyleClass().add("teamsPane");

        leftBox = createTableTeams();
        setLeft(leftBox);
    }

    private JfxBorderPane<F1Team> createTableTeams() {
        JfxTableCol<F1Team, String> colName = JfxTableCol.createCol("TEAM", "teamName");
        JfxTableCol<F1Team, String> colNation = JfxTableCol.createCol("NATION", "country");

        JfxTable<F1Team> tableTeams = new JfxTable<>();
        tableTeams.add(colName);
        tableTeams.add(colNation);
        tableTeams.update(model.getTeams());

        return new JfxBorderPane<>(strf("TEAMS   ({})", model.getTeams().size()), tableTeams);
    }
}
