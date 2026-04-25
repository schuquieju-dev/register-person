package scapp.apischuquiejdev.dto.envios;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
@Builder
public class SolicitudResponse {

    private Long id;
    private Long personaId;
    private Long entidadId;
    private String producto;
    private BigDecimal cantidadSolicitada;
    private BigDecimal pesoSolicitado;
    private BigDecimal margenPermitidoPorcentaje;
    private BigDecimal pesoTotalEnviado;
    private BigDecimal pesoTotalRecibido;
    private BigDecimal pesoPendiente;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaSolicitud;
}
