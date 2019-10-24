package unitTests;

import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;

import java.util.Set;

public class Driver extends RepoEntity {

    @EntityField
    private String fullName;
    @EntityField
    private Country country;
    @EntityField
    private Set<Country> countries;

    public Driver() {
    }

    public Driver(String fullName, Country country) {
        this.fullName = fullName;
        this.country = country;
    }

    public Driver(String fullName) {
        this.fullName = fullName;
    }

    public Driver(String fullName, Country country, Set<Country> countries) {
        this.fullName = fullName;
        this.country = country;
        this.countries = countries;
    }

    @Override
    public String getPrimaryKey() {
        return "driver-" + fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Country getCountry() {
        return country;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
