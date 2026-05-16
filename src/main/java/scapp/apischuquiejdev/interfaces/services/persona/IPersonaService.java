package scapp.apischuquiejdev.interfaces.services.persona;

import org.springframework.data.domain.Page;
import scapp.apischuquiejdev.dto.persona.PersonaCreateRequest;
import scapp.apischuquiejdev.dto.persona.PersonaResponse;
import scapp.apischuquiejdev.dto.persona.PersonaUpdateRequest;

public interface IPersonaService {

    PersonaResponse crear(PersonaCreateRequest request);

    PersonaResponse obtenerPorId(Long id);

    Page<PersonaResponse> listar(int page, int size, String sort, String search);

    PersonaResponse actualizar(Long id, PersonaUpdateRequest request);
    PersonaResponse buscarPorEmail(String email);

}