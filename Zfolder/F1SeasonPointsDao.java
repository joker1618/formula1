import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1SeasonPointsDao extends JpaRepository<F1SeasonPoints, Long> {

}