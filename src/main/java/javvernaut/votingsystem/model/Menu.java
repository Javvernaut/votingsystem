package javvernaut.votingsystem.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menus", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "menus_unique_date_restaurant_idx"))
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant", "items"})
public class Menu extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Item> items;

    public void addItem(Dish dish, int price) {
        Item item = new Item(this, dish, price);
        dish.getItems().add(item);
    }
}
