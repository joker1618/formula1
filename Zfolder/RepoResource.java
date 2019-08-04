import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class RepoResource extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private String name;
@ManyToOne
private RepoUri uri;
private RepoTags tags;

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
@ManyToOne
public RepoUri getUri() {
return uri;
}
public void setUri(RepoUri uri) {
this.uri = uri;
}
public RepoTags getTags() {
return tags;
}
public void setTags(RepoTags tags) {
this.tags = tags;
}
}
