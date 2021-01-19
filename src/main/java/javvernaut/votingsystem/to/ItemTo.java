package javvernaut.votingsystem.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
public class ItemTo extends BaseTo {

    @Size(min = 2, max = 100)
    String name;

    @Range(min = 0)
    Integer price;

    public ItemTo(Integer id, String name, Integer price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @Override
    @NotNull
    public Integer getId() {
        return id;
    }
}
