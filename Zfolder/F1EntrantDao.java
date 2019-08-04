import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1EntrantDao extends JpaRepository<F1Entrant, Long> {

}