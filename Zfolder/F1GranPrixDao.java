import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1GranPrixDao extends JpaRepository<F1GranPrix, Long> {

}