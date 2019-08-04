import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1CircuitDao extends JpaRepository<F1Circuit, Long> {

}