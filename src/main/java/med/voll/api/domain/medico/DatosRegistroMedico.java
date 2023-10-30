package med.voll.api.domain.medico;



import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroMedico(
		@NotBlank 
		String nombre,
		
		@NotBlank
		@Email
		String email, 
		
		@NotBlank
		@Pattern(regexp = "\\d{4,6}", message = "Formato documento es inválido")
		String documento,
		
		@NotNull
		Especialidad especialidad,
		
		@NotBlank(message = "Teléfono es obligatorio")
		String telefono,
		
		@NotNull
		@Valid
		DatosDireccion direccion ) {

		

}
