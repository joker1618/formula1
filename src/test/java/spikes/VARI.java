package spikes;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import org.junit.Test;
import xxx.joker.apps.formula1.model.F1Model;
import xxx.joker.apps.formula1.model.F1ModelImpl;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkConsole;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.core.web.JkWeb;
import xxx.joker.libs.repository.util.MigrateToHibernate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class VARI {

    @Test
    public void aa() throws IOException, ClassNotFoundException {
        Path ciao = Paths.get("ciao");
        Path mondo = Paths.get("mondo/palo");

        List<String> lines = new ArrayList<>();
        lines.add(strf("from ciao|relAll:{}|relAbs:{}|absRel:{}|abs2:{}",
            ciao.relativize(mondo),
            ciao.relativize(mondo.toAbsolutePath()),
            ciao.toAbsolutePath().relativize(mondo),
            ciao.toAbsolutePath().relativize(mondo.toAbsolutePath())
        ));
    }

    @Test
    public void aa1() throws IOException, ClassNotFoundException {
        F1Model model = F1ModelImpl.getInstance();
        MigrateToHibernate.migrate(Paths.get("Zfolder"), model.getRepoCtx());
        display("END");
    }

}
