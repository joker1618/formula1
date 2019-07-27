package xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.common.F1Util;
import xxx.joker.apps.formula1.fxgui.fxmodel.ResultView;
import xxx.joker.apps.formula1.fxgui.fxview.SubPane;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.util.List;

public class YearResultsPane extends SubPane {

    private static final Logger LOG = LoggerFactory.getLogger(YearResultsPane.class);

    public YearResultsPane() {
        setTop(new Label("YEAR RESULTS"));
        guiModel.addChangeActionYear(this::createTableResults);
    }

    private void createTableResults(int year) {
        JfxTable<ResultView> table = new JfxTable<>();
        table.setStyle("-headerHeight: 50; -rowHeight: 40");

        JfxTableCol<ResultView, String> colDriver = JfxTableCol.createCol("DRIVER", rv -> rv.getDriver().getFullName());
        table.add(colDriver);
        List<F1GranPrix> gps = model.getGranPrixs(year);
        for (F1GranPrix gp : gps) {
            Image img = guiModel.getNation(gp.getCircuit().getCountry()).getFlagImage();
            JfxTableCol<ResultView, Double>  col = JfxTableCol.createCol("", rv -> rv.getRacePoints(gp), F1Util::safePrint, "centered");
            col.setGraphic(JfxUtil.createImageView(img, 40, 40));
            table.add(col);
            col.setFixedWidth(50);
        }

        JfxTableCol<ResultView, Double>  col = JfxTableCol.createCol("", ResultView::getTotalPoints, F1Util::safePrint, "centered");
        table.add(col);
        col.setFixedWidth(50);

        setCenter(table);

        table.update(guiModel.getSeasonView(year).getDriverResViews());
    }
}
