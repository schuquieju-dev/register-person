package scapp.apischuquiejdev.interfaces.repository.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.entity.persona.EPersonaRol;

public interface IPersonaRolRepository extends JpaRepository<EPersonaRol, EPersonaRol.PersonaRolId> {
    boolean existsById_PersonaIdAndId_RolId(Long personaId, Long rolId);

    @Modifying
    @Transactional
    @Query(value = """
        insert into persona_rol(persona_id, rol_id, asignado_por)
        values (:personaId, :rolId, :asignadoPor)
        on conflict (persona_id, rol_id) do nothing
        """, nativeQuery = true)
    void insertPersonaRol(@Param("personaId") Long personaId,
                          @Param("rolId") Long rolId,
                          @Param("asignadoPor") String asignadoPor);
}


