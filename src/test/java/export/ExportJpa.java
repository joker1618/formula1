package export;

import common.CommonTest;
import org.junit.Test;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.libs.datalayer.util.MigrateToHibernate;

import java.io.IOException;
import java.nio.file.Paths;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class ExportJpa extends CommonTest {

    @Test
    public void migrateToJpa() throws IOException, ClassNotFoundException {
        F1Model model = F1ModelImpl.getInstance();
        MigrateToHibernate.createJavaFiles(Paths.get("Zfolder"), model.getRepoCtx());
        display("END");
    }

}
