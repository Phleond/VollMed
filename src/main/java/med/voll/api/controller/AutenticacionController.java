
package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import med.voll.api.domain.usuarios.DatosAutenticacionUsuario;
import med.voll.api.domain.usuarios.Usuario;
import med.voll.api.infra.security.DatosJWTToken;
import med.voll.api.infra.security.TokenService;

@RestController
@RequestMapping("/login")
public class AutenticacionController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<?> autenticarUsuario(@RequestBody @Validated DatosAutenticacionUsuario datosAutenticacionUsuario){
		Authentication authToken = new UsernamePasswordAuthenticationToken(
				datosAutenticacionUsuario.login(), 
				datosAutenticacionUsuario.clave()
				);
	
		Authentication usuarioAutenticado = authenticationManager.authenticate(authToken);
		String JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
		return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
		
	}
}
