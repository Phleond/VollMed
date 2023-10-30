package med.voll.api.domain.medico;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

	Page<Medico> findByActivoTrue(Pageable paginacion);
	
	@Query(value = """
            select m form Medico m
            where m.activo = true and m.especialidad=:especialidad and m.id 
            not in(select c.medico.id from Consulta c where c.fecha=:fecha)
            order by rand()
            limit 1
            """, nativeQuery = true)
	
	Medico seleccionarMedicoEspecialidadEnFecha(Especialidad especialidad, LocalDateTime fecha);

}
