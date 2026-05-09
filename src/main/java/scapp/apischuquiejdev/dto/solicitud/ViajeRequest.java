package scapp.apischuquiejdev.dto.solicitud;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ViajeRequest {
    private Long transporteId;
    private Long transportistaId;
    private BigDecimal pesoEnviado;
    private LocalDateTime fechaSalida;
}