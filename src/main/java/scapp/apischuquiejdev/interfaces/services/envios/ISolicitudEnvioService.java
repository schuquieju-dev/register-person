package scapp.apischuquiejdev.interfaces.services.envios;

import scapp.apischuquiejdev.dto.envios.SolicitudEnvioCreateRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;

import java.util.List;

public interface ISolicitudEnvioService {




    SolicitudEnvioResponse create(SolicitudEnvioCreateRequest request);

    SolicitudEnvioResponse registrarRecepcion(Long envioId, SolicitudEnvioRecepcionRequest request);

    List<SolicitudEnvioResponse> findBySolicitudId(Long solicitudId);
}
