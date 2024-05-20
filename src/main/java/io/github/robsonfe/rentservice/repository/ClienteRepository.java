package io.github.robsonfe.rentservice.repository;

import io.github.robsonfe.rentservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
