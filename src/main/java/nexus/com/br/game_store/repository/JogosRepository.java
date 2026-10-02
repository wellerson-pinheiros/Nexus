package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Jogo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JogosRepository extends JpaRepository<Jogo,Long> {

    List<Jogo> findTop10ByRatingGreaterThanEqualOrderByDataLancamentoDesc(Double ratingMinimo);
}
