import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1DriverDao extends JpaRepository<F1Driver, Long> {

}