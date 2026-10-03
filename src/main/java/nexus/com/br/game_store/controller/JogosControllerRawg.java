package nexus.com.br.game_store.controller;

import nexus.com.br.game_store.domain.ControleImportacao;
import nexus.com.br.game_store.domain.JogoErro;
import nexus.com.br.game_store.dto.RawgListJogosRequestDTO;
import nexus.com.br.game_store.repository.ControleImportacaoRepository;
import nexus.com.br.game_store.repository.JogoErroRepository;
import nexus.com.br.game_store.service.RawgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/jogos")
public class JogosControllerRawg {

    private final RawgService rawgService;
    private final ControleImportacaoRepository controleRepository;
    private final JogoErroRepository jogoErroRepository;

    public JogosControllerRawg(RawgService rawgService,
                               ControleImportacaoRepository controleRepository,
                               JogoErroRepository jogoErroRepository) {
        this.rawgService = rawgService;
        this.controleRepository = controleRepository;
        this.jogoErroRepository = jogoErroRepository;
    }

    // Consulta direta à API RAWG (Método Original)
    @GetMapping
    public ResponseEntity<RawgListJogosRequestDTO> ListarJogosRawg(@RequestParam(required = false) Integer page) {
        RawgListJogosRequestDTO jogos = rawgService.listarJogosDaApi(page);
        return ResponseEntity.ok(jogos);
    }

    // =========================================================================
    // NOVOS MÉTODOS DE CONTROLE DA IMPORTAÇÃO AUTOMÁTICA
    // =========================================================================

    /**
     * Retorna o status atual da importação (Página atual, total de jogos salvos, etc).
     * Rota no Insomnia: GET http://localhost:8080/api/jogos/importacao/status
     */
    @GetMapping("/importacao/status")
    public ResponseEntity<ControleImportacao> obterStatusImportacao() {
        return controleRepository.findById(1L)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Dispara um lote manualmente sem esperar o intervalo de 2 horas.
     * Rota no Insomnia: POST http://localhost:8080/api/jogos/importacao/executar?paginas=5
     */
    @PostMapping("/importacao/executar")
    public ResponseEntity<String> executarLoteManualmente(@RequestParam(defaultValue = "10") int paginas) {
        rawgService.executarLoteImportacao(paginas);
        return ResponseEntity.ok(String.format("Lote de %d páginas executado com sucesso!", paginas));
    }

    /**
     * Retorna a lista de jogos que falharam ao tentar salvar no banco.
     * Rota no Insomnia: GET http://localhost:8080/api/jogos/importacao/erros
     */
    @GetMapping("/importacao/erros")
    public ResponseEntity<List<JogoErro>> listarErrosImportacao() {
        List<JogoErro> erros = jogoErroRepository.findAll();
        return ResponseEntity.ok(erros);
    }
}