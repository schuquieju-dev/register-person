package scapp.apischuquiejdev.controller.transporte;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.TransporteCreateRequest;
import scapp.apischuquiejdev.dto.TransporteResponse;
import scapp.apischuquiejdev.dto.response.ApiResponseDto;
import scapp.apischuquiejdev.interfaces.services.transporte.ITransporteService;

import java.util.List;

@RestController
@RequestMapping("/api/transportes")
public class TransporteController {

    private final ITransporteService transporteService;

    public TransporteController(ITransporteService transporteService) {
        this.transporteService = transporteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<TransporteResponse>> crear(@Valid @RequestBody TransporteCreateRequest request) {
        TransporteResponse data = transporteService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Transporte registrado.", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<TransporteResponse>>> obtenerTodos() {
        List<TransporteResponse> data = transporteService.obtenerTodos();

        return ResponseEntity.ok(
                ApiResponseDto.success("Listado de transportes obtenido correctamente.", data)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TransporteResponse>> obtenerPorId(@PathVariable Long id) {
        TransporteResponse data = transporteService.obtenerPorId(id);

        return ResponseEntity.ok(
                ApiResponseDto.success( "Transporte obtenido correctamente.", data)
        );
    }
}