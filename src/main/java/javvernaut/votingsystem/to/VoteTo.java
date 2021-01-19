package javvernaut.votingsystem.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    LocalDate date;
    String restaurantName;

    public VoteTo(Integer id, LocalDate date, String restaurantName) {
        this.id = id;
        this.date = date;
        this.restaurantName = restaurantName;
    }

}
