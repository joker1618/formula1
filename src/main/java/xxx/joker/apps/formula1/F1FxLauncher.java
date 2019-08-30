package xxx.joker.apps.formula1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.stage.Stage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.scenicview.ScenicView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxmodel.StatsLine;
import xxx.joker.apps.formula1.fxgui.fxview.RootPane;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.libs.core.debug.JkDebug;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class F1FxLauncher extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(F1FxLauncher.class);
    private static boolean scenicView;
    private RootPane rootPane;


    @Override
    public void start(Stage primaryStage) {
        this.rootPane = new RootPane();

        // Create scene
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setRoot(rootPane);
        scene.getStylesheets().add(getClass().getResource("/css/common.css").toExternalForm());

//        initScene();

        // Show stage
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();


        if(scenicView) {
            ScenicView.show(scene);
        }
    }


    @Override
    public void stop() throws Exception {
        LOG.debug("STOP APP");
        JkDebug.displayTimes();
//        JfxTable<StatsLine> table = (JfxTable<StatsLine>) rootPane.lookup(".homePane .jfxTable");
//        display("border {}", ToStringBuilder.reflectionToString(table.getBorder(), ToStringStyle.MULTI_LINE_STYLE));

    }

    public static void main(String[] args) {
        scenicView = args.length > 0 && args[0].equals("-sv");
        launch(args);
    }


}
