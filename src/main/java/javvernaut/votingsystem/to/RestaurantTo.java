package javvernaut.votingsystem.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends BaseTo {
    String name;
    Long votes;

    public RestaurantTo(Integer id, String name, Long votes) {
        super(id);
        this.name = name;
        this.votes = votes;
    }
}
