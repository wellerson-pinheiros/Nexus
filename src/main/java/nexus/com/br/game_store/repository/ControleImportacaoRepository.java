package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.ControleImportacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControleImportacaoRepository extends JpaRepository<ControleImportacao, Long> {
}
