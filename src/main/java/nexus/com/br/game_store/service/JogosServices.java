package nexus.com.br.game_store.service;


import nexus.com.br.game_store.domain.Jogo;
import nexus.com.br.game_store.dto.JogoDetalhesDTOResponse;
import nexus.com.br.game_store.repository.JogosRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class JogosServices {

    @Autowired
    JogosRepository jogosRepository;

    @Transactional(readOnly = true)
    public List<JogoDetalhesDTOResponse> obterLancamentosVitrine() {
        // Define a nota mínima para o jogo ser considerado "bom" para a vitrine
        Double notaMinima = 4.0;

        List<Jogo> jogosFiltrados = jogosRepository.findVitrineLancamentos(notaMinima);

        return jogosFiltrados.stream().map(JogoDetalhesDTOResponse::new).toList() ;
    }

    @Transactional(readOnly = true)
    public Page<JogoDetalhesDTOResponse> listarLancamentos(Pageable pageable) {
        // O Pageable já virá com a ordenação por data de lançamento decrescente do Controller
        return jogosRepository.findAll(pageable)
                .map(JogoDetalhesDTOResponse::new); // Convertendo para o DTO de resposta
    }
}
