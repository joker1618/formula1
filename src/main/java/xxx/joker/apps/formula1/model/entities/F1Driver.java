package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;

import java.time.LocalDate;

public class F1Driver extends RepoEntity {

    @EntityField
    private String fullName;
    @EntityField
    private String country;
    @EntityField
    private String city;
    @EntityField
    private LocalDate birthDay;

    public F1Driver() {
    }

    public F1Driver(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getPrimaryKey() {
        return fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }
}
