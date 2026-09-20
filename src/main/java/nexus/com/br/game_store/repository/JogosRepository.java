package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogosRepository extends JpaRepository<Jogo,Long> {
}
