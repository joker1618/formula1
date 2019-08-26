package xxx.joker.apps.formula1.fxgui.fxview.box;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;

import java.util.Collection;

public class TableBoxCaption<T> extends BorderPane {

    private static final String CSS_FILEPATH = "/css/box/TableBoxCaption.css";

    private HBox boxCaption;
    private Label lblCaption;
    private JfxTable<T> tableView;
    private HBox boxBottom;

    public TableBoxCaption(JfxTable<T> tableView) {
        this("", tableView);
    }
    public TableBoxCaption(String caption, JfxTable<T> tableView) {
        getStyleClass().add("rootBox");

        this.tableView = tableView;

        HBox container = new HBox(tableView);
        container.getStyleClass().addAll("borderBlack");
        HBox tbox = new HBox(container);
//        HBox tbox = new HBox(tableView);
        tbox.getStyleClass().addAll("boxCenter", "hbox");

        lblCaption = new Label(caption);
        boxCaption = new HBox(lblCaption);
        boxCaption.getStyleClass().addAll("boxTop", "hbox");

        boxBottom = new HBox();
        boxBottom.getStyleClass().addAll("boxBottom", "hbox");

        setTop(boxCaption);
        setCenter(tbox);

        getStylesheets().add(getClass().getResource(CSS_FILEPATH).toExternalForm());

        tableView.resizeWidth(true);
    }

    public void selectFirstTableElem() {
        tableView.getSelectionModel().selectFirst();
    }

    public void updateBox(String caption, Collection<T> items) {
        lblCaption.setText(caption);
        tableView.update(items);
    }

    public JfxTable<T> getTableView() {
        return tableView;
    }

    public Pane getBottomPane() {
        return getBottomPane();
    }

    public void setBottomPane(Pane bottomPane) {
        boxBottom.getChildren().setAll(bottomPane);
        if(getBottom() == null) {
            setBottom(boxBottom);
        }
    }

    public void showCaption(boolean show) {
        setTop(show ? boxCaption : null);
    }
}
