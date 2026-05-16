package scapp.apischuquiejdev.dto.persona;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
public class PersonaResponse {

    private Long id;
    private String cui;
    private String nit;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private String email;
    private String sexo;
    private String direccion;

    private LocalDate fechaNacimiento;

    private Boolean activo;
    private String nombreCompleto;
}