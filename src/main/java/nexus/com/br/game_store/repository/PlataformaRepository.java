package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {

    Optional<Plataforma> findBySlug(String slug);
}
