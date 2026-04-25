package scapp.apischuquiejdev.mappers;


import org.springframework.stereotype.Component;
import scapp.apischuquiejdev.dto.TransporteResponse;
import scapp.apischuquiejdev.dto.transportista.PersonaMiniResponse;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.entity.transporte.ETransporte;

import java.util.List;

@Component
public class TransporteMapper {

    public TransporteResponse toResponse(ETransporte entity) {
        if (entity == null) {
            return null;
        }

        TransporteResponse response = new TransporteResponse();
        response.setId(entity.getId());
        response.setPlaca(entity.getPlaca());
        response.setMarca(entity.getMarca());
        response.setModelo(entity.getModelo());
        response.setAnio(entity.getAnio());
        response.setColor(entity.getColor());
        response.setTipo(entity.getTipo());
        response.setCapacidadCargaKg(entity.getCapacidadCargaKg());
        response.setAprobado(entity.getAprobado());
        response.setActivo(entity.getActivo());

        if (entity.getEstadoTransporte() != null) {
            response.setEstado(entity.getEstadoTransporte().getCodigo());
        }

        EPersona propietario = entity.getPropietarioPersona();
        if (propietario != null) {
            PersonaMiniResponse propietarioMini = new PersonaMiniResponse();
            propietarioMini.setId(propietario.getId());
            propietarioMini.setCui(propietario.getCui());
            propietarioMini.setNit(propietario.getNit());
            propietarioMini.setPrimerNombre(propietario.getPrimerNombre());
            propietarioMini.setSegundoNombre(propietario.getSegundoNombre());
            propietarioMini.setPrimerApellido(propietario.getPrimerApellido());
            propietarioMini.setSegundoApellido(propietario.getSegundoApellido());
            response.setPropietario(propietarioMini);
        }

        return response;
    }

    public List<TransporteResponse> toResponseList(List<ETransporte> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}