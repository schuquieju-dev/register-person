package scapp.apischuquiejdev.interfaces.repository.solicitud;

import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.dto.solicitud.SolicitudCompletaRequest;

import java.util.List;

public interface ISolicitudesEnvioService {
    Long crearSolicitudConParcialidades(SolicitudCompletaRequest request);

    SolicitudEnvioResponse registrarRecepcion(Long parcialidadId, SolicitudEnvioRecepcionRequest request);

    List<SolicitudEnvioResponse> findBySolicitudId(Long solicitudId);



}
