package nexus.com.br.game_store.controller;

import nexus.com.br.game_store.domain.Jogo;
import nexus.com.br.game_store.service.JogosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/jogos")
public class JogosController {

    @Autowired
    JogosServices jogosServices;

    @GetMapping("/lancamentos/vitrine")
    public ResponseEntity<List<Jogo>> listarLancamentosVitrine() {
        List<Jogo> vitrine = jogosServices.obterLancamentosVitrine();
        return ResponseEntity.ok(vitrine);
    }
}
