package xxx.joker.apps.formula1.fxgui.fxview.box;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;

import java.util.Collection;

import static xxx.joker.libs.core.javafx.JfxUtil2.createHBox;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class JfxBorderPane2 extends BorderPane {

    private static final String CSS_FILEPATH = "/css/box/JfxBorderPane2.css";

    private Label topCaption;
    private JfxTable<?> tableView;

    public JfxBorderPane2(JfxTable<?> tableView) {
        this(tableView, null);
    }
    public JfxBorderPane2(JfxTable<?> tableView, String caption) {
        getStyleClass().add("jfxBorderPane");

        this.tableView = tableView;

        topCaption = new Label(caption);
        HBox boxTop = createHBox("boxTop hbox", topCaption);
        setTop(boxTop);

        HBox container = createHBox("simpleBorder", tableView);
        HBox boxCenter = createHBox("boxCenter hbox", container);
        setCenter(boxCenter);

        getStylesheets().add(getClass().getResource(CSS_FILEPATH).toExternalForm());
    }

    public void updateTable(Collection items, String captionFormat, Object... params) {
        topCaption.setText(strf(captionFormat, params));
        tableView.update(items);
    }

    public <T> JfxTable<T> getJfxTable() {
        return (JfxTable<T>) tableView;
    }

}
