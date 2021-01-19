package javvernaut.votingsystem.util;

import javvernaut.votingsystem.model.Vote;
import javvernaut.votingsystem.to.VoteTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VoteUtil {
    public static VoteTo asTo(Vote vote) {
        return new VoteTo(vote.id(), vote.getDate(), vote.getRestaurant().getName());
    }
}
