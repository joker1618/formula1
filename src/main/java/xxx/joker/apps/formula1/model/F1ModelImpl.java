package xxx.joker.apps.formula1.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.common.F1Const;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.repository.JkRepoFile;
import xxx.joker.libs.repository.entities.RepoResource;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class F1ModelImpl extends JkRepoFile implements F1Model {

    private static final Logger LOG = LoggerFactory.getLogger(F1ModelImpl.class);
    private static F1Model instance;

//    private Map<Integer, F1Season> seasonMap = new HashMap<>();

    private F1ModelImpl() {
        super(F1Const.REPO_FOLDER, F1Const.DB_NAME, "xxx.joker.apps.formula1.model.entities");
    }

    public static synchronized F1Model getInstance() {
        if(instance == null) {
            JkDebug.startTimer("createModel");
            instance = new F1ModelImpl();
            JkDebug.stopTimer("createModel");
//            instance = ModelHandler.createHandler();
        }
        return instance;
    }
//
//    private static class ModelHandler implements InvocationHandler {
//
//        private F1ModelNew model;
//
//        public ModelHandler() {
//            F1ModelNew m = new F1ModelNewImpl();
//            ClassLoader loader = F1ModelNew.class.getClassLoader();
//            Class[] interfaces = {F1ModelNew.class};
//            model = (F1ModelNew) Proxy.newProxyInstance(loader, interfaces, this);
//        }
//
//        public static F1ModelNew createHandler() {
//            ModelHandler mh = new ModelHandler();
//            return mh.model;
//        }
//
//        @Override
//        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            try {
//                LOG.trace();
//
//            } finally {
//
//            }
//        }
//    }

    @Override
    public RepoResource getDriverCover(F1Driver driver) {
        return getResource(driver.getPrimaryKey(), "driver cover");
    }

    @Override
    public RepoResource saveDriverCover(Path imgPath, F1Driver driver) {
        return addResource(imgPath, driver.getPrimaryKey(), "driver cover");
    }


    @Override
    public RepoResource getGpTrackMap(F1GranPrix gp) {
        return getResource(gp.getPrimaryKey(), "trackMap");
    }

    @Override
    public RepoResource saveGpTrackMap(Path imgPath, F1GranPrix gp) {
        return addResource(imgPath, gp.getPrimaryKey(), "trackMap");
    }

    @Override
    public F1Country getCountry(String countryName) {
        return get(F1Country.class, c -> c.getName().equalsIgnoreCase(countryName));
    }

//    @Override
//    public Set<F1Country> getCountries() {
//        return getDataSet(F1Country.class);
//    }

    @Override
    public F1Team getTeam(String teamName) {
        return JkStreams.findUnique(getTeams(), t -> t.getTeamName().equals(teamName));
    }

    @Override
    public Set<F1Team> getTeams() {
        return getDataSet(F1Team.class);
    }

    @Override
    public F1Driver getDriver(String driverName) {
        return JkStreams.findUnique(getDrivers(), d -> d.getFullName().equals(driverName));
    }

    @Override
    public Set<F1Driver> getDrivers() {
        return getDataSet(F1Driver.class);
    }

    @Override
    public Set<F1Entrant> getEntrants() {
        return getDataSet(F1Entrant.class);
    }

    @Override
    public List<F1Entrant> getEntrants(int year) {
        return getList(F1Entrant.class, e -> e.getYear() == year);
    }

    @Override
    public Set<F1GranPrix> getGranPrixs() {
        return getDataSet(F1GranPrix.class);
    }

    @Override
    public List<F1GranPrix> getGranPrixs(int year) {
        return getList(F1GranPrix.class, e -> e.getYear() == year);
    }

    @Override
    public Set<F1Circuit> getCircuits() {
        return getDataSet(F1Circuit.class);
    }

    @Override
    public F1Circuit getCircuit(String city, String nation) {
        F1Circuit circuit = new F1Circuit(nation, city);
        return getByPk(circuit);
    }

    @Override
    public List<Integer> getAvailableYears() {
        return JkStreams.mapFilterSortUniq(
                getEntrants(),
                F1Entrant::getYear,
                f -> true,
                Comparator.reverseOrder()
        );
    }

    @Override
    public int getNumQualifyRounds(int year) {
        List<F1GranPrix> gps = getGranPrixs(year);
        if(gps.isEmpty())   return -1;

        List<F1Qualify> qual = gps.get(0).getQualifies();
        if(qual.isEmpty())   return -1;

        return qual.get(0).getTimes().size();
    }


}
