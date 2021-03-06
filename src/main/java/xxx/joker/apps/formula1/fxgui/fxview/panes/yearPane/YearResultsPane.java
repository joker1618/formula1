package xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane;

import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.ResultCellPoints;
import xxx.joker.apps.formula1.fxgui.fxmodel.ResultView;
import xxx.joker.apps.formula1.fxgui.fxmodel.SeasonView;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.JfxBorderPane;
import xxx.joker.apps.formula1.fxgui.fxview.control.GridPaneBuilder;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.javafx.JfxUtil2;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static xxx.joker.apps.formula1.common.F1Util.*;
import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class YearResultsPane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(YearResultsPane.class);

    private AtomicBoolean showDrivers = new AtomicBoolean(true);
    private AtomicBoolean showPoints = new AtomicBoolean(true);
    private JfxBorderPane<ResultView> tboxCaption;
    private GridPane choicePane;

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
            tboxCaption.getTableView().update(showDrivers.get() ? sv.getDriverResults(): sv.getTeamResults());
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
            tboxCaption.getTableView().refresh();
        });

        choicePane = gpBuilder.createGridPane();
//        setTop(gpBuilder.createGridPane());

        guiModel.addChangeActionYear(this::createTableResults);
    }

    private void createTableResults(int year) {
        JkDebug.startTimer("Year result");
        JfxTable<ResultView> table = new JfxTable<>();
        table.getStyleClass().add("bigRowHeight");

        JfxTableCol<ResultView, Integer>  colPos = JfxTableCol.createCol("#", rv -> rv.getTotal().getPos(), "centered");
        table.add(colPos);
        colPos.setFixedPrefWidth(30);

        JfxTableCol<ResultView, String> colDriver = JfxTableCol.createCol("DRIVER", "entryName");
        table.add(colDriver);
        List<F1GranPrix> gps = model.getGranPrixs(year);
        for (F1GranPrix gp : gps) {
            Image img = guiModel.getFlagIcon(gp.getCircuit().getCountry());
            JfxTableCol<ResultView, ResultCellPoints>  col = JfxTableCol.createCol("", rv -> rv.getCellMap().get(gp), race -> safePrint0(showPoints.get() ? race.getValue() : race.getPos()), "centered");
//            col.setComparator(Comparator.comparing(ResultCellPoints::getPos));
            ImageView iv = JfxUtil2.createImageView(img, 35, 24, false);
            col.setGraphic(iv);
            col.setFixedPrefWidth(55);
//            col.setMaxWidth(60);
            table.add(col);
        }

        JfxTableCol<ResultView, ResultCellPoints>  colPts = JfxTableCol.createCol("TOT", "total", rcell -> showPoints.get() ? rcell.toStringTotPoints() : safePrint0(rcell.getPos()), "centered");
        colPts.setFixedPrefWidth(60);
        table.add(colPts);

//        HBox tbox = new HBox(table);
//        tbox.setStyle("-fx-padding: 20 0 0 0");
//        tbox.widthProperty().addListener((obs,o,n) -> table.setFixedPrefWidth(n.doubleValue()));
//        setCenter(tbox);

        table.update(showDrivers.get() ? guiModel.getSeasonView(year).getDriverResults(): guiModel.getSeasonView(year).getTeamResults());
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.tboxCaption = new JfxBorderPane<>(strf("Results {}", year), table);
        tboxCaption.setBottomPane(choicePane);
        setCenter(tboxCaption);

        JkDebug.stopTimer("Year result");

    }


}
