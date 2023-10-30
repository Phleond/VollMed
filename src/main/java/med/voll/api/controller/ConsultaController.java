package med.voll.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsultas;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
	
	
	private AgendaDeConsultaService service;

	@PostMapping
	@Transactional
	public ResponseEntity<DatosDetalleConsultas> agendar(@RequestBody @Valid DatosAgendarConsulta datos) {
		service.agendar(datos);
		return ResponseEntity.ok(new DatosDetalleConsultas(null, null, null, null));
	}
}
