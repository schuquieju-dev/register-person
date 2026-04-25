package scapp.apischuquiejdev.mappers.persona;


import org.springframework.stereotype.Component;
import scapp.apischuquiejdev.dto.persona.PersonaCreateRequest;
import scapp.apischuquiejdev.dto.persona.PersonaResponse;
import scapp.apischuquiejdev.dto.persona.PersonaUpdateRequest;
import scapp.apischuquiejdev.entity.persona.EPersona;

@Component
public class PersonaMapper {

    public EPersona toEntity(PersonaCreateRequest request) {
        EPersona entity = new EPersona();
        entity.setCui(trimToNull(request.getCui()));
        entity.setNit(trimToNull(request.getNit()));
        entity.setPrimerNombre(trimToNull(request.getPrimerNombre()));
        entity.setSegundoNombre(trimToNull(request.getSegundoNombre()));
        entity.setPrimerApellido(trimToNull(request.getPrimerApellido()));
        entity.setSegundoApellido(trimToNull(request.getSegundoApellido()));
        entity.setTelefono(trimToNull(request.getTelefono()));
        entity.setEmail(trimToNull(request.getEmail()));
        entity.setActivo(true);
        entity.setFechaNacimiento(request.getFechaNacimiento());
        entity.setSexo(request.getSexo());
        entity.setDireccion(request.getDireccion());
        return entity;
    }

    public void updateEntity(EPersona entity, PersonaUpdateRequest request) {
        if (request.getCui() != null) {
            entity.setCui(trimToNull(request.getCui()));
        }
        entity.setNit(trimToNull(request.getNit()));
        entity.setPrimerNombre(trimToNull(request.getPrimerNombre()));
        entity.setSegundoNombre(trimToNull(request.getSegundoNombre()));
        entity.setPrimerApellido(trimToNull(request.getPrimerApellido()));
        entity.setSegundoApellido(trimToNull(request.getSegundoApellido()));
        entity.setTelefono(trimToNull(request.getTelefono()));
        entity.setEmail(trimToNull(request.getEmail()));
        entity.setSexo(request.getSexo());
        entity.setFechaNacimiento(request.getFechaNacimiento());
        entity.setDireccion(request.getDireccion());

        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
    }

    public PersonaResponse toResponse(EPersona entity) {
        PersonaResponse response = new PersonaResponse();
        response.setId(entity.getId());
        response.setCui(entity.getCui());
        response.setNit(entity.getNit());
        response.setPrimerNombre(entity.getPrimerNombre());
        response.setSegundoNombre(entity.getSegundoNombre());
        response.setPrimerApellido(entity.getPrimerApellido());
        response.setSegundoApellido(entity.getSegundoApellido());
        response.setTelefono(entity.getTelefono());
        response.setEmail(entity.getEmail());
        response.setActivo(entity.getActivo());
        response.setNombreCompleto(buildNombreCompleto(entity));
        response.setSexo(entity.getSexo());
        response.setFechaNacimiento(entity.getFechaNacimiento());
        response.setDireccion(entity.getDireccion());

        return response;
    }

    private String buildNombreCompleto(EPersona entity) {
        StringBuilder sb = new StringBuilder();

        if (entity.getPrimerNombre() != null) {
            sb.append(entity.getPrimerNombre());
        }
        if (entity.getSegundoNombre() != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(entity.getSegundoNombre());
        }


        return sb.toString();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}