package xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane;

import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxview.SubPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Qualify;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.libs.core.format.JkOutput;

import java.text.NumberFormat;
import java.util.Arrays;

public class YearGpPane extends SubPane {

    private static final Logger LOG = LoggerFactory.getLogger(YearGpPane.class);


    public YearGpPane() {
        TableBoxCaption<F1Qualify> qtable = createTableQualify();
        TableBoxCaption<F1Race> rtable = createTableRace();
        HBox hbox = new HBox(qtable, rtable);
        hbox.getStyleClass().addAll("centered");
        hbox.setStyle("-fx-spacing: 150");
        setCenter(hbox);
    }

    private TableBoxCaption<F1Qualify> createTableQualify() {
        JfxTableCol<F1Qualify, Integer> colIdx = JfxTableCol.createCol("#", "pos");

        JfxTableCol<F1Qualify, String> colDriver = JfxTableCol.createCol("DRIVER", q -> q.getEntrant().getDriver().getFullName());

        JfxTableCol<F1Qualify, String> colQ1 = createQualTimeCol(1);
        JfxTableCol<F1Qualify, String> colQ2 = createQualTimeCol(2);
        JfxTableCol<F1Qualify, String> colQ3 = createQualTimeCol(3);
        Arrays.asList(colQ1, colQ2, colQ3).forEach(col -> col.setComparator((o1, o2) -> o2.isEmpty() ? -1 : o1.isEmpty() ? 1 : o1.compareTo(o2)));

        JfxTableCol<F1Qualify, Integer> colGrid = JfxTableCol.createCol("GRID", "finalGrid");

        Arrays.asList(colIdx, colQ1, colQ2, colQ3, colGrid).forEach(col -> col.getStyleClass().add("centered"));

        JfxTable<F1Qualify> tv = new JfxTable<>();
        tv.add(colIdx, colDriver, colQ1, colQ2, colQ3, colGrid);
        TableBoxCaption<F1Qualify> tbox = new TableBoxCaption<>(tv);
        guiModel.addChangeActionGranPrix(gp -> tbox.updateBox("Qualify", gp.getQualifies()));

        return tbox;
    }

    private JfxTableCol<F1Qualify, String> createQualTimeCol(int num) {
        int pos = num - 1;
        JfxTableCol<F1Qualify, String> col = JfxTableCol.createCol("Q"+num, q -> q.getTimes().size() > pos ? q.getTimes().get(pos) == null ? "" : q.getTimes().get(pos).toStringElapsed() : "");
        col.setComparator((o1, o2) -> o2.isEmpty() ? -1 : o1.isEmpty() ? 1 : o1.compareTo(o2));
        return col;
    }

    private TableBoxCaption<F1Race> createTableRace() {
        JfxTableCol<F1Race, Integer> colPos = JfxTableCol.createCol("#", "pos");
        JfxTableCol<F1Race, String> colOutcome = JfxTableCol.createCol("OUTCOME", r -> r.getOutcome().getLabel());
        JfxTableCol<F1Race, String> colDriver = JfxTableCol.createCol("DRIVER", r -> r.getEntrant().getDriver().getFullName());
        JfxTableCol<F1Race, Integer> colLaps = JfxTableCol.createCol("LAPS", "laps");
        JfxTableCol<F1Race, String> colTime = JfxTableCol.createCol("TIME", r -> r.getTime() == null ? "" : r.getTime().toStringElapsed());
        colTime.setComparator((o1, o2) -> o2.isEmpty() ? -1 : o1.isEmpty() ? 1 : o1.compareTo(o2));
        JfxTableCol<F1Race, Integer> colGrid = JfxTableCol.createCol("GRID", "startGrid");
        JfxTableCol<F1Race, Double> colPoints = JfxTableCol.createCol("POINTS", "points", p -> {
            NumberFormat nf = JkOutput.getNumberFmtEN(0, 2);
            return p == 0d ? "" : nf.format(p.doubleValue());
        });

        Arrays.asList(colPos, colOutcome, colLaps, colTime, colGrid, colPoints).forEach(col -> col.getStyleClass().add("centered"));

        JfxTable<F1Race> tv = new JfxTable<>();
        tv.add(colPos, colOutcome, colDriver, colLaps, colTime, colGrid, colPoints);
        TableBoxCaption<F1Race> tbox = new TableBoxCaption<>(tv);
        guiModel.addChangeActionGranPrix(gp -> tbox.updateBox("Race", gp.getRaces()));

        return tbox;
    }
}
