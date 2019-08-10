package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.RepoField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class F1Circuit extends RepoEntity {

    @RepoField
    private String country;
    @RepoField
    private String city;

    public F1Circuit() {
    }

    public F1Circuit(String country, String city) {
        this.city = city;
        this.country = country;
    }

    @Override
    public String getPrimaryKey() {
        return strf("{}-{}", country, city);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
