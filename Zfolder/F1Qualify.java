import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1Qualify extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private String gpPK;
private Integer pos;
@ManyToOne
private F1Entrant entrant;
private Integer finalGrid;
private List times;

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
@ManyToOne
public F1Entrant getEntrant() {
return entrant;
}
public void setEntrant(F1Entrant entrant) {
this.entrant = entrant;
}
public Integer getFinalGrid() {
return finalGrid;
}
public void setFinalGrid(Integer finalGrid) {
this.finalGrid = finalGrid;
}
public List getTimes() {
return times;
}
public void setTimes(List times) {
this.times = times;
}
}
