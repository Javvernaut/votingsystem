package javvernaut.votingsystem.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "user_id"}, name = "votes_unique_date_userId_idx"))
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
