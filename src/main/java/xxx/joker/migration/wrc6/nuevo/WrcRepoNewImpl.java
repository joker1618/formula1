package xxx.joker.migration.wrc6.nuevo;

import javafx.scene.image.Image;
import xxx.joker.libs.core.cache.JkCache;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.datalayer.JkRepoFile;
import xxx.joker.migration.wrc6.nuevo.entities.*;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class WrcRepoNewImpl extends JkRepoFile implements WrcRepoNew {

    private static final WrcRepoNew instance = new WrcRepoNewImpl();

    private List<Consumer<WrcRepoNew>> changeActions = new ArrayList<>();
    private JkCache<WrcNation, Image> cacheFlag = new JkCache<>();


    private WrcRepoNewImpl() {
        super(Paths.get("wrc-data/migrated"), "wrc", "xxx.joker.migration.wrc6.nuevo.entities");
    }

    public synchronized static WrcRepoNew getInstance() {
        return instance;
    }

    @Override
    public WrcNation getNation(String nationName) {
        return get(WrcNation.class, n -> nationName.equals(n.getName()));
    }

    @Override
    public List<WrcNation> getNations() {
        return getList(WrcNation.class);
    }

    @Override
    public Map<String, WrcNation> getNationMap() {
        return getMapSingle(WrcNation.class, WrcNation::getName);
    }

    @Override
    public WrcCar getCar(String carModel) {
        return get(WrcCar.class, car -> car.getCarModel().equals(carModel));
    }

    @Override
    public List<WrcCar> getCars() {
        return getList(WrcCar.class);
    }

    @Override
    public List<WrcStage> getStages(String nation) {
        return getStages(getNation(nation));
    }

    @Override
    public List<WrcStage> getStages(WrcNation nation) {
        Set<WrcStage> ds = getDataSet(WrcStage.class);
        return JkStreams.filterSort(ds, stage -> stage.getNation().equals(nation));
    }

    @Override
    public List<WrcSeason> getSeasons() {
        return getList(WrcSeason.class);
    }

    @Override
    public List<WrcRally> getRallies() {
        return getList(WrcRally.class);
    }

    @Override
    public List<WrcMatch> getMatches() {
        return getList(WrcMatch.class);
    }

    @Override
    public WrcSeason getActualSeason() {
        return get(WrcSeason.class, s -> !s.isFinished());
    }

    @Override
    public List<WrcSeason> getClosedSeasons() {
        return JkStreams.filterSort(getSeasons(), WrcSeason::isFinished, Comparator.comparing(WrcSeason::getCreationTm));
    }

    @Override
    public void registerActionChangeStats(Consumer<WrcRepoNew> action) {
        changeActions.add(action);
    }

    @Override
    public void refreshStats() {
        changeActions.forEach(action -> action.accept(this));
    }

    @Override
    public Image getFlag(WrcNation nation) {
        String str = strf("flags/{}.{}.flag.image.png", nation.getName(), nation.getCode());
        InputStream is = getClass().getClassLoader().getResourceAsStream(str);
        return cacheFlag.get(nation, () -> new Image(is));
    }

    @Override
    public List<FifaMatch> getFifaMatches() {
        return getList(FifaMatch.class);
    }


}
