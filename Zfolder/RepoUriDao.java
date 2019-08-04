import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoUriDao extends JpaRepository<RepoUri, Long> {

}