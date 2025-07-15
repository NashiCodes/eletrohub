package com.jp.eletrohub.model.repository;

import com.jp.eletrohub.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
