import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1Driver extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private String fullName;
private String country;
private String city;
private LocalDate birthDay;

public Long getJpaID() {
return jpaID;
}
public Long getEntityId() {
return entityId;
}
public void setEntityId(Long entityId) {
this.entityId = entityId;
}
public JkDateTime getCreationTm() {
return creationTm;
}
public void setCreationTm(JkDateTime creationTm) {
this.creationTm = creationTm;
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
