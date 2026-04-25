package scapp.apischuquiejdev.interfaces.services.transportista;

import scapp.apischuquiejdev.dto.transportista.TransportistaCreateRequest;
import scapp.apischuquiejdev.dto.transportista.TransportistaResponse;
import scapp.apischuquiejdev.entity.transportista.ETransportista;

import java.util.List;

public interface ITransportistaService {



    TransportistaResponse crearOActualizarTransportista(TransportistaCreateRequest req, String asignadoPor);



    List<TransportistaResponse> obtenerTodos();

    TransportistaResponse obtenerPorId(Long id);

}
