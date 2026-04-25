package scapp.apischuquiejdev.interfaces.repository.transportista;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.transportista.EEstadoTransportista;

import java.util.Optional;


public interface IEstadoTransportistaRepository extends JpaRepository<EEstadoTransportista, Long> {
    Optional<EEstadoTransportista> findByCodigo(String codigo);
}