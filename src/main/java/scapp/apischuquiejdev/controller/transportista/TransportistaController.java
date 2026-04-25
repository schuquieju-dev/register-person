package scapp.apischuquiejdev.controller.transportista;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.response.ApiResponseDto;
import scapp.apischuquiejdev.dto.transportista.TransportistaCreateRequest;
import scapp.apischuquiejdev.dto.transportista.TransportistaResponse;
import scapp.apischuquiejdev.interfaces.services.transportista.ITransportistaService;

import java.util.List;

@RestController
@RequestMapping("/api/transportistas")
public class TransportistaController {

    private final ITransportistaService transportistaService;

    public TransportistaController(ITransportistaService transportistaService) {
        this.transportistaService = transportistaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<TransportistaResponse>> crear(@Valid @RequestBody TransportistaCreateRequest request) {
        String asignadoPor = "sistema";
        TransportistaResponse saved = transportistaService.crearOActualizarTransportista(request, asignadoPor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Transportista registrado.", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<TransportistaResponse>>> obtenerTodos() {
        List<TransportistaResponse> data = transportistaService.obtenerTodos();

        return ResponseEntity.ok(
                ApiResponseDto.success("Listado de transportistas obtenido correctamente.", data)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TransportistaResponse>> obtenerPorId(@PathVariable Long id) {
        TransportistaResponse data = transportistaService.obtenerPorId(id);

        return ResponseEntity.ok(
                ApiResponseDto.success("Transportista obtenido correctamente.", data)
        );
    }
}