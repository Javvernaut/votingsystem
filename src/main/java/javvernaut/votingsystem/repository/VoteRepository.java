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
    @Query("SELECT v FROM Vote v WHERE v.user.id=:id ORDER BY v.voteDate DESC")
    List<Vote> findAllByUserId(int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:id AND v.voteDate=:date")
    Optional<Vote> findByUserIdAndDate(int id, LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v=:vote")
    int deleteByVote(Vote vote);
}
