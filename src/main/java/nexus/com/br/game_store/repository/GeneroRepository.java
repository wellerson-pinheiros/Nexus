package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findBySlug(String slug);
}
