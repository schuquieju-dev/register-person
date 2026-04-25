package scapp.apischuquiejdev.dto.envios;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
public class SolicitudEnvioCreateRequest {

    private Long solicitudId;
    private Long transportistaId;
    private Long transporteId;
    private LocalDateTime fechaSalida;
    private BigDecimal cantidadEnviada;
    private BigDecimal pesoEnviado;
    private String observacionesEnvio;
}
