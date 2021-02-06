package javvernaut.votingsystem.web.item;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Item;
import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.ItemRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.to.ItemTo;
import javvernaut.votingsystem.util.ItemUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static javvernaut.votingsystem.util.DateUtil.checkDateIsAfterTheCurrent;
import static javvernaut.votingsystem.util.ItemUtil.updatePriceFromTo;
import static javvernaut.votingsystem.util.ValidationUtil.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = ItemController.ITEMS_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    public static final String ITEMS_URL = "/api/admin/restaurants/{restaurantId}/menus/{menuId}/items";
    private final ItemRepository itemRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;


    @GetMapping
    public List<ItemTo> getAll(@PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("get all");
        return ItemUtil.getTos(itemRepository.findAllByMenuIdAndMenuRestaurantId(menuId, restaurantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemTo> get(@PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("get item {}", id);
        Optional<Item> item = itemRepository.findByIdAndMenuIdAndMenuRestaurantIdFetchAll(id, menuId, restaurantId);
        return item.map(value -> ResponseEntity.ok(ItemUtil.createTo(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemTo> createWithLocation(
            @PathVariable int restaurantId,
            @PathVariable int menuId,
            @Valid @RequestBody ItemTo itemTo) {
        log.info("add item {} to menu", itemTo);
        Menu menu = checkNotFoundWithId(menuRepository.findByIdAndRestaurantId(menuId, restaurantId), menuId);
        checkDateIsAfterTheCurrent(menu.getMenuDate(), "Item cannot be added.");
        Dish dish = checkNotFoundWithId(dishRepository.findByIdAndRestaurantId(itemTo.getDishId(), restaurantId), itemTo.getDishId());
        Item created = itemRepository.save(new Item(null, menu, dish, itemTo.getPrice()));
        URI uriOFNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ITEMS_URL + "/{id}")
                .buildAndExpand(restaurantId, menuId, created.getId()).toUri();
        return ResponseEntity.created(uriOFNewResource).body(ItemUtil.createTo(created));
    }


    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updatePrice(
            @PathVariable int restaurantId,
            @PathVariable int menuId,
            @PathVariable int id,
            @Valid @RequestBody ItemTo itemTo) {
        log.info("edit {}", itemTo);
        assureIdConsistent(itemTo, id);
        Item item = checkNotFoundWithId(itemRepository.findByIdAndMenuIdAndMenuRestaurantIdFetchAll(itemTo.getId(),
                menuId, restaurantId), "Item with id=" + itemTo.getId() + " not found");
        checkDateIsAfterTheCurrent(item.getMenu().getMenuDate(), "Item id = " + itemTo.getId() + " cannot be updated.");
        updatePriceFromTo(item, itemTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("delete item {} from menu {}", id, menuId);
        Item item = checkNotFoundWithId(
                itemRepository.findByIdAndMenuIdAndMenuRestaurantId(id, menuId, restaurantId),
                "Item with id=" + id + " not found");
        checkDateIsAfterTheCurrent(item.getMenu().getMenuDate(), "Item id = " + id + " cannot be deleted.");
        checkSingleModification(itemRepository.deleteByItem(item),"Item id=" + item.getId() + " missed");
    }
}
