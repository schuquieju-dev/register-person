package scapp.apischuquiejdev.entity.persona;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Entity
@Table(
        name = "persona",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_persona_cui", columnNames = "cui"),
                @UniqueConstraint(name = "uq_persona_nit", columnNames = "nit"),
                @UniqueConstraint(name = "uq_persona_email", columnNames = "email")
        }
)
@Getter
@Setter
public class EPersona {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Pattern(regexp = "^[0-9]{13}$", message = "El CUI debe tener 13 dígitos.")
        @Column(name = "cui", nullable = false, length = 13)
        private String cui;

        @Pattern(regexp = "^$|^[0-9]+(-?[0-9kK])?$", message = "El NIT no tiene formato válido.")
        @Column(name = "nit", length = 20)
        private String nit;

        @Column(name = "primer_nombre", nullable = false, length = 80)
        private String primerNombre;

        @Column(name = "segundo_nombre", length = 80)
        private String segundoNombre;

        @Column(name = "primer_apellido", nullable = false, length = 80)
        private String primerApellido;

        @Column(name = "segundo_apellido", length = 80)
        private String segundoApellido;

        @Column(name = "telefono", length = 20)
        private String telefono;

        @Email(message = "El correo no es válido.")
        @Column(name = "email", length = 150)
        private String email;

        @Column(name = "activo", nullable = false)
        private Boolean activo;

        @Column(name = "created_at", nullable = false)
        private OffsetDateTime createdAt;

        @Column(name = "updated_at", nullable = false)
        private OffsetDateTime  updatedAt;

        @Column(name = "sexo", nullable = false)
        private String sexo;


        @Column(name = "direccion", nullable = false)
        private String direccion;

        @Column(name = "fecha_nacimiento", nullable = false)
        private LocalDate fechaNacimiento;


        @PrePersist
        public void prePersist() {
                OffsetDateTime now = OffsetDateTime.now();
                this.createdAt = now;
                this.updatedAt = now;

                if (this.activo == null) {
                        this.activo = true;
                }

                this.cui = trimToNull(this.cui);
                this.nit = trimToNull(this.nit);
                this.primerNombre = trimToNull(this.primerNombre);
                this.segundoNombre = trimToNull(this.segundoNombre);
                this.primerApellido = trimToNull(this.primerApellido);
                this.segundoApellido = trimToNull(this.segundoApellido);
                this.telefono = trimToNull(this.telefono);
                this.email = trimToNull(this.email);

        }

        @PreUpdate
        public void preUpdate() {
                this.updatedAt = OffsetDateTime.now();

                this.cui = trimToNull(this.cui);
                this.nit = trimToNull(this.nit);
                this.primerNombre = trimToNull(this.primerNombre);
                this.segundoNombre = trimToNull(this.segundoNombre);
                this.primerApellido = trimToNull(this.primerApellido);
                this.segundoApellido = trimToNull(this.segundoApellido);
                this.telefono = trimToNull(this.telefono);
                this.email = trimToNull(this.email);


        }

        private String trimToNull(String value) {
                if (value == null) {
                        return null;
                }
                String trimmed = value.trim();
                return trimmed.isEmpty() ? null : trimmed;
        }
}
