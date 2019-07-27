package xxx.joker.apps.formula1.fxgui.fxview;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.F1GuiModel;
import xxx.joker.apps.formula1.fxgui.fxmodel.F1GuiModelImpl;
import xxx.joker.apps.formula1.fxgui.fxmodel.FxCountry;
import xxx.joker.apps.formula1.fxgui.fxview.panes.CircuitsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.DriversPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.HomePane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.TeamsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearEntrantsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearGpPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearResultsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearSummaryPane;
import xxx.joker.apps.formula1.model.F1ModelNew;
import xxx.joker.apps.formula1.model.F1ModelNewImpl;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.javafx.JfxUtil;

import java.util.List;

public class RootPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(RootPane.class);

    private F1ModelNew model = F1ModelNewImpl.getInstance();
    protected F1GuiModel guiModel = F1GuiModelImpl.getInstance();

    private JkCache<PaneType, SubPane> cachePanes = new JkCache<>();
    private SimpleObjectProperty<PaneType> showedPane = new SimpleObjectProperty<>();


    public RootPane() {
        createSubPanes();

        setLeft(createLeftMenu());

        getStyleClass().add("rootPane");
        
        showedPane.addListener((obs,o,n) -> setCenter(cachePanes.get(n)));

//        showedPane.set(PaneType.HOME);
//        showedPane.set(PaneType.CIRCUITS);
        showedPane.set(PaneType.YEAR_ENTRANTS);
//        showedPane.set(PaneType.YEAR_ENTRANTS);
//        showedPane.set(PaneType.YEAR_GRAN_PRIX);

        getStylesheets().add(getClass().getResource("/css/RootPane.css").toExternalForm());
    }

    private Pane createLeftMenu() {
        VBox menuBox = new VBox();
        menuBox.getStyleClass().add("rootMenu");
        menuBox.getStylesheets().add(getClass().getResource("/css/box/RootMenuBox.css").toExternalForm());

        VBox globalBox = new VBox();
        menuBox.getChildren().add(globalBox);
        globalBox.getStyleClass().add("globalBox");

        Button btn = new Button("HOME");
        btn.setOnAction(e -> showedPane.set(PaneType.HOME));
        globalBox.getChildren().add(btn);

        btn = new Button("DRIVERS");
        btn.setOnAction(e -> showedPane.set(PaneType.DRIVERS));
        globalBox.getChildren().add(btn);

        btn = new Button("TEAMS");
        btn.setOnAction(e -> showedPane.set(PaneType.TEAMS));
        globalBox.getChildren().add(btn);

        btn = new Button("CIRCUITS");
        btn.setOnAction(e -> showedPane.set(PaneType.CIRCUITS));
        globalBox.getChildren().add(btn);

        VBox yearBox = new VBox();
        yearBox.getStyleClass().addAll("yearBox");
        menuBox.getChildren().add(yearBox);

        ComboBox<Integer> comboSelYear = new ComboBox<>();
        comboSelYear.getItems().setAll(model.getAvailableYears());
        comboSelYear.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> guiModel.setSelectedYear(n));
        Button btnPrev = new Button("<");
        btnPrev.setOnAction(e -> comboSelYear.getSelectionModel().selectNext());
        btnPrev.getStyleClass().add("no-width");
        Button btnNext = new Button(">");
        btnNext.setOnAction(e -> comboSelYear.getSelectionModel().selectPrevious());
        btnNext.getStyleClass().add("no-width");
        HBox ybox = new HBox(btnPrev, comboSelYear, btnNext);
        ybox.getStyleClass().addAll("spacing10", "centered");
        yearBox.getChildren().add(ybox);

        btn = new Button("SUMMARY");
        btn.setOnAction(e -> showedPane.set(PaneType.YEAR_SUMMARY));
        yearBox.getChildren().add(btn);

        btn = new Button("ENTRANTS");
        btn.setOnAction(e -> showedPane.set(PaneType.YEAR_ENTRANTS));
        yearBox.getChildren().add(btn);

        btn = new Button("RESULTS");
        btn.setOnAction(e -> showedPane.set(PaneType.YEAR_RESULTS));
        yearBox.getChildren().add(btn);

        ListView<F1GranPrix> gpListView = new ListView<>();
        gpListView.getStyleClass().addAll("gpList");
        yearBox.getChildren().add(gpListView);
        gpListView.setCellFactory(param -> new ListCell<F1GranPrix>() {
            @Override
            protected void updateItem(F1GranPrix item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    FxCountry fnat = guiModel.getNation(item.getCircuit().getCountry());
                    Image flagIcon = fnat.getFlagIcon();
                    if(getGraphic() == null) {
                        ImageView ivIcon = JfxUtil.createImageView(flagIcon, 45, 28);
                        HBox iconBox = new HBox(ivIcon);
                        iconBox.getStyleClass().addAll("iconBox");
                        setGraphic(iconBox);
                    } else {
                        ImageView ivIcon = (ImageView) getGraphic().lookup(".iconBox .image-view");
                        ivIcon.setImage(flagIcon);
                    }
                    setText(fnat.getCode());
                }
            }
        });
        gpListView.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            if(n != null && n != o) {
                guiModel.setSelectedGranPrix(n);
                showedPane.set(PaneType.YEAR_GRAN_PRIX);
            } else if(n == null && o != null) {
                gpListView.getSelectionModel().select(o);
            }
        });
        guiModel.addChangeActionYear(n -> {
            List<F1GranPrix> gps = model.getGranPrixs(n);
            gpListView.getItems().setAll(gps);
            guiModel.setSelectedGranPrix(gps.get(0));
        });

        comboSelYear.getSelectionModel().selectFirst();

        return menuBox;
    }

    private void createSubPanes() {
        cachePanes.add(PaneType.HOME, new HomePane());
        cachePanes.add(PaneType.DRIVERS, new DriversPane());
        cachePanes.add(PaneType.TEAMS, new TeamsPane());
        cachePanes.add(PaneType.CIRCUITS, new CircuitsPane());
        cachePanes.add(PaneType.YEAR_SUMMARY, new YearSummaryPane());
        cachePanes.add(PaneType.YEAR_ENTRANTS, new YearEntrantsPane());
        cachePanes.add(PaneType.YEAR_RESULTS, new YearResultsPane());
        cachePanes.add(PaneType.YEAR_GRAN_PRIX, new YearGpPane());
    }

}
