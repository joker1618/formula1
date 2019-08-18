package xxx.joker.apps.formula1.fxgui.fxview.panes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.FxCountry;
import xxx.joker.apps.formula1.fxgui.fxview.CentralPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.GridPaneBuilder;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Driver;
import xxx.joker.libs.core.datetime.JkDates;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class DriversPane extends CentralPane {

    private static final Logger LOG = LoggerFactory.getLogger(DriversPane.class);

    private SimpleObjectProperty<F1Driver> selectedDriver = new SimpleObjectProperty<>();


    public DriversPane() {
        getStyleClass().add("driversPane");

        setLeft(createListPane());
        setCenter(createInfoPane());

        getStylesheets().add(getClass().getResource("/css/InfoPane.css").toExternalForm());

    }

    private TableBoxCaption<F1Driver> createListPane() {
        JfxTableCol<F1Driver, String> colName = JfxTableCol.createCol("NAME", "fullName");
        JfxTableCol<F1Driver, String> colNation = JfxTableCol.createCol("NATION", "country");
        JfxTableCol<F1Driver, LocalDate> colBirth = JfxTableCol.createCol("BIRTH", "birthDay", bd -> JkDates.format(bd, "dd/MM/yyyy"));
        JfxTableCol<F1Driver, LocalDate> colAge = JfxTableCol.createCol("AGE", "birthDay", ld -> ""+ChronoUnit.YEARS.between(ld, LocalDate.now()));

        JfxTable<F1Driver> tableDrivers = new JfxTable<>();
        tableDrivers.add(colName, colNation, colBirth, colAge);
        Set<F1Driver> drivers = model.getDrivers();

        TableBoxCaption<F1Driver> toRet = new TableBoxCaption<>(strf("DRIVERS   ({})", drivers.size()), tableDrivers);

        tableDrivers.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> selectedDriver.set(n));
        tableDrivers.update(drivers);

        return toRet;
    }

    private Pane createInfoPane() {
        BorderPane bp = new BorderPane();
        bp.getStyleClass().add("infoPane");

        ImageView ivCover = JfxUtil.createImageView(500, 500);
        HBox topBox = new HBox(ivCover);
        bp.setTop(topBox);
        topBox.getStyleClass().addAll("centered", "bgBlack");

        topBox.getStyleClass().addAll("subBox");

        int row = 0;

        GridPaneBuilder gpBuilder = new GridPaneBuilder();
        gpBuilder.add(row, 0, "Name:");
        Label lname = new Label();
        gpBuilder.add(row, 1, lname);

        row++;
        gpBuilder.add(row, 0, "Birthday:");
        Label lbday = new Label();
        gpBuilder.add(row, 1, lbday);

        row++;
        gpBuilder.add(row, 0, "Birthplace:");
        Label lbplace = new Label();
        gpBuilder.add(row, 1, lbplace);

        GridPane gridPane = gpBuilder.createGridPane();

        HBox cbox = new HBox(gridPane);
        cbox.getStyleClass().addAll("subBox");
        bp.setCenter(cbox);

        selectedDriver.addListener((obs,o,n) -> {
            if(n != null) {
                ivCover.setImage(guiModel.getDriverCover(n));
                lname.setText(n.getFullName());
                lbday.setText(JkDates.format(n.getBirthDay(), "dd/MM/yyyy"));
                lbplace.setText(strf("{}, {}", n.getCountry(), n.getCity()));
            }
        });

        BorderPane.setMargin(bp, new Insets(0d, 0d, 0d, 20d));

        return bp;
    }

}
