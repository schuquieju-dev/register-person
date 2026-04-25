package scapp.apischuquiejdev.mappers;

import org.springframework.stereotype.Component;
import scapp.apischuquiejdev.dto.transportista.PersonaMiniResponse;
import scapp.apischuquiejdev.dto.transportista.TransportistaResponse;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.entity.transportista.ETransportista;

import java.util.List;

@Component
public class TransportistaMapper {

    public TransportistaResponse toResponse(ETransportista entity) {
        if (entity == null) {
            return null;
        }

        TransportistaResponse res = new TransportistaResponse();
        res.setId(entity.getId());
        res.setLicenciaNumero(entity.getLicenciaNumero());
        res.setLicenciaTipo(entity.getLicenciaTipo());
        res.setLicenciaVencimiento(entity.getLicenciaVencimiento());
        res.setAprobado(entity.getAprobado());
        res.setActivo(entity.getActivo());

        if (entity.getEstadoTransportista() != null) {
            res.setEstado(entity.getEstadoTransportista().getCodigo());
        }

        EPersona persona = entity.getPersona();
        if (persona != null) {
            PersonaMiniResponse p = new PersonaMiniResponse();
            p.setId(persona.getId());
            p.setCui(persona.getCui());
            p.setNit(persona.getNit());
            p.setPrimerNombre(persona.getPrimerNombre());
            p.setSegundoNombre(persona.getSegundoNombre());
            p.setPrimerApellido(persona.getPrimerApellido());
            p.setSegundoApellido(persona.getSegundoApellido());
            res.setPersona(p);
        }

        return res;
    }

    public List<TransportistaResponse> toResponseList(List<ETransportista> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}