package scapp.apischuquiejdev.interfaces.repository.solicitud;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.solicitud.ESolicitudParcialidad;

import java.util.List;

public interface ISolicitudParcialidadRepository extends JpaRepository<ESolicitudParcialidad, Long> {


    List<ESolicitudParcialidad> findBySolicitudIdOrderByNumeroViajeAsc(Long solicitudId);
}