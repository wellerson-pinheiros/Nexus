package nexus.com.br.game_store.controller;

import jakarta.validation.Valid;
import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.dto.UsuarioLoginDTO;
import nexus.com.br.game_store.dto.UsuarioLoginDTOResposta;
import nexus.com.br.game_store.service.TokenService;
import nexus.com.br.game_store.service.UsuarioLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/login")
public class UsuarioLoginController {

    @Autowired
    UsuarioLoginService usuarioLoginService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
  public ResponseEntity<UsuarioLoginDTOResposta> login (@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO ) {

        // 1. Cria um token temporário com o email e a senha que vieram do Postman
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuarioLoginDTO.email(),usuarioLoginDTO.senha());

        // 2. Manda o gerente validar.
        // É AQUI que o Spring chama o BCrypt, o UsuarioLoginService e testa a senha!
       Authentication authentication = manager.authenticate(authenticationToken);

       Usuario usuario = (Usuario) authentication.getPrincipal();

        var TokenJWT = tokenService.gerarToken(usuario);

        return ResponseEntity.ok().body(new UsuarioLoginDTOResposta(usuario.getUsuarioID(),usuario.getEmail(),usuario.getFotoPerfil(),TokenJWT));
  }
}
