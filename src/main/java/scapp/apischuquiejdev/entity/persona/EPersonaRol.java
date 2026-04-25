package scapp.apischuquiejdev.entity.persona;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "persona_rol", schema = "public")
@Getter
@Setter
public class EPersonaRol {


    @EmbeddedId
    private PersonaRolId id;

    @Column(name = "asignado_at", nullable = false)
    private OffsetDateTime asignadoAt;

    @Column(name = "asignado_por", length = 100)
    private String asignadoPor;

    @PrePersist
    public void prePersist() {
        if (this.asignadoAt == null) {
            this.asignadoAt = OffsetDateTime.now();
        }
    }

    @Embeddable
    @Getter
    @Setter
    public static class PersonaRolId implements Serializable {
        @Column(name = "persona_id", nullable = false)
        private Long personaId;

        @Column(name = "rol_id", nullable = false)
        private Long rolId;

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof PersonaRolId)) { return false; }
            PersonaRolId that = (PersonaRolId) o;
            return Objects.equals(personaId, that.personaId) && Objects.equals(rolId, that.rolId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(personaId, rolId);
        }
    }
}



