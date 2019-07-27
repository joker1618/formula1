package xxx.joker.apps.formula1.fxgui.fxview.panes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.fxgui.fxview.SubPane;
import xxx.joker.apps.formula1.fxgui.fxview.box.TableBoxCaption;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTable;
import xxx.joker.apps.formula1.fxgui.fxview.control.JfxTableCol;
import xxx.joker.apps.formula1.model.entities.F1Driver;
import xxx.joker.libs.core.datetime.JkDates;

import java.time.LocalDate;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class DriversPane extends SubPane {

    private static final Logger LOG = LoggerFactory.getLogger(DriversPane.class);

    public DriversPane() {
        getStyleClass().add("driversPane");

        setLeft(createTableDrivers());
    }

    private TableBoxCaption<F1Driver> createTableDrivers() {
        JfxTableCol<F1Driver, String> colName = JfxTableCol.createCol("NAME", "fullName");
        JfxTableCol<F1Driver, String> colNation = JfxTableCol.createCol("NATION", "country");
        JfxTableCol<F1Driver, LocalDate> colBorn = JfxTableCol.createCol("BORN", F1Driver::getBirthDay, bd -> JkDates.format(bd, "dd/MM/yyyy"));

        JfxTable<F1Driver> tableDrivers = new JfxTable<>();
        tableDrivers.add(colName, colNation, colBorn);
        Set<F1Driver> drivers = model.getDrivers();
        tableDrivers.update(drivers);

        return new TableBoxCaption<>(strf("DRIVERS   ({})", drivers.size()), tableDrivers);
    }
}
