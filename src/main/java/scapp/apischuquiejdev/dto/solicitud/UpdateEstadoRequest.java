package scapp.apischuquiejdev.dto.solicitud;


import lombok.Data;
import scapp.apischuquiejdev.entity.solicitud.EstadoSolicitud;

@Data
public class UpdateEstadoRequest {
    private EstadoSolicitud nuevoEstado;
    private String observacionAdmin; // Opcional, por si el admin quiere dejar una nota
}