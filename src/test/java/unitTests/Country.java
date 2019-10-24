package unitTests;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;

public class Country extends RepoEntity {

    @EntityField
    private String name;

    public Country() {
    }

    public Country(String name) {
        this.name = name;
    }

    @Override
    public String getPrimaryKey() {
        return "country:" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
