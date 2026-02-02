package com.matriz.backend.modules.finance.bankAccount;

import com.matriz.backend.modules.finance.holder.HolderRepository;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountMapper;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountReqDto;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final HolderRepository holderRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountResDto createBankAccount(BankAccountReqDto reqDto) {
        BankAccount bankAccount = bankAccountMapper.toEntity(reqDto);
        bankAccount.setHolder(holderRepository.getReferenceById(reqDto.holderId()));
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDto(savedBankAccount);
    }

    public List<BankAccountResDto> getAllBankAccounts() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::toDto)
                .toList();
    }
}