package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.financial_transaction.FinancialTransaction;
import com.example.finmanagerbackend.limit.Limit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.CascadeType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing all financial tracking instruments, including user, financials, limits, etc.
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_generator")
    @SequenceGenerator(
            name = "account_id_generator",
            sequenceName = "account_id_seq",
            allocationSize = 50
    )
    private Long id;

    // Relationship mapping: One Account has many IncomeExpense records (Lazy loading for efficiency).
    // This avoids creating a new table for the relationship.
    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    // Cascade option is used to apply the same operation (e.g., save, update, delete) to the associated entities.
    private List<FinancialTransaction> operations = new ArrayList<>();

    // Relationship mapping: One Account has many Limit records (Lazy loading for efficiency).
    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Limit> limits = new ArrayList<>();

    @Setter
    private boolean isDemo;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return this.id != null && id.equals(((Account) o).getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }
}
