package xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane;

import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.FxCountry;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.GridPaneBuilder;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Qualify;
import xxx.joker.apps.formula1.model.entities.F1Race;
import xxx.joker.libs.core.datetime.JkDates;
import xxx.joker.libs.core.datetime.JkDuration;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Arrays;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class YearGpPane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(YearGpPane.class);


    public YearGpPane() {
        getStylesheets().add(getClass().getResource("/css/YearGpPane.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/css/InfoPane.css").toExternalForm());

        BorderPane detailsPane = createDetailsPane();

        TableBoxCaption<F1Qualify> qtable = createTableQualify();
        TableBoxCaption<F1Race> rtable = createTableRace();
        HBox boxResults = new HBox(qtable, rtable);
        boxResults.getStyleClass().addAll("boxResults");

        Tab tab1 = new Tab("DETAILS");
        tab1.setContent(detailsPane);
        Tab tab2 = new Tab("RESULTS");
        tab2.setContent(boxResults);

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("tabPane");
        tabPane.getTabs().addAll(tab1, tab2);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.setSide(Side.LEFT);

        setCenter(tabPane);
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

    private BorderPane createDetailsPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("detailsPane");

        VBox vboxLeft = new VBox();
        bp.setLeft(vboxLeft);
        vboxLeft.getStyleClass().addAll("boxLeft", "subBox");
        vboxLeft.getStyleClass().addAll("bgGrey", "borderBlue");

        ImageView ivFlag = JfxUtil.createImageView(400, 300);
        HBox flagBox = new HBox(ivFlag);
        flagBox.setPrefWidth(400);
        flagBox.setPrefHeight(300);
        flagBox.getStyleClass().addAll("borderRed", "bgYellow", "centered");
        vboxLeft.getChildren().add(flagBox);

        int rowNum = 0;
        GridPaneBuilder gpBuilder = new GridPaneBuilder();
        gpBuilder.add(rowNum, 0, "GP num:");
        Label lblGpNum = new Label();
        gpBuilder.add(rowNum, 1, lblGpNum);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Date:");
        Label lblDate = new Label();
        gpBuilder.add(rowNum, 1, lblDate);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Location:");
        Label lblLocation = new Label();
        gpBuilder.add(rowNum, 1, lblLocation);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Lap length:");
        Label lblLapLen = new Label();
        gpBuilder.add(rowNum, 1, lblLapLen);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Laps:");
        Label lblLaps = new Label();
        gpBuilder.add(rowNum, 1, lblLaps);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Race length:");
        Label lblRaceLen = new Label();
        gpBuilder.add(rowNum, 1, lblRaceLen);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Pole:");
        Label lblPoleDriver = new Label();
        gpBuilder.add(rowNum, 1, lblPoleDriver);
        Label lblPoleTime = new Label();
        gpBuilder.add(rowNum, 2, lblPoleTime);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Winner:");
        Label lblWinner = new Label();
        gpBuilder.add(rowNum, 1, lblWinner);

        rowNum++;
        gpBuilder.add(rowNum, 0, "Fast lap:");
        Label lblFastDriver = new Label();
        gpBuilder.add(rowNum, 1, lblFastDriver);
        Label lblFastTime = new Label();
        gpBuilder.add(rowNum, 2, lblFastTime);

        vboxLeft.getChildren().add(gpBuilder.createGridPane());

        HBox hboxTrackGp = new HBox();
        bp.setRight(hboxTrackGp);
        hboxTrackGp.getStyleClass().addAll("boxTrackGp", "subBox");
        hboxTrackGp.getStyleClass().addAll("bgYellow", "borderRed");

        ImageView iv = JfxUtil.createImageView(800, 800);
        HBox hboxTmp = new HBox(iv);
//        hboxTmp.getStyleClass().addAll("bgRed", "borderWhite", "centered");
        hboxTmp.getStyleClass().addAll("centered");
        hboxTmp.setStyle("-fx-background-color: #ffe6ff");
        hboxTrackGp.getChildren().add(hboxTmp);

        guiModel.addChangeActionGranPrix(gp -> {
            if(gp != null) {
                FxCountry fxCountry = guiModel.getNation(gp.getCircuit().getCountry());
                ivFlag.setImage(fxCountry.getFlagImage());
                lblGpNum.setText(strf("{}/{}", gp.getNum(), model.getGranPrixs(gp.getYear()).size()));
                lblDate.setText(strf("{}", JkDates.format(gp.getDate(), "dd/MM/yyyy")));
                lblLocation.setText(strf("{}, {}", gp.getCircuit().getCountry(), gp.getCircuit().getCity()));
                lblLapLen.setText(strf("{} m", gp.getLapLength()));
                lblLaps.setText(strf("{}", gp.getNumLapsRace()));
                lblRaceLen.setText(strf("{}", gp.getNumLapsRace() * gp.getLapLength()));
                F1Qualify qual = gp.getQualifies().get(0);
                lblPoleDriver.setText(qual.getEntrant().getDriver().getFullName());
                int idx = -1;
                for(int i = qual.getTimes().size()-1; idx == -1 && i >= 0; i--) {
                    JkDuration t = qual.getTimes().get(i);
                    if(t != null && t.toMillis() > 1000) {
                        idx = i;
                    }
                }
                lblPoleTime.setText(idx == -1 ? "" : qual.getTimes().get(idx).toStringElapsed());
                lblWinner.setText(gp.getRaces().get(0).getEntrant().getDriver().getFullName());
                lblFastDriver.setText(gp.getFastLap().getDriverPK());
                lblFastTime.setText(gp.getFastLap().getLapTime().toStringElapsed());
                iv.setImage(guiModel.getGpTrackMap(gp));
            }
        });

        return bp;
    }
}
