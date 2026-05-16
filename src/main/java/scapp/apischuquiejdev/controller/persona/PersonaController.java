package scapp.apischuquiejdev.controller.persona;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.persona.PersonaCreateRequest;
import scapp.apischuquiejdev.dto.persona.PersonaResponse;
import scapp.apischuquiejdev.dto.persona.PersonaUpdateRequest;
import scapp.apischuquiejdev.dto.response.ApiResponseDto;
import scapp.apischuquiejdev.interfaces.services.persona.IPersonaService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final IPersonaService personaService;

    public PersonaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<PersonaResponse>> crear(
            @Valid @RequestBody PersonaCreateRequest request) {

        PersonaResponse response = personaService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Persona creada correctamente.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<PersonaResponse>> obtenerPorId(@PathVariable Long id) {
        PersonaResponse response = personaService.obtenerPorId(id);

        return ResponseEntity.ok(
                ApiResponseDto.success("Persona obtenida correctamente.", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<PersonaResponse>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            @RequestParam(required = false) String search) {

        Page<PersonaResponse> response = personaService.listar(page, size, sort, search);

        return ResponseEntity.ok(
                ApiResponseDto.success("Listado de personas.", response)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<PersonaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaUpdateRequest request) {

        PersonaResponse response = personaService.actualizar(id, request);

        return ResponseEntity.ok(
                ApiResponseDto.success("Persona actualizada correctamente.", response)
        );
    }

    // =========================================================================
    // NUEVO ENDPOINT: Requerido por el microservicio de apiauth
    // =========================================================================
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDto<PersonaResponse>> obtenerPorEmail(
            @RequestParam("email") String email) {

        PersonaResponse response = personaService.buscarPorEmail(email);

        return ResponseEntity.ok(
                ApiResponseDto.success("Persona localizada correctamente por email.", response)
        );
    }
}