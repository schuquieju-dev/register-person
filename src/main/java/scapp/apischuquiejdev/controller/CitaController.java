package scapp.apischuquiejdev.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.request.CitaCreateRequest;
import scapp.apischuquiejdev.dto.response.ApiResponseDto;
import scapp.apischuquiejdev.dto.response.CitaResponse;
import scapp.apischuquiejdev.interfaces.services.ICitaService;

import java.util.List;


@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final ICitaService citaService;

    public CitaController(ICitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CitaResponse>>> listar() {
        List<CitaResponse> data = citaService.listar();
        return ResponseEntity.ok(
                ApiResponseDto.success("Listado de citas.", data)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<CitaResponse>> crear(@Valid @RequestBody CitaCreateRequest request) {
        CitaResponse created = citaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Cita creada correctamente.", created));
    }

}
