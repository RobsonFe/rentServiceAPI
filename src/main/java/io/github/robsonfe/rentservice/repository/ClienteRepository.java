package io.github.robsonfe.rentservice.repository;

import io.github.robsonfe.rentservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByName(String name);
}
