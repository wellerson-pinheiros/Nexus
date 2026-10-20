package nexus.com.br.game_store.service;


import nexus.com.br.game_store.domain.Jogo;
import nexus.com.br.game_store.domain.JogoSpecifications;
import nexus.com.br.game_store.dto.JogoDetalhesDTOResponse;
import nexus.com.br.game_store.excecoes.ResourceNotFoundException;
import nexus.com.br.game_store.repository.JogosRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JogosServices {

    @Autowired
    JogosRepository jogosRepository;

    @Transactional(readOnly = true)
    public List<JogoDetalhesDTOResponse> obterLancamentosVitrine() {
        // Define a nota mínima para o jogo ser considerado "bom" para a vitrine
        Double notaMinima = 4.0;

        List<Jogo> jogosFiltrados = jogosRepository.findVitrineLancamentos(notaMinima);

        if (jogosFiltrados.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum lançamento encontrado para a vitrine.");
        }

        return jogosFiltrados.stream().map(JogoDetalhesDTOResponse::new).toList() ;
    }

    @Transactional(readOnly = true)
    public Page<JogoDetalhesDTOResponse> listarLancamentos(Pageable pageable) {
        // O Pageable já virá com a ordenação por data de lançamento decrescente do Controller
        Page<Jogo> pagina = jogosRepository.findAll(pageable);
        if (pagina.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum jogo cadastrado encontrado.");
        }
        return pagina.map(JogoDetalhesDTOResponse::new); // Convertendo para o DTO de resposta
    }

    @Transactional(readOnly = true)
    public Page<JogoDetalhesDTOResponse> buscarPorNome(String titulo, Pageable pageable) {
        Page<Jogo> pagina = jogosRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        if (pagina.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum jogo encontrado com o titulo: " + titulo);
        }
        return pagina.map(JogoDetalhesDTOResponse::new);
    }

    public List<JogoDetalhesDTOResponse> listarTopDestaquesHome() {
        Double ratingMinimo = 4.0;
        Integer minAvaliacoes = 10;

        Pageable pageable = PageRequest.of(
                0, 5,
                Sort.by(Sort.Direction.DESC, "ratingTop", "rating", "ratingsCount")
        );

        List<Jogo> destaques = jogosRepository.findTopDestaques(ratingMinimo, minAvaliacoes, pageable);
        if (destaques.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum destaque encontrado para a home.");
        }

        return destaques.stream()
                .map(JogoDetalhesDTOResponse::new)
                .collect(Collectors.toList());
    }

    // 2. Retorna a lista paginada completa de destaques
    public Page<JogoDetalhesDTOResponse> listarDestaquesPaginados(Pageable pageable) {
        Double ratingMinimo = 4.0;
        Integer minAvaliacoes = 10;

        Page<Jogo> pagina = jogosRepository.findDestaquesPaginados(ratingMinimo, minAvaliacoes, pageable);
        if (pagina.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum destaque paginado encontrado.");
        }
        return pagina.map(JogoDetalhesDTOResponse::new);
    }

    public Page<JogoDetalhesDTOResponse> filtrarJogos(
            String titulo,
            String genero,
            String plataforma,
            BigDecimal precoMin,
            BigDecimal precoMax,
            Pageable pageable) {

        Specification<Jogo> spec = Specification.where(JogoSpecifications.comTitulo(titulo))
                .and(JogoSpecifications.comGenero(genero))
                .and(JogoSpecifications.comPlataforma(plataforma))
                .and(JogoSpecifications.precoMaiorOuIgual(precoMin))
                .and(JogoSpecifications.precoMenorOuIgual(precoMax));

        Page<Jogo> pagina = jogosRepository.findAll(spec, pageable);
        if (pagina.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum jogo encontrado com os filtros informados.");
        }

        return pagina.map(JogoDetalhesDTOResponse::new);
    }
}
