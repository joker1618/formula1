package xxx.joker.apps.formula1.fxgui.fxmodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.datalayer.entities.RepoResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class F1GuiModelImpl implements F1GuiModel {

    private static final Logger LOG = LoggerFactory.getLogger(F1GuiModelImpl.class);

    private static final F1GuiModel instance = new F1GuiModelImpl();

    private F1Model model = F1ModelImpl.getInstance();

    private JkCache<Country, Image> cacheFlagIcon = new JkCache<>();
    private final Image imageUnavailable;
//    private JkCache<F1GranPrix, Image> cacheGpTrackMap = new JkCache<>();
//    private JkCache<F1Driver, Image> cacheDriverCoverMap = new JkCache<>();
    private JkCache<Integer, SeasonView> cacheYears = new JkCache<>();

    private SimpleIntegerProperty selectedYear = new SimpleIntegerProperty();
    private SimpleObjectProperty<F1GranPrix> selectedGranPrix = new SimpleObjectProperty<>();


    private F1GuiModelImpl() {
        RepoResource res = model.getImageUnavailable();
        Path uriPath = res.getPath();
        this.imageUnavailable = new Image(JkFiles.toURL(uriPath));
        // preload and cache all flag icons
        model.getCircuits().forEach(c -> getFlagIcon(c.getCountry()));
    }

    public static F1GuiModel getInstance() {
        return instance;
    }

    @Override
    public Image getFlagIcon(String countryName) {
        return getFlagIcon(model.getCountry(countryName));
    }

    @Override
    public Image getFlagIcon(Country country) {
        return cacheFlagIcon.get(country, () -> {
            RepoResource flagIcon = model.getFlagIcon(country);
            String url = JkFiles.toURL(flagIcon.getPath());
            return new Image(url);
        });
    }

    @Override
    public Image getFlagImage(String countryName) {
        return getFlagImage(model.getCountry(countryName));
    }

    @Override
    public Image getFlagImage(Country country) {
        RepoResource flagLarge = model.getFlagImage(country);
        String url = JkFiles.toURL(flagLarge.getPath());
        return new Image(url);
    }

    @Override
    public Image getGpTrackMap(F1GranPrix gp) {
//        return cacheGpTrackMap.get(gp, () -> {
            Path uriPath = model.getGpTrackMap(gp).getPath();
            if(Files.exists(uriPath)) {
                return new Image(JkFiles.toURL(uriPath));
            } else {
                return imageUnavailable;
            }
//        });
    }

    @Override
    public Image getDriverCover(F1Driver driver) {
//        return cacheDriverCoverMap.get(driver, () -> {
            RepoResource res = model.getDriverCover(driver);
            if (res != null) {
                Path uriPath = res.getPath();
                if (Files.exists(uriPath)) {
                    return new Image(JkFiles.toURL(uriPath));
                }
            }
            return imageUnavailable;
//        });
    }

    @Override
    public SeasonView getSeasonView(int year) {
        return cacheYears.get(year, () -> {
            SeasonView sv = new SeasonView(year);
            Map<String, F1SeasonPoints> seasonPoints = model.getSeasonPointsMap(year);

            List<F1Driver> drivers = JkStreams.mapUniq(model.getEntrants(year), F1Entrant::getDriver);
            for (F1Driver d : drivers) {
                ResultView rv = new ResultView();
                rv.setEntryName(d.getFullName());
                for (F1GranPrix gp : model.getGranPrixs(year)) {
                    F1Race unique = JkStreams.findUnique(gp.getRaces(), r -> r.getEntrant().getDriver().equals(d));
                    ResultCellPoints rcell = new ResultCellPoints();
                    if(unique != null) {
                        rcell.setValue(unique.getPoints());
                        rcell.setComputedPoints(unique.getPoints());
                        rcell.setPos(unique.getPos());
                    }
                    rv.getCellMap().put(gp, rcell);
                }
                try {
                    ResultCellPoints cp = new ResultCellPoints();
                    cp.setComputedPoints(getTotalPoints(rv.getCellMap()));
                    F1SeasonPoints spoint = seasonPoints.get(d.getFullName());
                    cp.setValue(spoint == null ? 0d : spoint.getFinalPoints());
                    rv.setTotal(cp);
                    sv.getDriverResults().add(rv);
                } catch (Exception ex) {
                    LOG.error("year={}, entryName={}", year, d);
                    throw ex;
                }
            }
            AtomicInteger pos = new AtomicInteger(1);
            JkStreams.reverseOrder(sv.getDriverResults(), Comparator.comparing(r -> r.getTotal().getValue()))
                    .forEach(drv -> drv.getTotal().setPos(pos.getAndIncrement()));
            Collections.sort(sv.getDriverResults());

            List<F1Team> teams = JkStreams.mapUniq(model.getEntrants(year), F1Entrant::getTeam);
            for (F1Team team : teams) {
                ResultView rv = new ResultView();
                rv.setEntryName(team.getTeamName());
                for (F1GranPrix gp : model.getGranPrixs(year)) {
                    double sum = gp.getRaces().stream().filter(r -> r.getEntrant().getTeam().equals(team)).mapToDouble(F1Race::getPoints).sum();
                    ResultCellPoints rcell = new ResultCellPoints();
                    rcell.setValue(sum);
                    rcell.setComputedPoints(sum);
                    rv.getCellMap().put(gp, rcell);
                }
                try {
                    ResultCellPoints cp = new ResultCellPoints();
                    cp.setComputedPoints(getTotalPoints(rv.getCellMap()));
                    F1SeasonPoints spoint = seasonPoints.get(team.getTeamName());
                    cp.setValue(spoint == null ? 0d : spoint.getFinalPoints());
                    rv.setTotal(cp);
                    sv.getTeamResults().add(rv);
                } catch (Exception ex) {
                    LOG.error("year={}, entryName={}", year, team);
                    throw ex;
                }
            }

            for (F1GranPrix gp : model.getGranPrixs(year)) {
                pos.set(1);
                JkStreams.reverseOrder(sv.getTeamResults(), Comparator.comparing(tr -> tr.getCellMap().get(gp).getValue()))
                        .forEach(tr -> tr.getCellMap().get(gp).setPos(pos.getAndIncrement()));
            }

            pos.set(1);
            JkStreams.reverseOrder(sv.getTeamResults(), Comparator.comparing(r -> r.getTotal().getValue()))
                    .forEach(drv -> drv.getTotal().setPos(pos.getAndIncrement()));
            Collections.sort(sv.getTeamResults());

            return sv;
        });
    }

    @Override
    public int getSelectedYear() {
        return selectedYear.get();
    }

    private Double getTotalPoints(Map<F1GranPrix, ResultCellPoints> raceMap) {
        double sum = 0d;
        for (ResultCellPoints race : raceMap.values()) {
            if(race != null) {
                sum += race.getValue();
            }
        }
        return sum;
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
