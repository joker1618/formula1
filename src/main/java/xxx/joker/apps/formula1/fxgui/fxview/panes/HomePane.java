package xxx.joker.apps.formula1.fxgui.fxview.panes;

import javafx.scene.image.ImageView;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine.StatKind;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine.StatsCell;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.JfxBorderPane2;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.utils.JkConvert;

import java.net.URL;
import java.util.List;

import static xxx.joker.apps.formula1.common.F1Util.safePrint0;
import static xxx.joker.libs.core.javafx.JfxUtil2.createHBox;
import static xxx.joker.libs.core.javafx.JfxUtil2.createImageView;

public class HomePane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(HomePane.class);

    public HomePane() {
        getStyleClass().addAll("homePane");
        setCenter(createTableStats());
//        URL url = getClass().getClassLoader().getResource("initImg.jpg");
//        ImageView imageView = createImageView(JkFiles.toPath(url), 1500, 1000);
//        setCenter(createHBox("borderBlack1 centered", imageView));

    }

    private JfxBorderPane2 createTableStats() {
        JfxTable<StatsLine> table = new JfxTable<>();

        JfxTableCol<StatsLine, String> colName = JfxTableCol.createCol("NAME", "statName");
        table.add(colName);

        Pair<List<StatKind>, List<StatsLine>> pairs = statsComputer.computeByDriver(JkConvert.toList(model.getGranPrixs()));
        for (StatKind skind : pairs.getKey()) {
            JfxTableCol<StatsLine, StatsCell> col = JfxTableCol.createCol(skind.getLabel(), sv -> sv.getStatsCell(skind), sc -> safePrint0(sc.getValue()));
            col.getStyleClass().addAll("centered");
            col.setFixedPrefWidth(90d);
            table.add(col);
        }

        JfxBorderPane2 toRet = new JfxBorderPane2(table);
        toRet.updateTable(pairs.getValue(), "STATS");

        return toRet;
    }

}

