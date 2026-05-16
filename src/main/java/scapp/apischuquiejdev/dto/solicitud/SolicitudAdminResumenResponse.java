package scapp.apischuquiejdev.dto.solicitud;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudAdminResumenResponse {
    private Long id;
    private String clienteNombre;
    private String productoNombre;
    private BigDecimal pesoSolicitado;
    private BigDecimal pesoTotalRecibido;
    private BigDecimal porcentajeDesviacion;
    private String estado;
    private LocalDateTime fechaSolicitud;
}