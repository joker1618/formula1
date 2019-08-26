package export;

import common.CommonTest;
import org.junit.Test;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.datalayer.entities.RepoResource;
import xxx.joker.libs.datalayer.util.MigrateToHibernate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class ExportImages extends CommonTest {

    private static Path outFolder = Paths.get("Zdata/export");

    @Test
    public void exportImages() {
        Path imgFolder = outFolder.resolve("img");

        F1Model model = F1ModelImpl.getInstance();
        model.getCountries().forEach(c -> {
            display("Exporting flag {}", c.getName());

            Path source = model.getUriPath(c.getFlagImage());
            Path target = imgFolder.resolve(strf("large/{}.{}", c.getName(), JkFiles.getExtension(source)));
            JkFiles.copy(source, target);

            source = model.getUriPath(c.getFlagIcon());
            target = imgFolder.resolve(strf("icon/{}.{}", c.getName(), JkFiles.getExtension(source)));
            JkFiles.copy(source, target);
        });

        model.getDrivers().forEach(d -> {
            RepoResource cover = model.getDriverCover(d);
            if(cover != null) {
                display("Exporting cover for {}", d.getFullName());
                Path source = model.getUriPath(cover);
                Path target = imgFolder.resolve(strf("cover/{}.{}", d.getFullName(), JkFiles.getExtension(source)));
                JkFiles.copy(source, target);
            }
        });

        model.getGranPrixs().forEach(gp -> {
            RepoResource gpTrackMap = model.getGpTrackMap(gp);
            if(gpTrackMap != null) {
                display("Exporting track map for {}", gp.getPrimaryKey());
                Path source = model.getUriPath(gpTrackMap);
                Path target = imgFolder.resolve(strf("trackMap/{}_{}.{}", gp.getPrimaryKey(), gp.getCircuit().getCountry(), JkFiles.getExtension(source)));
                JkFiles.copy(source, target);
            }
        });
    }

}
