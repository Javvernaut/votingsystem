package javvernaut.votingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "restaurant_id"}, name = "dishes_unique_name_restaurant_idx"))
public class Dish extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private Integer price;

    @Column(name = "active", nullable = false, columnDefinition = "bool default true")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menu_dishes",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private List<Menu> menus;
}
