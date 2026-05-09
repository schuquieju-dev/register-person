package scapp.apischuquiejdev.interfaces.repository.solicitud;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.solicitud.EEntidad;

import java.util.List;

public interface IEntidadRepository


        extends JpaRepository<EEntidad, Long> {


    List<EEntidad> findByEstadoOrderByNombreAsc(String estado);

}