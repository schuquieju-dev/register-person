package scapp.apischuquiejdev.interfaces.repository.persona;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.persona.EPersona;

import java.util.Optional;

public interface IPersonaRepository extends JpaRepository<EPersona, Long> {

    boolean existsByCui(String cui);

    boolean existsByNit(String nit);

    boolean existsByEmail(String email);

    boolean existsByCuiAndIdNot(String cui, Long id);

    boolean existsByNitAndIdNot(String nit, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<EPersona> findByCui(String cui);

    Page<EPersona> findByCuiContainingIgnoreCaseOrNitContainingIgnoreCaseOrPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCase(
            String cui,
            String nit,
            String primerNombre,
            String primerApellido,
            Pageable pageable
    );
}
