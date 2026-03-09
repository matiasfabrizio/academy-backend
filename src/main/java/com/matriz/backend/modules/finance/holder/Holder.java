package com.matriz.backend.modules.finance.holder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.matriz.backend.modules.courses.Course;
import com.matriz.backend.modules.finance.bankAccount.BankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.UUID;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Holder {

    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "holder")
    @JsonManagedReference("holder-bank-accounts")
    private Set<BankAccount> bankAccounts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "holder")
    @JsonManagedReference("holder-courses")
    private Set<Course> courses = new HashSet<>();
}
