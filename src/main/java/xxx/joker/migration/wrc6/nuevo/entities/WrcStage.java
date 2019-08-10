package xxx.joker.migration.wrc6.nuevo.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class WrcStage extends RepoEntity {

    @RepoField
    private WrcNation nation;
    @RepoField
    private String name;
    @RepoField
    private int num;
    @RepoField
    private int length;
    @RepoField
    private boolean specialStage;
    @RepoField
    private WrcSurface surface;


    @Override
    public String getPrimaryKey() {
        return strf("stage-{}-{}", nation.getName(), num);
    }

    public WrcNation getNation() {
        return nation;
    }

    public void setNation(WrcNation nation) {
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isSpecialStage() {
        return specialStage;
    }

    public void setSpecialStage(boolean specialStage) {
        this.specialStage = specialStage;
    }

    public WrcSurface getSurface() {
        return surface;
    }

    public void setSurface(WrcSurface surface) {
        this.surface = surface;
    }
}
