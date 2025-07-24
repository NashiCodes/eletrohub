package com.jp.eletrohub.model.repository;

import com.jp.eletrohub.model.entity.VendaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaItemRepository extends JpaRepository<VendaItem, Long> {
}
