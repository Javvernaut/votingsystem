package javvernaut.votingsystem.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Access(AccessType.FIELD)
@Table(name = "items")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Item {

    @EmbeddedId
    private ItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuId")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dishId")
    private Dish dish;

    @Column(name = "price", nullable = false)
    @Range(min = 0)
    private Integer price = 0;

    public Item(Menu menu, Dish dish, Integer price) {
        this.menu = menu;
        this.dish = dish;
        this.price = price;
        id = new ItemId(menu.getId(), dish.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        Item item = (Item) o;
        return id != null && id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : Objects.hash(id);
    }
}
