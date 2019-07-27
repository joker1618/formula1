package xxx.joker.apps.formula1.fxgui.fxview.panes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.FxCountry;
import xxx.joker.apps.formula1.fxgui.fxview.SubPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Circuit;
import xxx.joker.libs.core.javafx.JfxUtil;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class CircuitsPane extends SubPane {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitsPane.class);

    private SimpleObjectProperty<F1Circuit> selectedCircuit = new SimpleObjectProperty<>();

    public CircuitsPane() {
        TableBoxCaption<F1Circuit> circuitsBox = createCircuitsPane();
        setLeft(circuitsBox);

        setCenter(createInfoPane());

        getStyleClass().add("circuitsPane");
        getStylesheets().add(getClass().getResource("/css/CircuitsPane.css").toExternalForm());

//        circuitsBox.selectFirstTableElem();
    }

    private TableBoxCaption<F1Circuit> createCircuitsPane() {
        JfxTableCol<F1Circuit, String> colNation = JfxTableCol.createCol("NATION", "country");
        JfxTableCol<F1Circuit, String> colCity = JfxTableCol.createCol("CITY", "city");

        JfxTable<F1Circuit> tableCircuits = new JfxTable();
        tableCircuits.add(colNation, colCity);
        tableCircuits.update(model.getCircuits());

        tableCircuits.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> selectedCircuit.set(n));
        return new TableBoxCaption<>(strf("CIRCUITS   ({})", model.getCircuits().size()), tableCircuits);
    }

    private Pane createInfoPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("infoPane");

        ImageView ivFlag = JfxUtil.createImageView(150, 100);
        Label lblTitle = new Label();
        HBox topBox = new HBox(ivFlag, lblTitle);
//        topBox.getStyleClass().addAll("pad10", "spacing10");
//        topBox.getStyleClass().addAll("bgGrey");
        bp.setTop(topBox);

        selectedCircuit.addListener((obs,o,n) -> {
            if(n != null) {
                FxCountry fxCountry = guiModel.getNation(n.getCountry());
                ivFlag.setImage(fxCountry.getFlagImage());
                lblTitle.setText(strf("{} - {}", fxCountry.getName(), n.getCity()));
            }
            bp.setVisible(n != null);
        });

        BorderPane.setMargin(bp, new Insets(0d, 0d, 0d, 40d));

        return bp;
    }
}
