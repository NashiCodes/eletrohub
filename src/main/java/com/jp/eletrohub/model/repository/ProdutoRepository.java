package com.jp.eletrohub.model.repository;

import com.jp.eletrohub.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
