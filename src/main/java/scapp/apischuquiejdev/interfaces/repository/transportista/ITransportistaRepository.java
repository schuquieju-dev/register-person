package scapp.apischuquiejdev.interfaces.repository.transportista;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.transportista.ETransportista;

import java.util.Optional;

public interface ITransportistaRepository extends JpaRepository<ETransportista, Long> {


    Optional<ETransportista> findByPersona_Id(Long personaId);
}
