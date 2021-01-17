package javvernaut.votingsystem.to;


import javvernaut.votingsystem.HasId;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class BaseTo implements HasId {
    protected Integer id;
}
