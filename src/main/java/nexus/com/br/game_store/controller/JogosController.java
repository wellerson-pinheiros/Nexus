package nexus.com.br.game_store.controller;

import nexus.com.br.game_store.domain.Jogo;
import nexus.com.br.game_store.dto.JogoDetalhesDTOResponse;
import nexus.com.br.game_store.service.JogosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<List<JogoDetalhesDTOResponse>> listarLancamentosVitrine() {
        List<JogoDetalhesDTOResponse> vitrine = jogosServices.obterLancamentosVitrine();
        return ResponseEntity.ok(vitrine);
    }

    @GetMapping("/lancamentos/paginados")
    public ResponseEntity<Page<JogoDetalhesDTOResponse>> listarLancamentos(
            @PageableDefault(size = 20, sort = "dataLancamento", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<JogoDetalhesDTOResponse> pagina = jogosServices.listarLancamentos(pageable);
        return ResponseEntity.ok(pagina);
    }


}
