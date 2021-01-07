package javvernaut.votingsystem.web.menu;

import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.util.exception.IllegalRequestDataException;
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
import java.time.LocalDate;
import java.util.List;

import static javvernaut.votingsystem.config.AppConfig.CURRENT_DATE;
import static javvernaut.votingsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = MenuAdminController.MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuAdminController {

    public static final String MENU_URL = "/admin/restaurants/{restaurantId}/menus";
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return menuRepository.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restaurantId, @PathVariable int id) {
        return ResponseEntity.of(menuRepository.findByIdAndRestaurantId(id, restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Menu> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Menu menu) {
        log.info("create {}", menu);
        checkNew(menu);
        checkDateIsAfterTheCurrent(menu.getDate());
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MENU_URL + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void changeDate(@PathVariable int restaurantId, @PathVariable int id, @RequestBody LocalDate date) {
        log.info("change date of menu={} to {}", id, date);
        Menu existed = menuRepository.findByIdAndRestaurantId(id, restaurantId).orElseThrow();
        if (existed.getDate().isBefore(CURRENT_DATE.plusDays(1)))
            throw new IllegalRequestDataException("Date cannot be changed");
        checkDateIsAfterTheCurrent(date);
        existed.setDate(date);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        menuRepository.deleteByIdAndRestaurantId(id, restaurantId);
    }

    private void checkDateIsAfterTheCurrent(LocalDate date) {
        if (!date.isAfter(CURRENT_DATE)) {
            throw new IllegalRequestDataException("New date must be greater the current");
        }
    }
}
