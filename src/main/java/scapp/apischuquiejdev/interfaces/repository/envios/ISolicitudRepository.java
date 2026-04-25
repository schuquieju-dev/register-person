package scapp.apischuquiejdev.interfaces.repository.envios;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.solicitud.ESolicitud;

public interface ISolicitudRepository extends JpaRepository<ESolicitud, Long> {
}
