package scapp.apischuquiejdev.dto.envios;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Builder
public class SolicitudEnvioResponse {


    private Long id;
    private Long solicitudId;
    private Integer numeroEnvio;
    private Long transportistaId;
    private Long transporteId;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaRecepcion;
    private BigDecimal cantidadEnviada;
    private BigDecimal pesoEnviado;
    private BigDecimal cantidadRecibida;
    private BigDecimal pesoRecibido;
    private BigDecimal diferenciaPeso;
    private BigDecimal porcentajeDiferencia;
    private String estado;
    private String motivoRechazo;
    private String observacionesEnvio;
    private String observacionesRecepcion;
}
