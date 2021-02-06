package javvernaut.votingsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menus", uniqueConstraints = @UniqueConstraint(columnNames = {"menu_date", "restaurant_id"}, name = "menus_unique_date_restaurant_idx"))
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant", "items"})
public class Menu extends AbstractBaseEntity {

    @Column(name = "menu_date", nullable = false, columnDefinition = "date")
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<Item> items;

    public Menu(Integer id, LocalDate menuDate) {
        this.id = id;
        this.menuDate = menuDate;
    }

    public Menu(Menu menu) {
        this.id = id();
        this.menuDate = menu.getMenuDate();
    }
}
