package scapp.apischuquiejdev.interfaces.services;

import scapp.apischuquiejdev.entity.solicitud.EProductoCatalogo;

import java.util.List;

public interface IProductoCatalogoService {
    List<EProductoCatalogo> obtenerTodosActivos();
    EProductoCatalogo obtenerPorId(Long id);
    EProductoCatalogo guardar(EProductoCatalogo producto);
}
