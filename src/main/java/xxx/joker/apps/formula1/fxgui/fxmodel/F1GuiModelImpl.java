package xxx.joker.apps.formula1.fxgui.fxmodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import xxx.joker.apps.formula1.model.F1ModelNew;
import xxx.joker.apps.formula1.model.F1ModelNewImpl;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class F1GuiModelImpl implements F1GuiModel {

    private static final F1GuiModel instance = new F1GuiModelImpl();

    private F1ModelNew model = F1ModelNewImpl.getInstance();

    private JkCache<String, FxCountry> cacheNation = new JkCache<>();
    private JkCache<Integer, SeasonView> cacheYears = new JkCache<>();

    private SimpleIntegerProperty selectedYear = new SimpleIntegerProperty();
    private SimpleObjectProperty<F1GranPrix> selectedGranPrix = new SimpleObjectProperty<>();


    private F1GuiModelImpl() {

    }

    public static F1GuiModel getInstance() {
        return instance;
    }

    @Override
    public FxCountry getNation(String nationName) {
        return cacheNation.get(nationName, () -> {
            F1Country country = model.getCountry(nationName);
            Path iconPath = model.getRepoCtx().getResourcesFolder().resolve(country.getFlagIcon().getUri().getPath());
            Image icon = new Image(JkFiles.toURL(iconPath));
            Path imagePath = model.getRepoCtx().getResourcesFolder().resolve(country.getFlagImage().getUri().getPath());
            Image image = new Image(JkFiles.toURL(imagePath));
            return new FxCountry(country, icon, image);
        });
    }

    @Override
    public SeasonView getSeasonView(int year) {
        return cacheYears.get(year, () -> {
            SeasonView sv = new SeasonView(year);
            List<F1Driver> drivers = JkStreams.mapUniq(model.getEntrants(year), F1Entrant::getDriver);
            for (F1Driver d : drivers) {
                ResultView rv = new ResultView();
                rv.setDriver(d);
                for (F1GranPrix gp : model.getGranPrixs(year)) {
                    F1Race unique = JkStreams.findUnique(gp.getRaces(), r -> r.getEntrant().getDriver().equals(d));
                    if(unique != null) {
                        rv.getRaceMap().put(gp, unique);
                    }
                }
                sv.getDriverResViews().add(rv);
            }
            return sv;
        });
    }

    @Override
    public void setSelectedYear(int year) {
        selectedYear.set(year);
    }

    @Override
    public void addChangeActionYear(Consumer<Integer> action) {
        selectedYear.addListener((obs,o,n) -> { if(n != null && n != o) action.accept(n.intValue()); });
    }

    @Override
    public void setSelectedGranPrix(F1GranPrix gp) {
        selectedGranPrix.set(gp);
    }

    @Override
    public void addChangeActionGranPrix(Consumer<F1GranPrix> action) {
        selectedGranPrix.addListener((obs,o,n) -> {
            if(n != null && n != o) {
                action.accept(n);
            }
        });
    }
}
