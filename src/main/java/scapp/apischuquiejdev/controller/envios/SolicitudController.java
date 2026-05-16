package scapp.apischuquiejdev.controller.envios;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.response.ApiResponseDto;
import scapp.apischuquiejdev.dto.solicitud.*;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.entity.solicitud.ValidacionGaritaResponse;
import scapp.apischuquiejdev.interfaces.repository.solicitud.ISolicitudesEnvioService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final ISolicitudesEnvioService solicitudesEnvioService;

    // 1. CREACIÓN (Agricultor)
    @PostMapping
    public ResponseEntity<ApiResponseDto<SolicitudCreadaDto>> create(@RequestBody SolicitudCompletaRequest request) {
        SolicitudCreadaDto data = solicitudesEnvioService.crearSolicitudConParcialidades(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Solicitud creada exitosamente.", data));
    }

    // 2. RECEPCIÓN BÁSCULA (Operador de Báscula)
    @PutMapping("/parcialidad/{parcialidadId}/recepcion")
    public ResponseEntity<ApiResponseDto<SolicitudEnvioResponse>> registrarRecepcion(
            @PathVariable Long parcialidadId,
            @RequestBody SolicitudEnvioRecepcionRequest request
    ) {
        SolicitudEnvioResponse response = solicitudesEnvioService.registrarRecepcion(parcialidadId, request);
        return ResponseEntity.ok(
                ApiResponseDto.success("Recepción de parcialidad registrada en báscula correctamente.", response)
        );
    }

    // 3. OBTENER PARCIALIDADES DE UNA SOLICITUD
    @GetMapping("/{solicitudId}/parcialidades")
    public ResponseEntity<ApiResponseDto<List<SolicitudEnvioResponse>>> findParcialidadesBySolicitudId(
            @PathVariable Long solicitudId
    ) {
        List<SolicitudEnvioResponse> data = solicitudesEnvioService.findBySolicitudId(solicitudId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Listado de parcialidades obtenido con éxito.", data)
        );
    }

    // 4. VISTA LIGERA PARA TABLA DEL ADMINISTRADOR
    @GetMapping("/admin/resumen")
    public ResponseEntity<ApiResponseDto<List<SolicitudAdminResumenResponse>>> getResumen() {
        return ResponseEntity.ok(
                ApiResponseDto.success("Resumen obtenido exitosamente.", solicitudesEnvioService.obtenerResumenParaAdmin())
        );
    }

    // 5. VISTA DETALLE COMPLETO (Al presionar "Revisar Detalles")
    @GetMapping("/admin/{solicitudId}/detalle")
    public ResponseEntity<ApiResponseDto<SolicitudAdminDetalleResponse>> getDetalle(@PathVariable Long solicitudId) {
        return ResponseEntity.ok(
                ApiResponseDto.success("Detalle completo obtenido exitosamente.", solicitudesEnvioService.obtenerDetalleAdmin(solicitudId))
        );
    }

    // 6. INGRESO A GARITA (Guardia)
    @PatchMapping("/parcialidad/{parcialidadId}/garita")
    public ResponseEntity<ApiResponseDto<SolicitudEnvioResponse>> registrarGarita(
            @PathVariable Long parcialidadId,
            @RequestBody GaritaRequest request
    ) {
        SolicitudEnvioResponse data = solicitudesEnvioService.registrarIngresoGarita(parcialidadId, request);
        String mensaje = request.isAprobado() ? "Ingreso a garita aprobado." : "Ingreso a garita RECHAZADO.";

        return ResponseEntity.ok(ApiResponseDto.success(mensaje, data));
    }

    // 7. ACTUALIZACIÓN MANUAL DE ESTADO (Admin)
    @PatchMapping("/{solicitudId}/estado")
    public ResponseEntity<ApiResponseDto<SolicitudAdminDetalleResponse>> actualizarEstado(
            @PathVariable Long solicitudId,
            @RequestBody UpdateEstadoRequest request
    ) {
        SolicitudAdminDetalleResponse data = solicitudesEnvioService.actualizarEstado(solicitudId, request);
        String mensajeExito = String.format("La solicitud #%d ha sido actualizada al estado: %s",
                solicitudId, request.getNuevoEstado());

        return ResponseEntity.ok(ApiResponseDto.success(mensajeExito, data));
    }

    // 8. VALIDACIÓN EN GARITA (Guardia - GET)
    @GetMapping("/{solicitudId}/parcialidad/{parcialidadId}/validar-garita")
    public ResponseEntity<ApiResponseDto<ValidacionGaritaResponse>> validarGarita(
            @PathVariable Long solicitudId,
            @PathVariable Long parcialidadId
    ) {
        ValidacionGaritaResponse data = solicitudesEnvioService.obtenerDetalleParaValidacion(solicitudId, parcialidadId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Información de validación obtenida correctamente.", data)
        );
    }

    // 9. VALIDACIÓN EN BÁSCULA (Operador Báscula - GET)
    @GetMapping("/{solicitudId}/parcialidad/{parcialidadId}/validar-bascula")
    public ResponseEntity<ApiResponseDto<ValidacionBasculaResponse>> validarBascula(
            @PathVariable Long solicitudId,
            @PathVariable Long parcialidadId
    ) {
        ValidacionBasculaResponse data = solicitudesEnvioService.obtenerDetalleParaBascula(solicitudId, parcialidadId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Datos de pesaje listos para procesar.", data)
        );
    }

    // 10. CIERRE ADMINISTRATIVO / LIQUIDACIÓN (Admin)
    @PostMapping("/{solicitudId}/liquidar")
    public ResponseEntity<ApiResponseDto<SolicitudAdminDetalleResponse>> liquidar(@PathVariable Long solicitudId) {
        SolicitudAdminDetalleResponse data = solicitudesEnvioService.liquidarSolicitud(solicitudId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Solicitud liquidada y cerrada exitosamente.", data)
        );
    }
}