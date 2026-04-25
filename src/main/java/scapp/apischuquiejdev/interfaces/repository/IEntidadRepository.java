package scapp.apischuquiejdev.interfaces.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scapp.apischuquiejdev.entity.EEntidad;

@Repository
public interface IEntidadRepository extends JpaRepository<EEntidad, Long> {
}

