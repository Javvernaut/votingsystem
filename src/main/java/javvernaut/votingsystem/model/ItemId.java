package javvernaut.votingsystem.model;

import lombok.*;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "dish_id")
    private Integer dishId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        ItemId itemId = (ItemId) o;
        return menuId.equals(itemId.menuId) && dishId.equals(itemId.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, dishId);
    }
}
