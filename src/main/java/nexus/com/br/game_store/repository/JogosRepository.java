package nexus.com.br.game_store.repository;

import nexus.com.br.game_store.domain.Jogo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JogosRepository extends JpaRepository<Jogo,Long> , JpaSpecificationExecutor<Jogo> {

    @Query(value = """
    SELECT * FROM tb_jogo
    WHERE rating >= :ratingMinimo 
    ORDER BY data_lancamento DESC, rating DESC 
    LIMIT 10
    """, nativeQuery = true)
    List<Jogo> findVitrineLancamentos(@Param("ratingMinimo") Double ratingMinimo);

    Page<Jogo> findAll(Pageable pageable);

    boolean existsByRawgId(Long rawgId);

    Page<Jogo> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    @Query("""
        SELECT j FROM Jogo j 
        WHERE j.rating >= :ratingMinimo 
          AND j.ratingsCount >= :minAvaliacoes
        ORDER BY j.ratingTop DESC, j.rating DESC, j.ratingsCount DESC
    """)
    List<Jogo> findTopDestaques(
            @Param("ratingMinimo") Double ratingMinimo,
            @Param("minAvaliacoes") Integer minAvaliacoes,
            Pageable pageable
    );

    // Destaques completos com paginação para a vitrine
    @Query("""
        SELECT j FROM Jogo j 
        WHERE j.rating >= :ratingMinimo 
          AND j.ratingsCount >= :minAvaliacoes
    """)
    Page<Jogo> findDestaquesPaginados(
            @Param("ratingMinimo") Double ratingMinimo,
            @Param("minAvaliacoes") Integer minAvaliacoes,
            Pageable pageable
    );

}
