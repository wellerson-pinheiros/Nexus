package nexus.com.br.game_store.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import nexus.com.br.game_store.domain.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${secretJWT}")
    String secret;

    public String gerarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
                return JWT.create()
                    // specify any specific claim validations
                    .withIssuer("Api Nexus")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(dataExpiracao())
                        .sign(algorithm);
                    // reusable verifier instance

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Erro ao gerar Token JWT", exception );
        }
    }

    // Método 2: Valida o Token nas próximas requisições e devolve o email do usuário
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("Api Nexus")
                    .build()
                    .verify(tokenJWT) // Verifica validade e se a assinatura está correta
                    .getSubject(); // Devolve o e-mail que guardamos lá no withSubject()

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
