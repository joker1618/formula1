import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1GranPrix extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private Integer year;
private Integer num;
private LocalDate date;
@ManyToOne
private F1Circuit circuit;
private Double lapLength;
private Integer numLapsRace;
private F1FastLap fastLap;
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private List qualifies;
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private List races;

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
public Integer getNum() {
return num;
}
public void setNum(Integer num) {
this.num = num;
}
public LocalDate getDate() {
return date;
}
public void setDate(LocalDate date) {
this.date = date;
}
@ManyToOne
public F1Circuit getCircuit() {
return circuit;
}
public void setCircuit(F1Circuit circuit) {
this.circuit = circuit;
}
public Double getLapLength() {
return lapLength;
}
public void setLapLength(Double lapLength) {
this.lapLength = lapLength;
}
public Integer getNumLapsRace() {
return numLapsRace;
}
public void setNumLapsRace(Integer numLapsRace) {
this.numLapsRace = numLapsRace;
}
public F1FastLap getFastLap() {
return fastLap;
}
public void setFastLap(F1FastLap fastLap) {
this.fastLap = fastLap;
}
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
public List getQualifies() {
return qualifies;
}
public void setQualifies(List qualifies) {
this.qualifies = qualifies;
}
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
public List getRaces() {
return races;
}
public void setRaces(List races) {
this.races = races;
}
}
