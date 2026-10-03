package nexus.com.br.game_store.service;

import nexus.com.br.game_store.domain.*;
import nexus.com.br.game_store.dto.RawgListJogosRequestDTO;
import nexus.com.br.game_store.repository.GeneroRepository;
import nexus.com.br.game_store.repository.JogoErroRepository;
import nexus.com.br.game_store.repository.JogosRepository;
import nexus.com.br.game_store.repository.PlataformaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class JogoPersistenciaService {


    private final JogosRepository jogoRepository;
    private final GeneroRepository generoRepository;
    private final PlataformaRepository plataformaRepository;
    private final JogoErroRepository jogoErroRepository;

    public JogoPersistenciaService(JogosRepository jogoRepository,
                                   GeneroRepository generoRepository,
                                   PlataformaRepository plataformaRepository,
                                   JogoErroRepository jogoErroRepository) {
        this.jogoRepository = jogoRepository;
        this.generoRepository = generoRepository;
        this.plataformaRepository = plataformaRepository;
        this.jogoErroRepository = jogoErroRepository;
    }

    /**
     * Processa e salva um único jogo em uma transação isolada.
     * Se falhar, faz rollback APENAS deste jogo e grava o log na tb_jogo_erro.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean processarESalvarUnicoJogo(RawgListJogosRequestDTO.GameSummaryDTO gameDto, Integer numeroPagina) {
        try {
            // 1. Evita duplicidade se já existir no banco
            if (gameDto.id() == null || jogoRepository.existsByRawgId(gameDto.id())) {
                return false;
            }

            // 2. Mapeamento dos dados básicos
            Jogo jogo = new Jogo();
            jogo.setRawgId(gameDto.id());
            jogo.setSlugRawg(gameDto.slug());
            jogo.setTitulo(gameDto.name());
            jogo.setImagemCapa(gameDto.backgroundImage());
            jogo.setRating(gameDto.rating());
            jogo.setRatingTop(gameDto.ratingTop());
            jogo.setRatingsCount(gameDto.ratingsCount());
            jogo.setMetacritic(gameDto.metacritic());
            jogo.setPlaytime(gameDto.playtime());

            if (gameDto.esrbRating() != null) {
                jogo.setEsrbRating(gameDto.esrbRating().name());
            }

            // 3. Conversão da Data
            if (gameDto.released() != null && !gameDto.released().isBlank()) {
                jogo.setDataLancamento(LocalDate.parse(gameDto.released(), DateTimeFormatter.ISO_LOCAL_DATE));
            }

            // 4. Associação de Gêneros
            if (gameDto.genres() != null) {
                for (RawgListJogosRequestDTO.GenreSummaryDTO genreDto : gameDto.genres()) {
                    Genero genero = generoRepository.findBySlug(genreDto.slug())
                            .orElseGet(() -> {
                                Genero novoGenero = new Genero();
                                novoGenero.setNome(genreDto.name());
                                novoGenero.setSlug(genreDto.slug());
                                return generoRepository.save(novoGenero);
                            });
                    jogo.adicionarGenero(genero);
                }
            }

            // 5. Associação de Plataformas
            if (gameDto.platforms() != null) {
                for (RawgListJogosRequestDTO.PlatformWrapperDTO platWrapper : gameDto.platforms()) {
                    if (platWrapper.platform() != null) {
                        RawgListJogosRequestDTO.PlatformDTO platDto = platWrapper.platform();

                        Plataforma plataforma = plataformaRepository.findBySlug(platDto.slug())
                                .orElseGet(() -> {
                                    Plataforma novaPlataforma = new Plataforma();
                                    novaPlataforma.setName(platDto.name());
                                    novaPlataforma.setSlug(platDto.slug());
                                    return plataformaRepository.save(novaPlataforma);
                                });

                        JogoPlataforma jogoPlataforma = new JogoPlataforma();
                        jogoPlataforma.setJogo(jogo);
                        jogoPlataforma.setPlataforma(plataforma);

                        jogo.adicionarPlataforma(jogoPlataforma);
                    }
                }
            }

            // 6. Salva o jogo no banco
            jogoRepository.save(jogo);
            return true;

        } catch (Exception e) {
            // Em caso de erro ao salvar ESTE jogo, salvamos o log de erro
            registrarErroDoJogo(gameDto, numeroPagina, e.getMessage());
            return false;
        }
    }

    /**
     * Transação isolada também para registrar a falha na tb_jogo_erro
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarErroDoJogo(RawgListJogosRequestDTO.GameSummaryDTO gameDto, Integer numeroPagina, String mensagemErro) {
        Long rawgId = (gameDto != null) ? gameDto.id() : null;
        String titulo = (gameDto != null) ? gameDto.name() : "Desconhecido";

        JogoErro jogoErro = new JogoErro(rawgId, titulo, numeroPagina, mensagemErro);
        jogoErroRepository.save(jogoErro);
    }
}
