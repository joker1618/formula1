package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;
import xxx.joker.libs.datalayer.entities.RepoResource;

public class Country extends RepoEntity {

    @EntityField
    private String name;
    @EntityField
    private String code;

    public Country() {
    }

    public Country(String name) {
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

}
