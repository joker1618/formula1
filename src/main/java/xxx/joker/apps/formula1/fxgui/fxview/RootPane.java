package xxx.joker.apps.formula1.fxgui.fxview;

import com.madhukaraphatak.sizeof.SizeEstimator;
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
import xxx.joker.apps.formula1.fxgui.fxview.panes.CircuitsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.DriversPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.HomePane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.TeamsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearEntrantsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearGpPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearResultsPane;
import xxx.joker.apps.formula1.fxgui.fxview.panes.yearPane.YearSummaryPane;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.apps.formula1.model.entities.Country;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.format.JkOutput;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static xxx.joker.libs.core.javafx.JfxUtil2.createHBox;
import static xxx.joker.libs.core.javafx.JfxUtil2.createImageView;
import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class RootPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(RootPane.class);

    private F1Model model = F1ModelImpl.getInstance();
    protected F1GuiModel guiModel = F1GuiModelImpl.getInstance();
    private ComboBox<Integer> comboSelYear;

    private JkCache<PaneType, CentralPane> cachePanes = new JkCache<>();
    private SimpleObjectProperty<PaneType> showedPane = new SimpleObjectProperty<>();


    public RootPane() {
        createSubPanes();

        setLeft(createLeftMenu());

        getStyleClass().add("rootPane");
        getStylesheets().add(getClass().getResource("/css/RootPane.css").toExternalForm());

        showedPane.addListener((obs,o,n) -> setCenter(cachePanes.get(n)));

//        showedPane.set(PaneType.HOME);
        showedPane.set(PaneType.CIRCUITS);
//        showedPane.set(PaneType.YEAR_RESULTS);
//        showedPane.set(PaneType.YEAR_ENTRANTS);
//        showedPane.set(PaneType.TEAMS);
//        showedPane.set(PaneType.YEAR_GRAN_PRIX);

        comboSelYear.getSelectionModel().selectFirst();
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

        this.comboSelYear = new ComboBox<>();
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
                JkDebug.startTimer("List cell factory");
                if(item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Country country = model.getCountry(item.getCircuit().getCountry());
                    Image flagIcon = guiModel.getFlagIcon(country);
                    if(getGraphic() == null) {
                        ImageView ivIcon = createImageView(flagIcon, 45, 28, false);
                        HBox hbox = createHBox("borderBlack1 centered", ivIcon);
                        setGraphic(createHBox("iconBox", hbox));
                    } else {
                        ImageView ivIcon = (ImageView) getGraphic().lookup(".iconBox .image-view");
                        ivIcon.setImage(flagIcon);
                    }
                    setText(strf("%2d  -  %s", item.getNum(), country.getCode()));
                }
                JkDebug.stopTimer("List cell factory");
            }
        });
        gpListView.setOnMouseClicked(e -> showedPane.set(PaneType.YEAR_GRAN_PRIX));
        gpListView.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> guiModel.setSelectedGranPrix(n));

        guiModel.addChangeActionYear(n -> {
            List<F1GranPrix> gps = model.getGranPrixs(n);
            gpListView.getItems().setAll(gps);
            gpListView.getSelectionModel().selectFirst();
        });

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

//    private CentralPane createSubPane(PaneType paneType) {
//        switch (paneType) {
//            case HOME: return new  HomePane();
//            case DRIVERS: return new  DriversPane();
//            case TEAMS: return new  TeamsPane();
//            case CIRCUITS: return new  CircuitsPane();
//            case YEAR_SUMMARY: return new  YearSummaryPane();
//            case YEAR_ENTRANTS: return new  YearEntrantsPane();
//            case YEAR_RESULTS: return new  YearResultsPane();
//            case YEAR_GRAN_PRIX: return new  YearGpPane();
//            default: return null;
//        }
//    }

}
