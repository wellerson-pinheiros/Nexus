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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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

    @GetMapping("/busca")
    public ResponseEntity<Page<JogoDetalhesDTOResponse>> buscarPorNomeDoJogo(
            @RequestParam String titulo,
            @PageableDefault(size = 20, sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<JogoDetalhesDTOResponse> jogos = jogosServices.buscarPorNome(titulo, pageable);
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/destaques/home")
    public ResponseEntity<List<JogoDetalhesDTOResponse>> listarTopDestaquesHome() {
        List<JogoDetalhesDTOResponse> topJogos = jogosServices.listarTopDestaquesHome();
        return ResponseEntity.ok(topJogos);
    }

    @GetMapping("/destaques/paginados")
    public ResponseEntity<Page<JogoDetalhesDTOResponse>> listarDestaquesPaginados(
            @PageableDefault(
                    size = 10,
                    sort = {"ratingTop", "rating", "ratingsCount"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        Page<JogoDetalhesDTOResponse> paginaJogos = jogosServices.listarDestaquesPaginados(pageable);
        return ResponseEntity.ok(paginaJogos);
    }

    @GetMapping("/filtro")
    public ResponseEntity<Page<JogoDetalhesDTOResponse>> filtrarJogos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String plataforma,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {

        Page<JogoDetalhesDTOResponse> resultado = jogosServices.filtrarJogos(
                titulo, genero, plataforma, precoMin, precoMax, pageable
        );

        return ResponseEntity.ok(resultado);
    }
}
