package com.matriz.backend.modules.finance.bankAccount;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.matriz.backend.modules.finance.holder.Holder;
import com.matriz.backend.shared.BankName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BankName name;

    @Column(nullable = false)
    private long accountNumber;

    @Column(nullable = false)
    private long cci;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id", nullable = false)
    @JsonBackReference("holder-bank-accounts")
    private Holder holder;
}
