package xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Entrant;

import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class YearEntrantsPane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(YearEntrantsPane.class);

    public YearEntrantsPane() {
        getStyleClass().add("entrantsPane");
        setLeft(createTableEntrants());

    }

    private TableBoxCaption<F1Entrant> createTableEntrants() {
        JfxTableCol<F1Entrant, String> colDriver = JfxTableCol.createCol("DRIVER", e -> e.getDriver().getFullName());
        JfxTableCol<F1Entrant, Integer> colCarNo = JfxTableCol.createCol("CAR", "carNo", "centered");
        JfxTableCol<F1Entrant, String> colTeam = JfxTableCol.createCol("TEAM", e -> e.getTeam().getTeamName());

        JfxTable<F1Entrant> tv = new JfxTable<>();
        tv.add(colDriver, colCarNo, colTeam);
        TableBoxCaption<F1Entrant> tbox = new TableBoxCaption<>(tv);

        guiModel.addChangeActionYear(year -> {
            List<F1Entrant> elist = model.getEntrants(year);
            tbox.updateBox(strf("Entrants {}   ({})", year, elist.size()), elist);
        });

        return tbox;
    }
}
