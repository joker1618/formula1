import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1Entrant extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private Integer year;
@ManyToOne
private F1Team team;
private Integer carNo;
@ManyToOne
private F1Driver driver;

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
public Integer getYear() {
return year;
}
public void setYear(Integer year) {
this.year = year;
}
@ManyToOne
public F1Team getTeam() {
return team;
}
public void setTeam(F1Team team) {
this.team = team;
}
public Integer getCarNo() {
return carNo;
}
public void setCarNo(Integer carNo) {
this.carNo = carNo;
}
@ManyToOne
public F1Driver getDriver() {
return driver;
}
public void setDriver(F1Driver driver) {
this.driver = driver;
}
}
