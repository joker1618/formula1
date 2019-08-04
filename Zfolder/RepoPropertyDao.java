import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoPropertyDao extends JpaRepository<RepoProperty, Long> {

}