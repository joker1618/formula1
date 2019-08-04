import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1TeamDao extends JpaRepository<F1Team, Long> {

}