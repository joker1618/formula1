package xxx.joker.apps.formula1.model;


import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.libs.datalayer.JkRepo;
import xxx.joker.libs.datalayer.entities.RepoResource;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface F1Model extends JkRepo {

    RepoResource getFlagIcon(Country country);
    RepoResource saveFlagIcon(Path imgPath, Country country);
    RepoResource getFlagImage(Country country);
    RepoResource saveFlagImage(Path imgPath, Country country);

    RepoResource getDriverCover(F1Driver driver);
    RepoResource saveDriverCover(Path imgPath, F1Driver driver);

    RepoResource getGpTrackMap(F1GranPrix gp);
    RepoResource saveGpTrackMap(Path imgPath, F1GranPrix gp);

    RepoResource getImageUnavailable();
    RepoResource saveImageUnavailable(Path imgPath);

    Country getCountry(String name);
    Set<Country> getCountries();

    F1Team getTeam(String teamName);
    Set<F1Team> getTeams();

    F1Driver getDriver(String driverName);
    Set<F1Driver> getDrivers();

    Set<F1Entrant> getEntrants();
    List<F1Entrant> getEntrants(int year);

    Set<F1GranPrix> getGranPrixs();
    List<F1GranPrix> getGranPrixs(int year);

    Set<F1Circuit> getCircuits();
    F1Circuit getCircuit(String city, String nation);

    Set<F1SeasonPoints> getSeasonPoints();
    List<F1SeasonPoints> getSeasonPoints(int year);
    Map<String, F1SeasonPoints> getSeasonPointsMap(int year);

    List<Integer> getAvailableYears();

    int getNumQualifyRounds(int year);

}
