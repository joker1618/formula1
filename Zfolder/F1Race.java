import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1Race extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private String gpPK;
private Integer pos;
private Integer startGrid;
@ManyToOne
private F1Entrant entrant;
private Integer laps;
private F1RaceOutcome outcome;
private JkDuration time;
private Double points;

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
public String getGpPK() {
return gpPK;
}
public void setGpPK(String gpPK) {
this.gpPK = gpPK;
}
public Integer getPos() {
return pos;
}
public void setPos(Integer pos) {
this.pos = pos;
}
public Integer getStartGrid() {
return startGrid;
}
public void setStartGrid(Integer startGrid) {
this.startGrid = startGrid;
}
@ManyToOne
public F1Entrant getEntrant() {
return entrant;
}
public void setEntrant(F1Entrant entrant) {
this.entrant = entrant;
}
public Integer getLaps() {
return laps;
}
public void setLaps(Integer laps) {
this.laps = laps;
}
public F1RaceOutcome getOutcome() {
return outcome;
}
public void setOutcome(F1RaceOutcome outcome) {
this.outcome = outcome;
}
public JkDuration getTime() {
return time;
}
public void setTime(JkDuration time) {
this.time = time;
}
public Double getPoints() {
return points;
}
public void setPoints(Double points) {
this.points = points;
}
}
