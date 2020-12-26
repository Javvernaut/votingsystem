package javvernaut.votingsystem.repository.jpa;

import javvernaut.votingsystem.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findAll();

    @EntityGraph(attributePaths = "restaurant", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m")
    List<Menu> getWithRestaurants();
}
