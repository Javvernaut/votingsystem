package javvernaut.votingsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Access(AccessType.FIELD)
@Table(name = "items", uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "items_unique_menu_dish_idx"))
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"menu", "dish"})
public class Item extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonBackReference
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "price", nullable = false)
    @Range(min = 0)
    private Integer price = 0;

    public Item(Integer id, Menu menu, Dish dish, Integer price) {
        super(id);
        this.menu = menu;
        this.dish = dish;
        this.price = price;
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
