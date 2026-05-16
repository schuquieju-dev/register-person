package scapp.apischuquiejdev.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scapp.apischuquiejdev.entity.solicitud.EProductoCatalogo;

import java.util.List;

public interface IProductoCatalogoRepository extends JpaRepository<EProductoCatalogo, Long> {
    List<EProductoCatalogo> findByEstadoActivoTrue();
}