package scapp.apischuquiejdev.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.entity.solicitud.EProductoCatalogo;
import scapp.apischuquiejdev.interfaces.repository.IProductoCatalogoRepository;
import scapp.apischuquiejdev.interfaces.services.IProductoCatalogoService;

import java.util.List;


@Service
public class ProductoCatalogoService implements IProductoCatalogoService {


    private final IProductoCatalogoRepository productoRepository;

    // Constructor manual según tu ejemplo de TransportistaService
    public ProductoCatalogoService(IProductoCatalogoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EProductoCatalogo> obtenerTodosActivos() {
        return productoRepository.findByEstadoActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public EProductoCatalogo obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el producto con id: " + id));
    }

    @Override
    @Transactional
    public EProductoCatalogo guardar(EProductoCatalogo producto) {
        // Aseguramos estado activo por defecto si viene nulo
        if (producto.getEstadoActivo() == null) {
            producto.setEstadoActivo(true);
        }
        return productoRepository.save(producto);
    }
}