package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.to.ItemTo;
import javvernaut.votingsystem.to.RestaurantTo;
import javvernaut.votingsystem.util.RestaurantUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static javvernaut.votingsystem.config.AppConfig.CURRENT_DATE;
import static javvernaut.votingsystem.util.ValidationUtil.checkNotFoundWithId;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantUserController {

    private final RestaurantRepository restaurantRepository;

/*    @GetMapping
    public List<RestaurantTo> getAllWithVotesCountForCurrentDay() {
        log.info("get all restaurants that presented menu for current date");
        return restaurantRepository.findAllTosWithVotesByMenuDate(CURRENT_DATE);
    }*/

    @GetMapping
    public List<RestaurantTo> getAllWithVotesCountForCurrentDay() {
        log.info("get all restaurants that presented menu for current date");
        return RestaurantUtil.getTos(restaurantRepository.findAllTosWithVotesByMenuDate(CURRENT_DATE));
    }

/*    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTo> getForCurrentDay(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return  ResponseEntity.of(restaurantRepository.findToByIdAndMenuDate(id, CURRENT_DATE));
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTo> getForCurrentDay(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return  ResponseEntity.ok(
                RestaurantUtil.asTo(
                        checkNotFoundWithId(restaurantRepository.findWithVotesByIdAndMenuDate(id, CURRENT_DATE),
                                "Restaurant id = " + id + " did not present menu today")));
    }

    @GetMapping("/{id}/menu")
    public List<ItemTo> getMenuForCurrentDay(@PathVariable int id) {
        log.info("get current menu for restaurant id={}", id);
        return null;
    }
}
