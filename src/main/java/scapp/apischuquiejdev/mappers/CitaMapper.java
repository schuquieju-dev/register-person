package scapp.apischuquiejdev.mappers;

import org.springframework.stereotype.Component;
import scapp.apischuquiejdev.dto.request.CitaCreateRequest;
import scapp.apischuquiejdev.dto.response.CitaResponse;
import scapp.apischuquiejdev.entity.ECita;

@Component
public class CitaMapper {

    public CitaResponse toResponse(ECita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getClienteId(),
                cita.getProfesionalId(),
                cita.getServicioId(),
                cita.getFechaHora(),
                cita.getEstado(),
                cita.getNotas()
        );
    }

    public ECita toEntity(CitaCreateRequest request) {
        ECita cita = new ECita();
        cita.setClienteId(request.clienteId());
        cita.setProfesionalId(request.profesionalId());
        cita.setServicioId(request.servicioId());
        cita.setFechaHora(request.fechaHora());
        cita.setEstado(request.estado());
        cita.setNotas(request.notas());
        return cita;
    }
}
