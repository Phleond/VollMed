package med.voll.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;


@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var authHeader = request.getHeader("Authorization");
	
		if (authHeader != null) {
			String token = authHeader.replace("Bearer ", "");
			System.out.println(tokenService.getSubject(token)); // este usuario tiene sesion?
			String nombreUsuario = tokenService.getSubject(token);
	
			if (nombreUsuario != null) {
				//el token es valido -
				UserDetails usuario = usuarioRepository.findByLogin(nombreUsuario);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						usuario, 
						null,
						usuario.getAuthorities());// forzamos el inicio de session
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}
	
}
