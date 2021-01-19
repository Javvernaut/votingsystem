package javvernaut.votingsystem.web.item;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Item;
import javvernaut.votingsystem.model.ItemId;
import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.ItemRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.to.ItemTo;
import javvernaut.votingsystem.util.ItemUtil;
import javvernaut.votingsystem.util.exception.IllegalRequestDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.DateUtil.checkDateIsAfterTheCurrent;
import static javvernaut.votingsystem.util.ItemUtil.updateFromTo;
import static javvernaut.votingsystem.util.ValidationUtil.assureIdConsistent;
import static javvernaut.votingsystem.util.ValidationUtil.checkNotFoundWithId;
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
        Item item = checkNotFoundWithId(
                itemRepository.findByDishIdAndMenuIdAndMenuRestaurantId(id, menuId, restaurantId),
                "Item with id=" + id + " not found"
        );
        return ResponseEntity.ok(ItemUtil.createTo(item));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ItemTo> createWithLocation(
            @PathVariable int restaurantId,
            @PathVariable int menuId,
            @Valid @RequestBody ItemTo itemTo) {
        log.info("add item {} to menu", itemTo);
        Menu menu = checkNotFoundWithId(menuRepository.findByIdAndRestaurantId(menuId, restaurantId), menuId);
        checkDateIsAfterTheCurrent(menu.getDate(), "Item id = " + itemTo.getId() + " cannot be added.");
        Dish dish = checkNotFoundWithId(dishRepository.findByIdAndRestaurantId(itemTo.getId(), restaurantId), itemTo.getId());
        ItemId itemId = new ItemId(menu.getId(), dish.getId());
        if (itemRepository.findById(itemId).isPresent()) {
            throw new IllegalRequestDataException("Item with id = " + itemId.getDishId() + " is present in menu");
        }
        Item created = itemRepository.save(new Item(menu, dish, itemTo.getPrice()));
        URI uriOFNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(ITEMS_URL + "{/id}")
                .buildAndExpand(restaurantId, menuId, created.getId().getDishId()).toUri();
        return ResponseEntity.created(uriOFNewResource).body(ItemUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void update(
            @PathVariable int restaurantId,
            @PathVariable int menuId,
            @PathVariable int id,
            @Valid @RequestBody ItemTo itemTo) {
        log.info("edit {}", itemTo);
        assureIdConsistent(itemTo, id);
        Item item = checkNotFoundWithId(itemRepository.findByDishIdAndMenuIdAndMenuRestaurantId(itemTo.getId(),
                menuId, restaurantId), "Item with id=" + itemTo.getId() + " not found");
        checkDateIsAfterTheCurrent(item.getMenu().getDate(), "Item id = " + itemTo.getId() + " cannot be updated.");
        updateFromTo(item, itemTo);
        itemRepository.save(item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("delete item {} from menu {}", id, menuId);
        Item item = checkNotFoundWithId(
                itemRepository.findByDishIdAndMenuIdAndMenuRestaurantId(id, menuId, restaurantId),
                "Item with id=" + id + " not found");
        checkDateIsAfterTheCurrent(item.getMenu().getDate(), "Item id = " + id + " cannot be deleted.");
        itemRepository.delete(item);
    }
}
