import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1RaceDao extends JpaRepository<F1Race, Long> {

}