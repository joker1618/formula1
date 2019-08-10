package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;
import xxx.joker.libs.datalayer.entities.RepoResource;

public class F1Country extends RepoEntity {

    @RepoField
    private String name;
    @RepoField
    private String code;
    @RepoField
    private RepoResource flagIcon;
    @RepoField
    private RepoResource flagImage;

    public F1Country() {
    }

    public F1Country(String name) {
        this.name = name;
    }

    @Override
    public String getPrimaryKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RepoResource getFlagIcon() {
        return flagIcon;
    }

    public void setFlagIcon(RepoResource flagIcon) {
        this.flagIcon = flagIcon;
    }

    public RepoResource getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(RepoResource flagImage) {
        this.flagImage = flagImage;
    }


}
