package com.matriz.backend.modules.finance.holder;

import com.matriz.backend.modules.exceptions.HolderNotFoundException;
import com.matriz.backend.modules.finance.bankAccount.BankAccount;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountMapper;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountReqDto;
import com.matriz.backend.modules.finance.holder.dto.HolderMapper;
import com.matriz.backend.modules.finance.holder.dto.HolderReqDto;
import com.matriz.backend.modules.finance.holder.dto.HolderResDto;
import com.matriz.backend.modules.finance.holder.dto.HolderUpdateReqDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HolderService {

    private final HolderRepository holderRepository;
    private final HolderMapper holderMapper;
    private final BankAccountMapper bankAccountMapper;

    public HolderResDto createHolder(HolderReqDto reqDto) {
        Holder holder = holderMapper.toEntity(reqDto);
        return holderMapper.toDto(holderRepository.save(holder));
    }

    public List<HolderResDto> getAllHolders() {
        return holderRepository.findAll().stream()
                .map(holderMapper::toDto)
                .toList();
    }

    public HolderResDto getHolderById(UUID holderId) {
        return holderRepository.findById(holderId)
                .map(holderMapper::toDto)
                .orElseThrow(() -> new HolderNotFoundException("Holder not found with id: " + holderId));
    }

    public HolderResDto updateHolderById(HolderUpdateReqDto reqDto, UUID id) {
        Holder holderToUpdate = holderRepository.findById(id)
                .orElseThrow(() -> new HolderNotFoundException("Holder not found with id: " + id));

        holderToUpdate.setName(reqDto.name());
        holderToUpdate.setPhoneNumber(reqDto.phoneNumber());

        if (reqDto.bankAccounts() != null) {
            holderToUpdate.getBankAccounts().clear();

            reqDto.bankAccounts().forEach(bankAccountDto -> {
                BankAccount newBankAccount = bankAccountMapper.toEntity(bankAccountDto);
                newBankAccount.setHolder(holderToUpdate);
                holderToUpdate.getBankAccounts().add(newBankAccount);
            });
        }

        return holderMapper.toDto(holderRepository.save(holderToUpdate));
    }

    public void deleteHolderbyId(UUID id) {
        holderRepository.deleteById(id);
    }
}