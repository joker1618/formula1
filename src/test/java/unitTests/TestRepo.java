package unitTests;

import org.junit.Test;
import xxx.joker.libs.core.format.JkOutput;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.repository.export.TmpFmt;

import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class TestRepo {

    @Test
    public void doTest() {
        Country italy = new Country("italy");
        Country france = new Country("france");
        Country spain = new Country("spain");

        List<Country> countries1 = Arrays.asList(italy, spain);
        List<Country> countries2 = Arrays.asList(italy, france);

        Driver kimi = new Driver("kimi", italy, JkConvert.toHashSet(countries1));
        Driver alonso = new Driver("alonso", new Country("spain"), JkConvert.toHashSet(Arrays.asList(new Country("italy"), spain)));
//        Driver alonso = new Driver("alonso", new Country("spain"), new HashSet<>(new Country("italy"), spain));

        Season season = new Season(kimi, Arrays.asList(kimi, alonso), france);

        F1ModeTest model = F1ModeTestImpl.getInstance();

        model.add(season);

        model.getDataSets().forEach((c,elist) -> {
            display(c.getSimpleName());
            List<String> lines = TmpFmt.get().formatCsv(elist);
            display("{}\n", JkOutput.columnsView(lines));
        });
    }
}
