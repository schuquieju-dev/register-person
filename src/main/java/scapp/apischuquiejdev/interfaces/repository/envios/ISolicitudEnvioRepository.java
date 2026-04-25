package scapp.apischuquiejdev.interfaces.repository.envios;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.solicitud.ESolicitudEnvio;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEnvioRepository extends JpaRepository<ESolicitudEnvio, Long> {

    List<ESolicitudEnvio> findBySolicitudIdOrderByNumeroEnvioAsc(Long solicitudId);

    Optional<ESolicitudEnvio> findTopBySolicitudIdOrderByNumeroEnvioDesc(Long solicitudId);
}
