package scapp.apischuquiejdev.interfaces.services.transporte;

import scapp.apischuquiejdev.dto.TransporteCreateRequest;
import scapp.apischuquiejdev.dto.TransporteResponse;

import java.util.List;


public interface ITransporteService {

    TransporteResponse crear(TransporteCreateRequest request);

    List<TransporteResponse> obtenerTodos();

    TransporteResponse obtenerPorId(Long id);
}
