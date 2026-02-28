package com.matriz.backend.modules.finance.bankAccount.dto;

import com.matriz.backend.shared.enums.BankName;

import java.util.UUID;

public record BankAccountResDto(
        UUID id,
        String accountNumber,
        String cci,
        UUID holderId,
        BankName name
) {
}