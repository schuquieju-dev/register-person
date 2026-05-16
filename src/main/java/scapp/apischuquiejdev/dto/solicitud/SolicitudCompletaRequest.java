package scapp.apischuquiejdev.dto.solicitud;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SolicitudCompletaRequest {
    private Long personaId;
    private Long entidadId;
    private Long productoId;
    private BigDecimal precioUnitarioPactado;
    private BigDecimal pesoSolicitado;
    private BigDecimal margenPermitidoPorcentaje;
    private String observaciones;
    private List<ViajeRequest> parcialidades;
}