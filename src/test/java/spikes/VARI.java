package spikes;

import org.junit.Test;
import xxx.joker.libs.core.utils.JkConvert;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static xxx.joker.libs.core.runtimes.JkReflection.getParametrizedTypes;
import static xxx.joker.libs.core.runtimes.JkReflection.isInstanceOf;
import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class VARI {

    @Test
    public void strfTest() throws IOException, ClassNotFoundException {
        int num = 23;
        display("%03d", num);
        display("%3d", num);
    }

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

    Map<String, Integer> map = new HashMap<>();
    Map<String, List<Long>> map2 = new HashMap<>();
    @Test
    public void aa2() throws IOException, ClassNotFoundException {
        Class<?>[] cArr = getParametrizedTypes(VARI.class, "map");
        List<Class<?>> classes = JkConvert.toList(cArr);
        display(classes);
        Class<?>[] cArr2 = getParametrizedTypes(VARI.class, "map2");
        List<?> classes2 = JkConvert.toList(cArr2);
        display(classes2);
    }

}
