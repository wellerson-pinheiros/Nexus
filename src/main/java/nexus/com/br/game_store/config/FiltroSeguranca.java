package nexus.com.br.game_store.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nexus.com.br.game_store.excecoes.ResourceNotFoundException;
import nexus.com.br.game_store.excecoes.TokenJwtException;
import nexus.com.br.game_store.repository.UsuarioRepository;
import nexus.com.br.game_store.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroSeguranca extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            var tokenJWT = recuperarToken(request);

            if (tokenJWT != null) {
                var subject = tokenService.getSubject(tokenJWT);

                // 3. Busca o usuário no banco de dados para confirmar que ele ainda existe
                var usuario = usuarioRepository.findByEmail(subject).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

                // 4. Cria o objeto de autenticação e injeta no contexto do Spring
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 5. Libera a requisição para seguir o fluxo (ir para o Controller)
            filterChain.doFilter(request, response);

    } catch (Exception e) {
        // Intercepta a exceção do token e força um retorno HTTP 401 limpo em vez de deixar virar 500
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\", \"status\": 401}");
    }

    }

    // Método auxiliar para limpar a palavra "Bearer " e pegar só o código do Token
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}
