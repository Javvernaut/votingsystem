package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Vote> findAllByUserId(int id);

    Optional<Vote> findAllByUserIdAndDate(int id, LocalDate date);

    Optional<Vote> findByIdAndUserIdAndDate(int id, int userId, LocalDate currentDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(int id);
}
