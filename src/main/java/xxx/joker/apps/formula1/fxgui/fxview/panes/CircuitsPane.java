package xxx.joker.apps.formula1.fxgui.fxview.panes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.JfxBorderPane;
import xxx.joker.apps.formula1.fxgui.fxview.control.GridPaneBuilder;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Circuit;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.libs.core.javafx.JfxUtil2;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.datalayer.entities.RepoResource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class CircuitsPane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitsPane.class);

    private SimpleObjectProperty<F1Circuit> selectedCircuit = new SimpleObjectProperty<>();

    public CircuitsPane() {
        JfxBorderPane<F1Circuit> circuitsBox = createListPane();
        setLeft(circuitsBox);

        setCenter(createInfoPane());
        setRight(createTrackMapsPane());

        getStyleClass().add("circuitsPane");
        getStylesheets().add(getClass().getResource("/css/InfoPane.css").toExternalForm());

    }
    private JfxBorderPane<F1Circuit> createListPane() {
        JfxTableCol<F1Circuit, String> colNation = JfxTableCol.createCol("NATION", "country");
        JfxTableCol<F1Circuit, String> colCity = JfxTableCol.createCol("CITY", "city");

        JfxTable<F1Circuit> tableCircuits = new JfxTable();
        tableCircuits.add(colNation, colCity);
        tableCircuits.update(model.getCircuits());

        tableCircuits.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> selectedCircuit.set(n));
        return new JfxBorderPane<>(strf("CIRCUITS   ({})", model.getCircuits().size()), tableCircuits);
    }

    private Pane createInfoPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("infoPane");

        ImageView ivFlag = JfxUtil2.createImageView(400, 300);
        HBox containerBox = new HBox(ivFlag);
        containerBox.getStyleClass().add("borderBlack");
        HBox topBox = new HBox(containerBox);
        bp.setTop(topBox);

        topBox.getStyleClass().addAll("subBox");

        int row = 0;

        GridPaneBuilder gpBuilder = new GridPaneBuilder();
        gpBuilder.add(row, 0, "Country:");
        Label nat = new Label();
        gpBuilder.add(row, 1, nat);

        row++;
        gpBuilder.add(row, 0, "City:");
        Label city = new Label();
        gpBuilder.add(row, 1, city);

        GridPane gridPane = gpBuilder.createGridPane();

        HBox cbox = new HBox(gridPane);
        cbox.getStyleClass().addAll("subBox");
        bp.setCenter(cbox);

        selectedCircuit.addListener((obs,o,n) -> {
            if(n != null) {
                ivFlag.setImage(guiModel.getFlagImage(n.getCountry()));
                nat.setText(n.getCountry());
                city.setText(n.getCity());
            }
        });

        BorderPane.setMargin(bp, new Insets(0d, 0d, 0d, 20d));

        return bp;
    }

    private ScrollPane createTrackMapsPane() {
        GridPane gridPane = new GridPane();
        ScrollPane sp = new ScrollPane(gridPane);
        selectedCircuit.addListener((obs,o,n) -> {
            List<F1GranPrix> gpList = model.getList(F1GranPrix.class, gp -> gp.getCircuit().equals(n));
            Map<RepoResource, List<F1GranPrix>> resMap = JkStreams.toMap(gpList, gp -> model.getGpTrackMap(gp));
            GridPaneBuilder gpBuilder = new GridPaneBuilder();
            AtomicInteger row = new AtomicInteger(0);
            resMap.forEach((ruri,gplist) -> {
                gpBuilder.add(row.get(), 0, JfxUtil2.createImageView(ruri.getPath(), 400, 400));
                String join = JkStreams.join(gplist, "\n", gp -> ""+gp.getYear());
                gpBuilder.add(row.get(), 1, join);
                row.incrementAndGet();
            });

            AtomicInteger row2 = new AtomicInteger(0);
            Map<Double, List<F1GranPrix>> resMap2 = JkStreams.toMap(gpList, F1GranPrix::getLapLength);
            resMap2.forEach((ruri,gplist) -> {
                gpBuilder.add(row2.get(), 2, ruri+"");
                String join = JkStreams.join(gplist, "\n", gp -> ""+gp.getYear());
                gpBuilder.add(row2.get(), 3, join);
                row2.incrementAndGet();
            });
            gridPane.getChildren().forEach(ch -> ch.getStyleClass().addAll("borderBlack1"));

            gpBuilder.createGridPane(gridPane);
        });
        gridPane.getStyleClass().addAll("pad10", "vgap10");

        return sp;
    }
}
