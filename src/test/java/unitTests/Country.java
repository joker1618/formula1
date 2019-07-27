package unitTests;

import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.design.RepoField;

public class Country extends RepoEntity {

    @RepoField
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
