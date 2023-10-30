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
import med.voll.api.domain.paciente.DatosActualizacionPaciente;
import med.voll.api.domain.paciente.DatosListaPacientes;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.DatosRespuestaPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

	@Autowired
	private PacienteRepository repository;

	@PostMapping
	public ResponseEntity<DatosRespuestaPaciente> registrar(@RequestBody @Valid DatosRegistroPaciente datosRegistro,
			UriComponentsBuilder uriComponentsBuilder) {

		Paciente paciente = repository.save(new Paciente(datosRegistro));
		URI url = UriComponentsBuilder.fromPath("pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
		return ResponseEntity.created(url).body(repuestaPaciente(paciente));
	}

	@GetMapping
	public ResponseEntity<Page<DatosListaPacientes>> listar(
			@PageableDefault(size = 10, sort = { "nombre" }) Pageable paginacion) {
		return ResponseEntity.ok(repository.findByActivoTrue(paginacion).map(DatosListaPacientes::new));

	}

	@PutMapping
	@Transactional
	public ResponseEntity<DatosRespuestaPaciente> actualizar(@RequestBody @Valid DatosActualizacionPaciente datos) {
		Paciente paciente = repository.getReferenceById(datos.id());
		paciente.atualizarInformacion(datos);
		return ResponseEntity.ok(repuestaPaciente(paciente));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Paciente paciente = repository.getReferenceById(id);
		paciente.inactivarPaciente();
		return ResponseEntity.noContent().build();
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<DatosRespuestaPaciente> pacientePorId(@PathVariable Long id) {
		Paciente paciente = repository.getReferenceById(id);
		return ResponseEntity.ok(repuestaPaciente(paciente));
	}

	private DatosRespuestaPaciente repuestaPaciente(Paciente paciente) {
		return new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
				paciente.getDocumentoidentidad(), paciente.getTelefono(),
				new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(),
						paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(),
						paciente.getDireccion().getComplemento()));
	}

}