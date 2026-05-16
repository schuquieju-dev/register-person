package scapp.apischuquiejdev.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.response.ApiResponseDto;
import scapp.apischuquiejdev.entity.solicitud.EProductoCatalogo;
import scapp.apischuquiejdev.interfaces.services.IProductoCatalogoService;

import java.util.List;


@RestController
@RequestMapping("/api/catalogos/productos")
public class ProductoCatalogoController {

    private final IProductoCatalogoService productoCatalogoService;

    // Constructor manual para inyección de dependencias
    public ProductoCatalogoController(IProductoCatalogoService productoCatalogoService) {
        this.productoCatalogoService = productoCatalogoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<EProductoCatalogo>>> findAll() {
        List<EProductoCatalogo> data = productoCatalogoService.obtenerTodosActivos();

        return ResponseEntity.ok(
                ApiResponseDto.success("Listado de productos obtenido correctamente.", data)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EProductoCatalogo>> findById(@PathVariable Long id) {
        EProductoCatalogo data = productoCatalogoService.obtenerPorId(id);

        return ResponseEntity.ok(
                ApiResponseDto.success("Producto obtenido correctamente.", data)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<EProductoCatalogo>> create(@Valid @RequestBody EProductoCatalogo producto) {
        EProductoCatalogo saved = productoCatalogoService.guardar(producto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Producto registrado en el catálogo.", saved));
    }

}