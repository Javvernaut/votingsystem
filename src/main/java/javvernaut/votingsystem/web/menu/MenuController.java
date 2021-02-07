package javvernaut.votingsystem.web.menu;

import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = MenuController.MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "menus")
public class MenuController {
    public static final String MENUS_URL = "/api/admin/restaurants/{restaurantId}/menus";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping
    @Cacheable
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
    @CacheEvict(allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Menu menu) {
        log.info("create {}", menu);
        checkNew(menu);
        checkDateIsAfterTheCurrent(menu.getMenuDate(), "New date must be greater the current");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MENUS_URL + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Transactional
    @CacheEvict(allEntries = true)
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Menu menu) {
        log.info("change date of menu={}", id);
        assureIdConsistent(menu, id);
        Menu existed = checkNotFoundWithId(menuRepository.findByIdAndRestaurantId(id, restaurantId),
                "Menu id=" + id + " doesn't belong to restaurant id=" + restaurantId);
        checkDateIsAfterTheCurrent(existed.getMenuDate(), "Date cannot be changed");
        checkDateIsAfterTheCurrent(menu.getMenuDate(), "New date must be greater the current");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        menuRepository.save(menu);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        Menu menu = checkNotFoundWithId(menuRepository.findByIdAndRestaurantId(id, restaurantId), id);
        checkDateIsAfterTheCurrent(menu.getMenuDate(), "Menu cannot be deleted");
        checkSingleModification(menuRepository.delete(id), "Menu id=" + id + " missed");
    }
}
