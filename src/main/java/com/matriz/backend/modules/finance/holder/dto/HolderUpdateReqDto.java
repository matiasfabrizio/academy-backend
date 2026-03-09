package com.matriz.backend.modules.finance.holder.dto;

import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record HolderUpdateReqDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Schema(example = "Ruben Prado")
        String name,

        @NotNull(message = "El número de teléfono es obligatorio")
        @Schema(example = "996300605")
        String phoneNumber,

        @Schema(description = "New bank accounts list")
        Set<BankAccountReqDto> bankAccounts
) {}