package scapp.apischuquiejdev.interfaces.repository.solicitud;

import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.dto.solicitud.*;
import scapp.apischuquiejdev.entity.solicitud.EstadoSolicitud;
import scapp.apischuquiejdev.entity.solicitud.ValidacionGaritaResponse;

import java.util.List;

public interface ISolicitudesEnvioService {
    // En ISolicitudesEnvioService.java
    SolicitudCreadaDto crearSolicitudConParcialidades(SolicitudCompletaRequest request);
    SolicitudEnvioResponse registrarRecepcion(Long parcialidadId, SolicitudEnvioRecepcionRequest request);

    List<SolicitudEnvioResponse> findBySolicitudId(Long solicitudId);

    SolicitudAdminDetalleResponse obtenerDetalleAdmin(Long solicitudId);




    SolicitudAdminDetalleResponse actualizarEstado(Long solicitudId, UpdateEstadoRequest request);


    SolicitudEnvioResponse registrarIngresoGarita(Long parcialidadId, GaritaRequest request);


    // Agregamos este método a la interfaz
    ValidacionGaritaResponse obtenerDetalleParaValidacion(Long solicitudId, Long parcialidadId);


    ValidacionBasculaResponse obtenerDetalleParaBascula(Long solicitudId, Long parcialidadId);

    SolicitudAdminDetalleResponse liquidarSolicitud (Long solicitudId);

    List<SolicitudAdminResumenResponse> obtenerResumenParaAdmin();
}
