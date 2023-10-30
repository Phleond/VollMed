package med.voll.api.domain.paciente;

public record DatosListaPacientes(Long id, String nombre, String email, String documentoidentidad) {
	
    public DatosListaPacientes(Paciente paciente) {
        this(paciente.getId(),
        		paciente.getNombre(),
        		paciente.getEmail(),
        		paciente.getDocumentoidentidad()
        		);
    }
}