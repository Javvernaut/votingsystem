package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.ItemRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.to.ItemTo;
import javvernaut.votingsystem.to.RestaurantTo;
import javvernaut.votingsystem.util.ItemUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static javvernaut.votingsystem.util.DateUtil.current_date;
import static javvernaut.votingsystem.util.ValidationUtil.checkNotFoundWithId;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = RestaurantUserController.USER_RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantUserController {
    public static final String USER_RESTAURANTS_URL = "/api/restaurants";

    private final RestaurantRepository restaurantRepository;
    private final ItemRepository itemRepository;
    private final MenuRepository menuRepository;

    @GetMapping
    public List<RestaurantTo> getAllWithVotesCountForCurrentDay() {
        log.info("get all restaurants that presented menu for current date");
        return restaurantRepository.findAllTosWithVotesByMenuDate(current_date);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTo> getForCurrentDay(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return ResponseEntity.of(restaurantRepository.findToByIdAndMenuDate(id, current_date));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<Menu> getMenuForCurrentDate(@PathVariable int id) {
        log.info("get current menu for restaurant id={}", id);
        Menu menu = checkNotFoundWithId(menuRepository.findByRestaurantIdAndDate(id, current_date),
                "Restaurant id = " + id + " did not provide menu today");
        return ResponseEntity.ok(menu);
    }

    @GetMapping("/{id}/menu/items")
    public List<ItemTo> getMenuItemsForCurrentDate(@PathVariable int id) {
        Menu menu = checkNotFoundWithId(menuRepository.findByRestaurantIdAndDate(id, current_date),
                "Restaurant id = " + id + " did not provide menu today");
        return ItemUtil.getTos(itemRepository.findAllByMenuIdAndMenuRestaurantId(menu.id(), id));
    }
}
