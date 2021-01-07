package javvernaut.votingsystem.web;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @GetMapping("/dish")
    public Dish getDish() {
        return dishRepository.findByIdAndRestaurantId(100007, 100002).orElseThrow();
    }

    @GetMapping("/beans")
    public List<String> getBeans() {
        return Arrays.asList(applicationContext.getBeanDefinitionNames());
    }

    @DeleteMapping
    public void delete() {
        //menuRepository.deleteById(100017);
        dishRepository.deleteById(100009);
    }
}
