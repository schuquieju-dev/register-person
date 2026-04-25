package scapp.apischuquiejdev.dto.transportista;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaMiniResponse {

    private Long id;
    private String cui;
    private String nit;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
}
