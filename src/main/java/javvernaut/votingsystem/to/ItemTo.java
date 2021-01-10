package javvernaut.votingsystem.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemTo extends BaseTo {

    @Size(min = 2, max = 100)
    private String name;

    @Range(min = 0)
    private Integer price;

    public ItemTo(Integer id, String name, Integer price) {
        super(id);
        this.name = name;
        this.price = price;
    }
}
