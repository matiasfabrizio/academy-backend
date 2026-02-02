package com.matriz.backend.modules.finance.bankAccount.dto;

import com.matriz.backend.shared.BankName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BankAccountReqDto(
        @NotNull(message = "El número de cuenta es obligatorio")
        @Schema(example = "19112345678901", description = "Account number of the holder. Sent as a string to preserve precision.")
        String accountNumber,

        @NotNull(message = "El CCI es obligatorio")
        @Schema(example = "00219100123456789025", description = "CCI of the holder account. Sent as a string to preserve precision.")
        String cci,

        @NotNull(message = "El ID del titular es obligatorio")
        @Schema(description = "ID of the Holder that owns this account.")
        UUID holderId,

        @NotNull(message = "El nombre del banco es obligatorio")
        @Schema(example = "BCP", description = "Name of the bank.")
        BankName name
) {}