package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import med.voll.api.domain.usuarios.Usuario;

@Service
public class TokenService {
	@Value("${api.security.secret}")
	private String apiSecret;

	public String generarToken(Usuario usuario) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(apiSecret);
			String token = JWT.create().withIssuer("voll med").withSubject(usuario.getLogin()).withClaim("id", usuario.getId())
					.withExpiresAt(generarFechaExpiracion(2)).sign(algorithm);
			System.out.println("TOKEN GENERADO   :"+token);
			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException();
		}
		
	}

	public String getSubject(String token) {
		System.out.println("TOKEN RECIBIDO   :" + token);
		DecodedJWT verifier = null;
		try {
			
			Algorithm algorithm = Algorithm.HMAC256(apiSecret);
			verifier = JWT.require(algorithm)
					.withIssuer("voll med")
					.build()
					.verify(token);
			System.out.println("verifier  :" + verifier);
			verifier.getSubject();
			System.out.println(token);
		} catch (JWTVerificationException exception) {
			System.out.println("COMO E' POSOSIBLE   :" + verifier.getSubject());
		}
		if (verifier.getSubject() == null || verifier == null ) {
			throw new RuntimeException("El Verifier es inválido!");
		}
		return verifier.getSubject();
	}

	private Instant generarFechaExpiracion(int horas) {
		return LocalDateTime.now().plusHours(horas).toInstant(ZoneOffset.of("-05:00"));
	}

}
