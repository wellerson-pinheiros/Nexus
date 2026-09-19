package nexus.com.br.game_store.controller;

import nexus.com.br.game_store.dto.RawgListJogosRequestDTO;
import nexus.com.br.game_store.service.RawgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/jogos")
public class JogosControllerRawg {

    private final RawgService rawgService;

    public JogosControllerRawg(RawgService rawgService) {
        this.rawgService = rawgService;
    }

    @GetMapping
            public ResponseEntity<RawgListJogosRequestDTO> ListarJogosRawg(@RequestParam(required = false) Integer page) {
                RawgListJogosRequestDTO jogos = rawgService.listarJogosDaApi(page);
                return ResponseEntity.ok(jogos);
            }
}
