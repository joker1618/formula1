package xxx.joker.apps.formula1.fxgui.fxview.control;

import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import xxx.joker.apps.formula1.common.JfxCss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JfxTable<T> extends TableView<T> {

    private static final String CSS_FILEPATH = "/css/control/JfxTable.css";
    private static final double EXTRA_COL_WIDTH = 30d;

    private List<JfxTableCol<T, ?>> columns;
    private double fixedWidth = -1;

    @SafeVarargs
    public JfxTable(JfxTableCol<T, ?>... cols) {
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        this.columns = new ArrayList<>();
        add(cols);
        getStylesheets().add(getClass().getResource(CSS_FILEPATH).toExternalForm());
    }

    public void add(JfxTableCol... cols) {
        Arrays.stream(cols).forEach(c -> {
            columns.add(c);
            getColumns().add(c);
        });

        if(!columns.isEmpty()){
            for(int i = 0; i < columns.size(); i++) {
                JfxTableCol col = columns.get(i);
                col.getStyleClass().addAll("col", "col" + i);
                col.getStyleClass().add("col-" + (i % 2 == 0 ? "odd" : "even"));
            }
            columns.get(columns.size() - 1).getStyleClass().add("col-last");
        }
    }

    public void update(Collection<T> items) {
        getItems().setAll(items);
        resizeWidth(true);
        resizeHeight();
    }

    public void resizeWidth(boolean reserveScrollSpace) {
        double tablePrefWidth = 2d + (reserveScrollSpace ? 22d : 0d);

        //Set the right policy
        for (JfxTableCol<T, ?> col : columns) {
            if(col.isVisible()) {
                double max;
                if (col.getFixedWidth() != -1) {
                    max = col.getFixedWidth();
                    double wcol = max + EXTRA_COL_WIDTH;
                    col.setPrefWidth(wcol);
                } else {
                    //Minimal width = columnheader
                    Text t = new Text(col.getText());
                    max = t.getLayoutBounds().getWidth();
                    for (int i = 0; i < getItems().size(); i++) {
                        //cell must not be empty
                        if (col.getCellData(i) != null) {
                            t = new Text(col.formatCellData(i));
                            double calcwidth = t.getLayoutBounds().getWidth();
                            if (calcwidth > max) {
                                max = calcwidth;
                            }
                        }
                    }
                    double wcol = max + EXTRA_COL_WIDTH;
                    col.setPrefWidth(wcol);
                }
                // add extra space
                tablePrefWidth += max + EXTRA_COL_WIDTH;
            }
        }

//        if(fixedWidth != -1) {
        if(fixedWidth == -1) {
//            setPrefWidth(fixedWidth);
//        } else {
            setPrefWidth(tablePrefWidth);
        }
    }

    public void resizeHeight() {
        double h = 2d; // top and bottom border
        h += JfxCss.retrieveInt(this, "-headerHeight");
        h += JfxCss.retrieveInt(this, "-rowHeight") * getItems().size();
        setPrefHeight(h);
    }

    public void setFixedWidth(double fixedWidth) {
        this.fixedWidth = fixedWidth;
        setPrefWidth(fixedWidth);
    }
}