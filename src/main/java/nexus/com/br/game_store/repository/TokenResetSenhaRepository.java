package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.TokenResetSenha;
import nexus.com.br.game_store.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenResetSenhaRepository extends JpaRepository<TokenResetSenha, Long> {
    Optional<TokenResetSenha> findByToken(String token);

    Optional<TokenResetSenha> findByUsuario(Usuario usuario);
}