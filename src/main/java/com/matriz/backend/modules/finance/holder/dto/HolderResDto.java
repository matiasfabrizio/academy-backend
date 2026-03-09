package com.matriz.backend.modules.finance.holder.dto;

import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountResDto;

import java.util.Set;
import java.util.UUID;

public record HolderResDto(
        UUID id,
        String name,
        String phoneNumber,
        Set<BankAccountResDto> bankAccounts,
        Set<UUID> coursesIds
) {}