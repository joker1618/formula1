package xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.ResultCellPoints;
import xxx.joker.apps.formula1.fxgui.fxmodel.ResultView;
import xxx.joker.apps.formula1.fxgui.fxmodel.SeasonView;
import xxx.joker.apps.formula1.fxgui.fxview.SubPane;
import xxx.joker.apps.formula1.fxgui.fxview.control.GridPaneBuilder;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static xxx.joker.apps.formula1.common.F1Util.safePrint;

public class YearResultsPane extends SubPane {

    private static final Logger LOG = LoggerFactory.getLogger(YearResultsPane.class);

    private AtomicBoolean showDrivers = new AtomicBoolean(true);
    private AtomicBoolean showPoints = new AtomicBoolean(true);

    public YearResultsPane() {
//        setTop(new Label("YEAR RESULTS"));

        GridPaneBuilder gpBuilder = new GridPaneBuilder();

        ToggleGroup tga = new ToggleGroup();
        RadioButton ra1 = new RadioButton("Drivers");
        ra1.setToggleGroup(tga);
        RadioButton ra2 = new RadioButton("Teams");
        ra2.setToggleGroup(tga);
        ra1.setSelected(true);
        gpBuilder.add(0, 0, ra1);
        gpBuilder.add(0, 1, ra2);
        tga.selectedToggleProperty().addListener((obs,o,n) -> {
            showDrivers.set(ra1.isSelected());
            SeasonView sv = guiModel.getSeasonView(guiModel.getSelectedYear());
            ((JfxTable)((HBox)getCenter()).getChildren().get(0)).update(showDrivers.get() ? sv.getDriverResults(): sv.getTeamResults());
        });

        ToggleGroup tgb = new ToggleGroup();
        RadioButton rb1 = new RadioButton("Points");
        rb1.setToggleGroup(tgb);
        RadioButton rb2 = new RadioButton("Pos");
        rb2.setToggleGroup(tgb);
        rb1.setSelected(true);
        gpBuilder.add(1, 0, rb1);
        gpBuilder.add(1, 1, rb2);
        tgb.selectedToggleProperty().addListener((obs,o,n) -> {
            showPoints.set(rb1.isSelected());
            ((JfxTable)((HBox)getCenter()).getChildren().get(0)).refresh();
        });

        setTop(gpBuilder.createGridPane());

        guiModel.addChangeActionYear(this::createTableResults);
    }

    private void createTableResults(int year) {
        JfxTable<ResultView> table = new JfxTable<>();
        table.setStyle("-headerHeight: 50; -rowHeight: 45");

        JfxTableCol<ResultView, Integer>  colPos = JfxTableCol.createCol("#", rv -> rv.getTotal().getPos(), "centered");
        table.add(colPos);
        colPos.setFixedWidth(30);

        JfxTableCol<ResultView, String> colDriver = JfxTableCol.createCol("DRIVER", "entryName");
        table.add(colDriver);
        List<F1GranPrix> gps = model.getGranPrixs(year);
        for (F1GranPrix gp : gps) {
            Image img = guiModel.getNation(gp.getCircuit().getCountry()).getFlagImage();
            JfxTableCol<ResultView, ResultCellPoints>  col = JfxTableCol.createCol("", rv -> rv.getCellMap().get(gp), race -> safePrint(showPoints.get() ? race.getExpectedPoints() : race.getPos()), "centered");
//            col.setComparator(Comparator.comparing(ResultCellPoints::getPos));
            col.setGraphic(JfxUtil.createImageView(img, 40, 40));
            col.setFixedWidth(50);
            table.add(col);
        }

        JfxTableCol<ResultView, ResultCellPoints>  colPts = JfxTableCol.createCol("TOT", "total", rcell -> showPoints.get() ? rcell.toStringTotPoints() : ""+rcell.getPos(), "centered");
//        colPts.setComparator(Comparator.comparing(ResultCellPoints::getPos));
        table.add(colPts);
        colPts.setFixedWidth(55);

        HBox tbox = new HBox(table);
//        tbox.getStyleClass().addAll("bgRed", "pad10");
        tbox.getStyleClass().addAll("bgRed");
        tbox.setStyle("-fx-padding: 20 0 0 0");
        tbox.widthProperty().addListener((obs,o,n) -> table.setFixedWidth(n.doubleValue()));
        setCenter(tbox);

        table.update(showDrivers.get() ? guiModel.getSeasonView(year).getDriverResults(): guiModel.getSeasonView(year).getTeamResults());
    }


}
