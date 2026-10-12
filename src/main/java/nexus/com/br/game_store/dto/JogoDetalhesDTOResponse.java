package nexus.com.br.game_store.dto;

import nexus.com.br.game_store.domain.Jogo;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record JogoDetalhesDTOResponse (

        Long id,
        Long rawgId,
        String slugRawg,
        String titulo,
        String descricao,
        String imagemCapa,
        LocalDate dataLancamento,
        Double rating,
        Double ratingTop,
        Integer ratingsCount,
        Integer metacritic,
        Integer playtime,
        String esrbRating,
        Set<GeneroResumoDTO> generos,
        Set<PlataformaResumoDTO> plataformas
) {
    public JogoDetalhesDTOResponse(Jogo jogo) {
        this(
                jogo.getId(),
                jogo.getRawgId(),
                jogo.getSlugRawg(),
                jogo.getTitulo(),
                jogo.getDescricao(),
                jogo.getImagemCapa(),
                jogo.getDataLancamento(),
                jogo.getRating(),
                jogo.getRatingTop(),
                jogo.getRatingsCount(),
                jogo.getMetacritic(),
                jogo.getPlaytime(),
                jogo.getEsrbRating(),
                jogo.getGeneros().stream().map(g -> new GeneroResumoDTO(g.getId(), g.getNome())).collect(Collectors.toSet()),
                jogo.getPlataformas().stream().map(p -> new PlataformaResumoDTO(p.getPlataforma().getPlataformaId(), p.getPlataforma().getName())).collect(Collectors.toSet())
        );
    }
}

