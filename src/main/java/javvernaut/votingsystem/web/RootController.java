package javvernaut.votingsystem.web;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.jpa.DishJpaRepository;
import javvernaut.votingsystem.repository.jpa.MenuJpaRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/menus")
public class RootController {

    private final MenuJpaRepository menuJpaRepository;
    private final DishJpaRepository dishJpaRepository;
    private final ApplicationContext applicationContext;

    public RootController(MenuJpaRepository menuJpaRepository, DishJpaRepository dishJpaRepository, ApplicationContext applicationContext) {
        this.menuJpaRepository = menuJpaRepository;
        this.dishJpaRepository = dishJpaRepository;
        this.applicationContext = applicationContext;
    }

    @GetMapping
    public List<Menu> getMenus() {
        //menuRepository.save(new Menu(LocalDate.now()));
        return menuJpaRepository.findAll();
    }

    @GetMapping("/restaurants")
    public List<Menu> getMenusWithReataurants() {
        return menuJpaRepository.getWithRestaurants();
    }

    @GetMapping("/dish")
    public Dish getDish() {
        return dishJpaRepository.findByIdAndRestaurantId(100007, 100002).orElseThrow();
    }

    @GetMapping("/beans")
    public List<String> getBeans(){
        return Arrays.asList(applicationContext.getBeanDefinitionNames());
    }
}
