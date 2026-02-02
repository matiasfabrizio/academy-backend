package com.matriz.backend.modules.finance.bankAccount;

import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountReqDto;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-accounts")
@RequiredArgsConstructor
@Tag(name = "Bank Accounts", description = "Operations to manage bank accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    @Operation(summary = "Create a new bank account and assign it to a holder", description = "Create a new bank account with the provided information. Returns the created bank account.")
    public ResponseEntity<BankAccountResDto> create(@RequestBody @Valid BankAccountReqDto reqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountService.createBankAccount(reqDto));
    }

    @GetMapping
    @Operation(summary = "List all bank accounts", description = "Returns a list of all bank accounts.")
    public ResponseEntity<List<BankAccountResDto>> getAll() {
        return ResponseEntity.ok(bankAccountService.getAllBankAccounts());
    }
}