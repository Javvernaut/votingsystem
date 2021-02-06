package javvernaut.votingsystem.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemTo extends BaseTo {

    @NotNull
    @Range(min = 0)
    Integer dishId;

    @Size(min = 2, max = 100)
    String name;

    @NotNull
    @Range(min = 0)
    Integer price;

    public ItemTo(Integer id, Integer dishId, String name, Integer price) {
        super(id);
        this.dishId = dishId;
        this.name = name;
        this.price = price;
    }
}
