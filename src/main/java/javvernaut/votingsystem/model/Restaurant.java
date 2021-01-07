package javvernaut.votingsystem.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Setter
@Getter
@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false, unique = true))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "active", nullable = false, columnDefinition = "bool default true")
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Dish> dishes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Vote> votes;
}
