package xxx.joker.apps.formula1.fxgui.fxview;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.F1GuiModel;
import xxx.joker.apps.formula1.fxgui.fxmodel.F1GuiModelImpl;
import xxx.joker.apps.formula1.model.F1ModelNew;
import xxx.joker.apps.formula1.model.F1ModelNewImpl;

public abstract class SubPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger(SubPane.class);

    protected F1ModelNew model = F1ModelNewImpl.getInstance();
    protected F1GuiModel guiModel = F1GuiModelImpl.getInstance();

    protected SubPane() {
        getStyleClass().add("centralPane");
    }

}
