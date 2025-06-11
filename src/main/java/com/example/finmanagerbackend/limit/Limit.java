package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.account.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity class representing the table of limits that can be created by users.
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table( name = "Limits" )
@Entity
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "limit_id_generator")
    @SequenceGenerator(
            name = "limit_id_generator",
            sequenceName = "limit_id_seq",
            allocationSize = 50
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "account_id", nullable = false )
    @NotNull
    private Account account;

    @Enumerated( EnumType.STRING )
    @Column( nullable = false )
    @NotNull
    private LimitType limitType;

    @Column( nullable = false )
    @NotNull
    private BigDecimal limitAmount;

    private String category;
    private LocalDate creationDate;

    @Override
    public boolean equals(Object o) {

        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return id != null && id.equals(((Limit) o).getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Limit{" +
                "id=" + id +
                ", account=" + (account != null && account.getId() != null ? account.getId() : (account != null ? "transientAccount" : "null")) +
                ", limitType=" + limitType +
                ", limitAmount=" + limitAmount +
                ", category='" + category + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}




