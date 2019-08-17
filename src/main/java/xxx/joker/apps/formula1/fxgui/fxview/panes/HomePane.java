package xxx.joker.apps.formula1.fxgui.fxview.panes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.common.F1Util;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsView;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsView.*;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.libs.core.utils.JkConvert;

import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class HomePane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(HomePane.class);

    public HomePane() {
        getStyleClass().add("bgYellow");
        setCenter(createTableStats());
    }

    private TableBoxCaption<StatsView> createTableStats() {
        JfxTable<StatsView> table = new JfxTable<>();

        JfxTableCol<StatsView, String> colName = JfxTableCol.createCol("NAME", "statName");
        table.add(colName);

        for (StatKind skind : StatKind.values()) {
            JfxTableCol<StatsView, StatsCell> col = JfxTableCol.createCol(skind.getLabel(), sv -> sv.getStatsCell(skind), sc -> F1Util.safePrint0(sc.getValue()));
            col.getStyleClass().add("centered");
            table.add(col);
        }

        List<StatsView> statsViews = statsComputer.computeByDriver(JkConvert.toList(model.getGranPrixs()));
        table.update(statsViews);

        return new TableBoxCaption<>(strf("STATS"), table);
    }
}

