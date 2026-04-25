package scapp.apischuquiejdev.interfaces.repository.transporte;


import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.transporte.EEstadoTransporte;

import java.util.Optional;

public interface IEstadoTransporteRepository extends JpaRepository<EEstadoTransporte, Long> {
    Optional<EEstadoTransporte> findByCodigo(String codigo);
}