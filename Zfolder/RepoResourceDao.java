import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoResourceDao extends JpaRepository<RepoResource, Long> {

}