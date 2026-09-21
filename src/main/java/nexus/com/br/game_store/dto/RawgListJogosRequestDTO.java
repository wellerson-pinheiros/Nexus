package nexus.com.br.game_store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RawgListJogosRequestDTO(

        Integer count,
        String next,
        String previous,
        List<GameSummaryDTO> results
) {

    public record GameSummaryDTO(
            Long id,
            String name,
            String slug,
            @JsonProperty("background_image")
            String backgroundImage,
            Double rating,
            @JsonProperty("rating_top")
            Double ratingTop,
            @JsonProperty("ratings_count")
            Integer ratingsCount,
            String released,
            Integer metacritic,
            Integer playtime,
            @JsonProperty("updated")
            String updated,
            @JsonProperty("esrb_rating")
            EsrbRatingDTO esrbRating,
            List<GenreSummaryDTO> genres,
            List<PlatformWrapperDTO> platforms
    ) {}

    public record GenreSummaryDTO(
            Long id,
            String name,
            String slug
    ) {}

    public record PlatformWrapperDTO(
            PlatformDTO platform,
            @JsonProperty("released_at")
            String releasedAt,
            RequirementsDTO requirements
    ) {}

    public record PlatformDTO(
            Long id,
            String name,
            String slug
    ) {}

    public record RequirementsDTO(
            String minimum,
            String recommended
    ) {}

    public record EsrbRatingDTO(
            Long id,
            String slug,
            String name
    ) {}

    // Getters (Caso precise explicitamente, mas em Java Records o próprio record já gera os acessos via method names, ex: results())
    public List<GameSummaryDTO> getResults() { return results; }
}