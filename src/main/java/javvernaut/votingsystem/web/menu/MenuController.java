package javvernaut.votingsystem.web.menu;

import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.DateUtil.checkDateIsAfterTheCurrent;
import static javvernaut.votingsystem.util.ValidationUtil.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = MenuController.MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {

    public static final String MENUS_URL = "/admin/restaurants/{restaurantId}/menus";
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return menuRepository.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(menuRepository.findByIdAndRestaurantId(id, restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Menu> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Menu menu) {
        log.info("create {}", menu);
        checkNew(menu);
        checkDateIsAfterTheCurrent(menu.getDate(), "New date must be greater the current");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MENUS_URL + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Menu menu) {
        log.info("change date of menu={}", id);
        assureIdConsistent(menu, id);
        Menu existed = checkNotFoundWithId(menuRepository.findByIdAndRestaurantId(id, restaurantId),
                "Menu id=" + id + " doesn't belong to restaurant id=" + restaurantId);
        checkDateIsAfterTheCurrent(existed.getDate(), "Date cannot be changed");
        checkDateIsAfterTheCurrent(menu.getDate(), "New date must be greater the current");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        menuRepository.save(menu);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        Menu menu = checkNotFoundWithId(menuRepository.findByIdAndRestaurantId(id, restaurantId), id);
        checkDateIsAfterTheCurrent(menu.getDate(), "Menu cannot be removed");
        menuRepository.delete(menu);
    }
}
