import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1SeasonPoints extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private int year;
private String name;
private int finalPos;
private double finalPoints;

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
public int getYear() {
return year;
}
public void setYear(int year) {
this.year = year;
}
public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}
public int getFinalPos() {
return finalPos;
}
public void setFinalPos(int finalPos) {
this.finalPos = finalPos;
}
public double getFinalPoints() {
return finalPoints;
}
public void setFinalPoints(double finalPoints) {
this.finalPoints = finalPoints;
}
}
