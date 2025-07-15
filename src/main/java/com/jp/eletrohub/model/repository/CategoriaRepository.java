package com.jp.eletrohub.model.repository;

import com.jp.eletrohub.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
