package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.HistoricoAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoAcessoRepository extends JpaRepository<HistoricoAcesso, Long> {
}
