package javvernaut.votingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "menus", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "menus_unique_date_restaurant_idx"))
public class Menu extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menu_dishes",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private List<Dish> dishes;
}
