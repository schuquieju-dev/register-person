package scapp.apischuquiejdev.dto;




import lombok.Data;
import scapp.apischuquiejdev.dto.transportista.PersonaMiniResponse;

import java.math.BigDecimal;

@Data
public class TransporteResponse {

    private Long id;
    private PersonaMiniResponse propietario;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private String tipo;
    private BigDecimal capacidadCargaKg;
    private String estado;
    private Boolean aprobado;
    private Boolean activo;
}