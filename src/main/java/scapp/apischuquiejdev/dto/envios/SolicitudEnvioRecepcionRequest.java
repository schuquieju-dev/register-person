package scapp.apischuquiejdev.dto.envios;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class SolicitudEnvioRecepcionRequest {

    private LocalDateTime fechaRecepcion;
    private BigDecimal pesoRecibido;
    private String observacionesRecepcion;
}
