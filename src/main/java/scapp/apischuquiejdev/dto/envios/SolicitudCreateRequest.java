package scapp.apischuquiejdev.dto.envios;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SolicitudCreateRequest {

    private Long personaId;
    private Long entidadId;
    private String producto;
    private BigDecimal cantidadSolicitada;
    private BigDecimal pesoSolicitado;
    private BigDecimal margenPermitidoPorcentaje;
    private String observaciones;
}
