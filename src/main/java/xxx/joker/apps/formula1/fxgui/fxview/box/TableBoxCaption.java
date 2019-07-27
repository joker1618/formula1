package xxx.joker.apps.formula1.fxgui.fxview.box;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;

import java.util.Collection;

public class TableBoxCaption<T> extends BorderPane {

    private static final String CSS_FILEPATH = "/css/box/TableBoxCaption.css";

    private Label lblCaption;
    private JfxTable<T> tableView;

    public TableBoxCaption(JfxTable<T> tableView) {
        this("", tableView);
    }
    public TableBoxCaption(String caption, JfxTable<T> tableView) {
        getStyleClass().add("rootBox");

        this.tableView = tableView;

        HBox tbox = new HBox(tableView);
        tbox.getStyleClass().addAll("boxTable", "hbox");

        lblCaption = new Label(caption);
        HBox boxCaption = new HBox(lblCaption);
        boxCaption.getStyleClass().addAll("boxCaption", "hbox");

        setTop(boxCaption);
        setCenter(tbox);

        getStylesheets().add(getClass().getResource(CSS_FILEPATH).toExternalForm());
    }

    public void selectFirstTableElem() {
        tableView.getSelectionModel().selectFirst();
    }

    public void updateBox(String caption, Collection<T> items) {
        lblCaption.setText(caption);
        tableView.update(items);
    }
}
