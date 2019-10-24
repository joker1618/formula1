package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;

public class F1Team extends RepoEntity {

    @EntityField
    private String teamName;
    @EntityField
    private String country;

    public F1Team() {
    }

    public F1Team(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String getPrimaryKey() {
        return teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
