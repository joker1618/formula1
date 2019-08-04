import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface F1CountryDao extends JpaRepository<F1Country, Long> {

}