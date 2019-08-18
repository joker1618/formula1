package xxx.joker.apps.formula1.fxgui.fxmodel;

import javafx.scene.image.Image;
import xxx.joker.apps.formula1.model.entities.F1Driver;
import xxx.joker.apps.formula1.model.entities.F1GranPrix;

import java.util.function.Consumer;

public interface F1GuiModel {

    FxCountry getNation(String nationName);
    Image getGpTrackMap(F1GranPrix gp);
    Image getDriverCover(F1Driver driver);
    SeasonView getSeasonView(int year);

    int getSelectedYear();
    void setSelectedYear(int year);
    void addChangeActionYear(Consumer<Integer> action);

    void setSelectedGranPrix(F1GranPrix gp);
    void addChangeActionGranPrix(Consumer<F1GranPrix> action);
}
