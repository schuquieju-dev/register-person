package scapp.apischuquiejdev.interfaces.services.envios;

import scapp.apischuquiejdev.dto.envios.SolicitudCreateRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudResponse;

import java.util.List;

public interface ISolicitudService {



    SolicitudResponse create(SolicitudCreateRequest request);

    SolicitudResponse findById(Long id);

    List<SolicitudResponse> findAll();
}
