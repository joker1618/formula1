package xxx.joker.migration.wrc6.nuevo.entities;

import xxx.joker.libs.core.format.JkFormattable;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class WrcSurface implements JkFormattable<WrcSurface> {

    public enum GroundType {
        TARMAC,
        GRAVEL,
        SNOW
    }

    private Map<GroundType, Double> surfaces;

    public WrcSurface() {
        this.surfaces = new HashMap<>();
    }

    @Override
    public String format() {
        return JkStreams.join(surfaces.entrySet(), "--", p -> strf("{}:{}", p.getKey().name(), p.getValue()));
    }

    @Override
    public WrcSurface parse(String s) {
        List<String> surfList = JkStrings.splitList(s, "--");
        surfaces.clear();
        for (String surf : surfList) {
            String[] split = JkStrings.splitArr(surf, ":");
            surfaces.put(GroundType.valueOf(split[0]), Double.valueOf(split[1]));
        }
        return this;
    }



}
