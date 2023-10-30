package med.voll.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.DatosActualizarMedico;
import med.voll.api.domain.medico.DatosListadoMedico;
import med.voll.api.domain.medico.DatosRegistroMedico;
import med.voll.api.domain.medico.DatosRespuestaMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
	
	@Autowired // buscar mejor manera de inyeccion de depencias con un setter.
	private MedicoRepository medicoRepository;
	
	@PostMapping
	public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
						UriComponentsBuilder uriComponentsBuilder) {
		Medico medico =medicoRepository.save(new Medico(datosRegistroMedico));
		//return 201 created
		//URL donde encontrar el medico -> GET http://localhost:8080/medicos/xx
		DatosRespuestaMedico datosRespuestaMedico = repuestaMedico(medico);
		
		URI url=  uriComponentsBuilder.path("medicos/{id}").buildAndExpand(medico.getId()).toUri();
		return ResponseEntity.created(url).body(datosRespuestaMedico);
	}
	
	@GetMapping
	public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size=10)  Pageable paginacion) {
//		return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
		return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
	}
	
	
	@PutMapping
	@Transactional
	public ResponseEntity<DatosRespuestaMedico> actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico) {
		//ResponseEntity devuelve la endidad actualizada -> por buenas practicas no debemos devolver el objeto de la  entidad
		// sino un DTO osea un record con los datos necesarios
		
		Medico medico =  medicoRepository.getReferenceById(datosActualizarMedico.id());
		medico.actualizarDatos(datosActualizarMedico);
		return ResponseEntity.ok(repuestaMedico(medico));
	}
	
//	@DeleteMapping("/{id}")0/	
//	@Transactional	
//	public void eliminarMedico(@PathVariable Long id) {
//		Medico medico =  medicoRepository.getReferenceById(id);
//		medicoRepository.delete(medico);
//		
//	}
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> eliminarMedico(@PathVariable Long id) {
		Medico medico =  medicoRepository.getReferenceById(id);
		medico.desactivarMedico();
		return ResponseEntity.noContent().build();
		// ResponseEntity retorna un 204 -> no contet
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id) {
		Medico medico =  medicoRepository.getReferenceById(id);
		return ResponseEntity.ok(repuestaMedico(medico));
		// ResponseEntity retorna un 204 -> no contet
	}
	
	
	private DatosRespuestaMedico repuestaMedico(Medico medico) {
		return new DatosRespuestaMedico(
				medico.getId(),
				medico.getNombre(),
				medico.getEmail(),
				medico.getTelefono(),
				medico.getEspecialidad().toString(),
					new DatosDireccion(
							medico.getDireccion().getCalle(),
							medico.getDireccion().getDistrito(),
							medico.getDireccion().getCiudad(),
							medico.getDireccion().getNumero(),
							medico.getDireccion().getComplemento()
						)
			);
	}
	
}
