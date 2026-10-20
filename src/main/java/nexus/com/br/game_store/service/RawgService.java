package nexus.com.br.game_store.service;

import nexus.com.br.game_store.domain.ControleImportacao;
import nexus.com.br.game_store.domain.StatusImportacao;
import nexus.com.br.game_store.dto.RawgListJogosRequestDTO;
import nexus.com.br.game_store.repository.ControleImportacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class RawgService {

    private final RestClient restClient;

    private static final Logger log = LoggerFactory.getLogger(RawgService.class);

    @Value("${secretRAWG}")
    private String apiKey;


    private final JogoPersistenciaService jogoPersistenciaService;
    private final ControleImportacaoRepository controleImportacaoRepository;


    public RawgService(RestClient restClient,
                       JogoPersistenciaService jogoPersistenciaService,
                       ControleImportacaoRepository controleImportacaoRepository) {
        this.restClient = restClient;
        this.jogoPersistenciaService = jogoPersistenciaService;
        this.controleImportacaoRepository = controleImportacaoRepository;
    }


    public RawgListJogosRequestDTO listarJogosDaApi(Integer page) {
        int paginaAtual = (page != null) ? page : 1;

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", apiKey)
                        .queryParam("page", paginaAtual)
                        .build())
                .retrieve()
                .body(RawgListJogosRequestDTO.class);
    }

    // Sobrecarga: Se chamarem sem argumentos, assume a página 1 automaticamente
    public RawgListJogosRequestDTO listarJogosDaApi() {
        return listarJogosDaApi(1);
    }


    public void executarLoteImportacao(int limitePaginasNoLote) {
        ControleImportacao controle = controleImportacaoRepository.findById(1L)
                .orElseGet(() -> controleImportacaoRepository.save(new ControleImportacao()));

        if (controle.getStatus() == StatusImportacao.CONCLUIDO || controle.getStatus() == StatusImportacao.ERRO_AUTENTICACAO) {
            log.info("Importação ignorada. Status atual: {}", controle.getStatus());
            return;
        }

        controle.setStatus(StatusImportacao.EM_ANDAMENTO);
        controle.setMensagemErro(null);

        int paginaAtual = controle.getUltimaPaginaProcessada() + 1;
        int paginasProcessadasNesteCiclo = 0;

        while (paginasProcessadasNesteCiclo < limitePaginasNoLote) {
            try {
                log.info("Iniciando requisição da página {} na RAWG API...", paginaAtual);

                // Reaproveita o seu método original
                RawgListJogosRequestDTO respostaApi = listarJogosDaApi(paginaAtual);

                if (respostaApi == null || respostaApi.results() == null || respostaApi.results().isEmpty()) {
                    log.info("Nenhum jogo retornado. Chegamos ao fim do catálogo!");
                    controle.setStatus(StatusImportacao.CONCLUIDO);
                    break;
                }

                int jogosSalvosNaPagina = 0;
                for (RawgListJogosRequestDTO.GameSummaryDTO gameDto : respostaApi.results()) {
                    boolean salvou = jogoPersistenciaService.processarESalvarUnicoJogo(gameDto, paginaAtual);
                    if (salvou) {
                        jogosSalvosNaPagina++;
                    }
                }

                controle.setUltimaPaginaProcessada(paginaAtual);
                controle.incrementarJogos(jogosSalvosNaPagina);
                controle.setDataUltimaExecucao(LocalDateTime.now());
                controleImportacaoRepository.save(controle);

                log.info("Página {} processada com sucesso! {} novos jogos salvos.", paginaAtual, jogosSalvosNaPagina);

                if (respostaApi.next() == null) {
                    log.info("Atributo 'next' é nulo. Importação total CONCLUÍDA!");
                    controle.setStatus(StatusImportacao.CONCLUIDO);
                    controleImportacaoRepository.save(controle);
                    break;
                }

                paginaAtual++;
                paginasProcessadasNesteCiclo++;

            } catch (HttpClientErrorException.TooManyRequests e) {
                log.warn("Limite de requisições (429) atingido na página {}. Pausando ciclo.", paginaAtual);
                controle.setStatus(StatusImportacao.PAUSADO_RATE_LIMIT);
                controle.setMensagemErro("HTTP 429 Too Many Requests - Limite de requisições excedido.");
                controleImportacaoRepository.save(controle);
                break;

            } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
                log.error("Erro de autenticação (401/403) na RAWG API. Verifique sua secretRAWG.", e);
                controle.setStatus(StatusImportacao.ERRO_AUTENTICACAO);
                controle.setMensagemErro("Chave de API inválida ou sem permissão.");
                controleImportacaoRepository.save(controle);
                break;

            } catch (HttpServerErrorException e) {
                log.error("Servidor da RAWG instável (HTTP {}). Tentaremos novamente no próximo ciclo.", e.getStatusCode());
                controle.setMensagemErro("Erro no servidor da RAWG: " + e.getResponseBodyAsString());
                controleImportacaoRepository.save(controle);
                break;

            } catch (Exception e) {
                log.error("Erro inesperado durante a comunicação com a RAWG na página {}", paginaAtual, e);
                controle.setMensagemErro("Erro inesperado: " + e.getMessage());
                controleImportacaoRepository.save(controle);
                break;
            }
        }
    }
}

