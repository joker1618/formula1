import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;

@Entity
public class RepoUri extends JpaEntity implements Serializable {

@Id
@GeneratedValue
private Long jpaID;

private Long entityId;
private JkDateTime creationTm;
private Path path;
private String md5;
private RepoUriType type;
private RepoMetaData metaData;

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
public Path getPath() {
return path;
}
public void setPath(Path path) {
this.path = path;
}
public String getMd5() {
return md5;
}
public void setMd5(String md5) {
this.md5 = md5;
}
public RepoUriType getType() {
return type;
}
public void setType(RepoUriType type) {
this.type = type;
}
public RepoMetaData getMetaData() {
return metaData;
}
public void setMetaData(RepoMetaData metaData) {
this.metaData = metaData;
}
}
