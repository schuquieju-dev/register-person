package scapp.apischuquiejdev.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransporteCreateRequest {

    @NotNull
    private Long propietarioPersonaId;

    @NotBlank
    @Size(max = 20)
    private String placa;

    @NotBlank
    @Size(max = 60)
    private String marca;

    @Size(max = 60)
    private String modelo;

    private Integer anio;

    @Size(max = 40)
    private String color;

    @Size(max = 40)
    private String tipo;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal capacidadCargaKg;
}