package nexus.com.br.game_store.service;


import nexus.com.br.game_store.domain.Jogo;
import nexus.com.br.game_store.repository.JogosRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class JogosServices {

    @Autowired
    JogosRepository jogosRepository;

    @Transactional(readOnly = true)
    public List<Jogo> obterLancamentosVitrine() {
        // Define a nota mínima para o jogo ser considerado "bom" para a vitrine
        Double notaMinima = 4.0;

        return jogosRepository.findTop10ByRatingGreaterThanEqualOrderByDataLancamentoDesc(notaMinima);
    }
}
