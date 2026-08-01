package nexus.com.br.game_store.controller;

import nexus.com.br.game_store.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @GetMapping
    public ResponseEntity<String> testarAcesso() {
        // Puxando o usuário que o SecurityFilter injetou no contexto
        var usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Montando uma mensagem personalizada
        String mensagem = "Acesso liberado com sucesso! Bem-vindo, " + usuarioLogado.getEmail() +
                ". O seu token JWT está funcionando perfeitamente.";

        return ResponseEntity.ok(mensagem);
    }
}