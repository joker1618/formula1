package spikes;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import org.junit.Test;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.web.JkWeb;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

}
