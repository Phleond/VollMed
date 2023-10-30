package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;

@Service
public class AgendaDeConsultaService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private MedicoRepository medicoRepository;

	public void agendar(DatosAgendarConsulta datos) {

		if(pacienteRepository.findById(datos.idPaciente()).isPresent()) {
			throw new ValidacionDeIntegridad("Es id del paciente no fue encontrado");
			
		}
		if(datos.idMedico() != null && medicoRepository.existsById(datos.idMedico())) {
			throw new ValidacionDeIntegridad("Es id del medico no fue encontrado");
		}
		
		
		Paciente paciente = pacienteRepository.findById(datos.idPaciente()).get();
		Medico medico = seleccionarMedico(datos);

		Consulta consulta = new Consulta(null, medico, paciente, datos.fecha());

		consultaRepository.save(consulta);

	}

	private Medico seleccionarMedico(DatosAgendarConsulta datos) {
		
		if(datos.idMedico() != null) {
			return medicoRepository.getReferenceById(datos.idMedico());
		}
		if(datos.especialidad()==null) {
			throw new ValidacionDeIntegridad("debe seleccionarse una especialidad para el medico");
		}
		return medicoRepository.seleccionarMedicoEspecialidadEnFecha(datos.especialidad(), datos.fecha());
	}
}
