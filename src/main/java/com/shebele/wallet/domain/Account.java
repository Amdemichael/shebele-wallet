package com.shebele.wallet.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String msisdn;

    @Column(unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, precision = 19, scale=2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Version
    private Long version;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @PreUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }

    public enum AccountStatus{
        ACTIVE,SUSPENDED,CLOSED,FROZEN
    }

}
