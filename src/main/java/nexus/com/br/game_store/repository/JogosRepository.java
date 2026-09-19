package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Jogos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogosRepository extends JpaRepository<Jogos,Long> {
}
