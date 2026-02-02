package com.matriz.backend.modules.finance.bankAccount.dto;

import com.matriz.backend.modules.finance.bankAccount.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "holder", ignore = true)
    BankAccount toEntity(BankAccountReqDto dto);

    @Mapping(source = "holder.id", target = "holderId")
    BankAccountResDto toDto(BankAccount entity);
}