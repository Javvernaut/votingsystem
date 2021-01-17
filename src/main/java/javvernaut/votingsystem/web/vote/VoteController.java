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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.config.AppConfig.*;
import static javvernaut.votingsystem.util.ValidationUtil.checkNotFoundWithId;
import static javvernaut.votingsystem.util.ValidationUtil.checkSingleModification;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(VoteController.VOTES_URL)
public class VoteController {

    public static final String VOTES_URL = "/votes";
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("get all votes for user id={}", authorizedUser.getId());
        return voteRepository.findAllByUserId(authorizedUser.getId());
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthorizedUser authorizedUser,
                                                   @RequestParam int restaurantId) {
        log.info("create new vote");
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findByIdAndMenuDate(restaurantId, CURRENT_DATE),
                "Restaurant id=" + restaurantId + " not present menu today");
        Vote created = voteRepository.save(new Vote(null, userRepository.getOne(authorizedUser.getId()), restaurant, CURRENT_DATE));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(VOTES_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthorizedUser authorizedUser, @PathVariable int id,
                       @RequestParam int restaurantId) {
        log.info("update vote to restaurant id={}", id);
        Vote existed = checkNotFoundWithId(voteRepository.findByIdAndUserIdAndDate(id, authorizedUser.getId(), CURRENT_DATE), id);
        if (!CURRENT_TIME.isBefore(VOTES_DEADLINE)) {
            throw new IllegalRequestDataException("Vote cannot be changed");
        }
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findByIdAndMenuDate(restaurantId, CURRENT_DATE),
                "Restaurant id=" + restaurantId + " not present menu today");
        existed.setRestaurant(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser, @PathVariable int id) {
        log.info("delete vote id={}", id);
        Vote existed = checkNotFoundWithId(voteRepository.findByIdAndUserIdAndDate(id, authorizedUser.getId(), CURRENT_DATE), id);
        if (existed.getDate().isBefore(CURRENT_DATE) || !CURRENT_TIME.isBefore(VOTES_DEADLINE)) {
            throw new IllegalRequestDataException("Vote cannot be deleted");
        }
        checkSingleModification(voteRepository.delete(id), "Vote id=" + id + ", user id=" + authorizedUser.getId() + " missed");
    }
}
