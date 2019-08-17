package xxx.joker.apps.formula1.fxgui.fxview;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.F1GuiModel;
import xxx.joker.apps.formula1.fxgui.fxmodel.F1GuiModelImpl;
import xxx.joker.apps.formula1.fxgui.fxservice.StatsComputer;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;

public abstract class CentralPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(CentralPane.class);

    protected F1Model model = F1ModelImpl.getInstance();
    protected F1GuiModel guiModel = F1GuiModelImpl.getInstance();
    protected StatsComputer statsComputer = new StatsComputer();

    protected CentralPane() {
        getStyleClass().add("centralPane");
    }

}
