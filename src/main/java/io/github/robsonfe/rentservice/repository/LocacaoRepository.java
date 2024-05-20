package io.github.robsonfe.rentservice.repository;

import io.github.robsonfe.rentservice.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocacaoRepository extends JpaRepository<Locacao,Long> {

}
