import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class F1Country extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private String name;
private String code;
@ManyToOne
private RepoResource flagIcon;
@ManyToOne
private RepoResource flagImage;

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
public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}
public String getCode() {
return code;
}
public void setCode(String code) {
this.code = code;
}
@ManyToOne
public RepoResource getFlagIcon() {
return flagIcon;
}
public void setFlagIcon(RepoResource flagIcon) {
this.flagIcon = flagIcon;
}
@ManyToOne
public RepoResource getFlagImage() {
return flagImage;
}
public void setFlagImage(RepoResource flagImage) {
this.flagImage = flagImage;
}
}
