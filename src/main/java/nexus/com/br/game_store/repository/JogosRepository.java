package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Jogo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JogosRepository extends JpaRepository<Jogo,Long> {

    @Query(value = """
    SELECT * FROM tb_jogo
    WHERE rating >= :ratingMinimo 
    ORDER BY data_lancamento DESC, rating DESC 
    LIMIT 10
    """, nativeQuery = true)
    List<Jogo> findVitrineLancamentos(@Param("ratingMinimo") Double ratingMinimo);

    Page<Jogo> findAll(Pageable pageable);

    boolean existsByRawgId(Long rawgId);

}
