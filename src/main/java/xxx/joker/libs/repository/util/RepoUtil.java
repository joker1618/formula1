package xxx.joker.libs.repository.util;

import xxx.joker.libs.core.runtimes.JkReflection;
import xxx.joker.libs.core.runtimes.JkRuntime;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.repository.design.RepoEntity;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepoUtil {

    public static boolean isOfClass(Class<?> toFind, Class<?>... elems) {
        for(Class<?> c : elems) {
            if(c == toFind) return true;
        }
        return false;
    }

    public static List<Class<?>> scanPackages(Class<?> launcherClazz, String... pkgsArr) {
        Set<Class<?>> classes = new HashSet<>();

        List<String> pkgsToScan = JkConvert.toList(pkgsArr);
        pkgsToScan.forEach(pkg -> classes.addAll(JkRuntime.findClasses(launcherClazz, pkg)));
        classes.removeIf(c -> !JkReflection.isInstanceOf(c, RepoEntity.class));
        classes.removeIf(c -> Modifier.isAbstract(c.getModifiers()));
        classes.removeIf(c -> Modifier.isInterface(c.getModifiers()));

        return JkConvert.toList(classes);
    }
}
