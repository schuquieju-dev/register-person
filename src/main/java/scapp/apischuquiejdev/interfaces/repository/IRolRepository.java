package scapp.apischuquiejdev.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.ERol;

import java.util.Optional;

public interface IRolRepository extends JpaRepository<ERol, Long> {

    Optional<ERol> findByCodigo(String codigo);
}
