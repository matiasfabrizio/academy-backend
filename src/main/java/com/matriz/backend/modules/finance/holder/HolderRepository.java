package com.matriz.backend.modules.finance.holder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID> {
}