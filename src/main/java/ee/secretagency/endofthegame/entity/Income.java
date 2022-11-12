package ee.secretagency.endofthegame.entity;

import ee.secretagency.endofthegame.entity.enumeration.IncomeCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "INCOMES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    IncomeCategory category;
    String currency;
    BigDecimal amount;
    String person;
    ZonedDateTime timestamp;
    ZonedDateTime creationTimestamp;
    ZonedDateTime updateTimestamp;

    @PrePersist
    private void initCreationTimestamp() {
        creationTimestamp = ZonedDateTime.now();
    }

    @PreUpdate
    private void setCurrentUpdateTimestamp() {
        updateTimestamp = ZonedDateTime.now();
    }
}