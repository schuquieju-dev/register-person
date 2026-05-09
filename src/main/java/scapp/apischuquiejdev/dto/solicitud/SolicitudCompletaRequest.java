package scapp.apischuquiejdev.dto.solicitud;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SolicitudCompletaRequest {

    private Long personaId;
    private Long entidadId;
    private String producto;
    private BigDecimal pesoSolicitado;
    private String observaciones;

    // Lista de viajes (Detalles)
    private List<ViajeRequest> parcialidades;
}