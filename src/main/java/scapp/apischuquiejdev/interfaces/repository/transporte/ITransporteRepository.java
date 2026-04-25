package scapp.apischuquiejdev.interfaces.repository.transporte;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.transporte.ETransporte;

public interface ITransporteRepository extends JpaRepository<ETransporte, Long> {
    boolean existsByPlaca(String placa);
    boolean existsByPlacaAndIdNot(String placa, Long id);
    boolean existsByPlacaIgnoreCase(String placa);
}
