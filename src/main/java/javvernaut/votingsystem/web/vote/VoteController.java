package javvernaut.votingsystem.web.vote;

import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.model.Vote;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.repository.UserRepository;
import javvernaut.votingsystem.repository.VoteRepository;
import javvernaut.votingsystem.util.exception.IllegalRequestDataException;
import javvernaut.votingsystem.util.security.AuthorizedUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.DateUtil.*;
import static javvernaut.votingsystem.util.ValidationUtil.checkNotFoundWithId;
import static javvernaut.votingsystem.util.ValidationUtil.checkSingleModification;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(VoteController.VOTES_URL)
public class VoteController {
    public static final String VOTES_URL = "/api/votes";

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("get all votes for user id={}", authorizedUser.getId());
        return voteRepository.findAllByUserId(authorizedUser.getId());
    }

    @GetMapping("/current")
    public ResponseEntity<Vote> get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("get current vote");
        return ResponseEntity.of(voteRepository.findByUserIdAndDate(authorizedUser.getId(), current_date));
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthorizedUser authorizedUser,
                                                   @RequestParam int restaurantId) {
        log.info("create vote to restaurant id={}", restaurantId);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findByIdAndMenuDate(restaurantId, current_date),
                "Restaurant id=" + restaurantId + " not present menu today");
        Vote created = voteRepository.save(new Vote(null, userRepository.getOne(authorizedUser.getId()), restaurant, current_date));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(VOTES_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping()
    @Transactional
    @ResponseStatus(NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthorizedUser authorizedUser, @RequestParam int restaurantId) {
        log.info("update vote to restaurant id={}", restaurantId);
        Vote existed = checkVoteNotFound(authorizedUser);
        if (!current_time.isBefore(votes_deadline) || existed.getRestaurant().id() == restaurantId) {
            throw new IllegalRequestDataException("Vote cannot be changed");
        }
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findByIdAndMenuDate(restaurantId, current_date),
                "Restaurant id=" + restaurantId + " not present menu today");
        existed.setRestaurant(restaurant);
    }

    @DeleteMapping()
    @ResponseStatus(NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("delete vote");
        Vote existed = checkVoteNotFound(authorizedUser);
        if (existed.getVoteDate().isBefore(current_date) || !current_time.isBefore(votes_deadline)) {
            throw new IllegalRequestDataException("Vote cannot be deleted");
        }
        checkSingleModification(voteRepository.deleteByVote(existed), "Vote id=" + existed.getId() + " missed");
    }

    private Vote checkVoteNotFound(AuthorizedUser authorizedUser) {
        return checkNotFoundWithId(voteRepository.findByUserIdAndDate(authorizedUser.getId(), current_date),
                "Vote not found");
    }
}
