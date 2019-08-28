package xxx.joker.apps.formula1.fxgui.fxview.box;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;

import java.util.Collection;

import static xxx.joker.libs.core.javafx.JfxUtil2.createHBox;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class InfoPane<T> extends BorderPane {

    private static final String CSS_FILEPATH = "/css/box/InfoPane.css";

    private HBox boxTop;
    private Label topCaption;
    private JfxTable<T> tableView;
    private HBox boxBottom;

    public InfoPane(JfxTable<T> tableView) {
        this(tableView, null);
    }
    public InfoPane(JfxTable<T> tableView, String caption) {
        getStyleClass().add("infoPane");

        this.tableView = tableView;

        topCaption = new Label(caption);
        boxTop = createHBox("boxTop hbox", topCaption);
        setTop(boxTop);

        HBox container = createHBox("borderBlack1", tableView);
        HBox boxCenter = createHBox("boxCenter hbox", container);
        setCenter(boxCenter);

        boxBottom = createHBox("boxBottom hbox");

        getStylesheets().add(getClass().getResource(CSS_FILEPATH).toExternalForm());
    }

    public void updatePane(Collection<T> items, String captionFormat, Object... params) {
        topCaption.setText(strf(captionFormat, params));
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
        setTop(show ? boxTop : null);
    }
}
