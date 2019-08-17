package xxx.joker.apps.formula1.fxgui.fxview.control;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.core.utils.JkStrings;

import java.util.function.Function;

public class JfxTableCol<T, V> extends TableColumn<T, V> {

    private String varName;
    private Function<T, V> extractor;
    private Function<V, String> strFunc;
    private boolean autoSize;
    private double fixedPrefWidth = -1d;

    public JfxTableCol() {

    }

    public static <T,V> JfxTableCol<T,V> createCol(String header, String varName, String... styleClasses) {
        return createCol(header, varName, null, null, true, -1d, styleClasses);
    }
    public static <T,V> JfxTableCol<T,V> createCol(String header, Function<T, V> extractor, String... styleClasses) {
        return createCol(header, null, extractor, null, true, -1d, styleClasses);
    }
    public static <T,V> JfxTableCol<T,V> createCol(String header, String varName, Function<V, String> strFunc, String... styleClasses) {
        return createCol(header, varName, null, strFunc, true, -1, styleClasses);
    }
    public static <T,V> JfxTableCol<T,V> createCol(String header, Function<T, V> extractor, Function<V, String> strFunc, String... styleClasses) {
        return createCol(header, null, extractor, strFunc, true, -1, styleClasses);
    }
    public static <T,V> JfxTableCol<T,V> createCol(String header, String varName, Function<T, V> extractor, Function<V, String> strFunc, boolean autoSize, double prefWidth, String... styleClasses) {
        JfxTableCol<T,V> col = new JfxTableCol();

        for (String str : styleClasses) {
            col.getStyleClass().addAll(JkStrings.splitList(str, " ", true));
        }

        if(StringUtils.isNotBlank(header)) {
            col.setText(header);
        }

        if(StringUtils.isNotBlank(varName)) {
            col.setCellValueFactory(new PropertyValueFactory<>(varName));
        } else {
            col.setCellValueFactory(param -> new SimpleObjectProperty<>(extractor.apply(param.getValue())));
        }

        if(strFunc != null) {
            col.setCellFactory(param -> new TableCell<T, V>() {
                @Override
                protected void updateItem (V item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(strFunc.apply(item));
                    }
                }
            });
        }

        col.strFunc = strFunc;
        col.autoSize = autoSize;
        col.fixedPrefWidth = prefWidth;

        return col;
    }

    public String formatCellData(int i) {
        V cellData = super.getCellData(i);
        return strFunc == null ? cellData.toString() : strFunc.apply(cellData);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Function<T, V> getExtractor() {
        return extractor;
    }

    public void setExtractor(Function<T, V> extractor) {
        this.extractor = extractor;
    }

    public Function<V, String> getStrFunc() {
        return strFunc;
    }

    public void setStrFunc(Function<V, String> strFunc) {
        this.strFunc = strFunc;
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
    }

    public double getFixedPrefWidth() {
        return fixedPrefWidth;
    }

    public void setFixedPrefWidth(double fixedPrefWidth) {
        this.fixedPrefWidth = fixedPrefWidth;
    }


}
