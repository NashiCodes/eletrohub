package com.jp.eletrohub.model.repository;

import com.jp.eletrohub.model.entity.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
}
