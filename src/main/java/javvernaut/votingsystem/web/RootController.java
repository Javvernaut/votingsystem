package javvernaut.votingsystem.web;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.jpa.DishRepository;
import javvernaut.votingsystem.repository.jpa.MenuRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/menus")
public class RootController {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final ApplicationContext applicationContext;

    public RootController(MenuRepository menuRepository, DishRepository dishRepository, ApplicationContext applicationContext) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.applicationContext = applicationContext;
    }

    @GetMapping
    public List<Menu> getMenus() {
        //menuRepository.save(new Menu(LocalDate.now()));
        return menuRepository.findAll();
    }

    @GetMapping("/restaurants")
    public List<Menu> getMenusWithReataurants() {
        return menuRepository.getWithRestaurants();
    }

    @GetMapping("/dish")
    public Dish getDish() {
        return dishRepository.findByIdAndRestaurantId(100007, 100002).orElseThrow();
    }

    @GetMapping("/beans")
    public List<String> getBeans() {
        return Arrays.asList(applicationContext.getBeanDefinitionNames());
    }
}
