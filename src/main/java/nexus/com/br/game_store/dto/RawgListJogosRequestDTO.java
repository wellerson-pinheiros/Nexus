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
            String released,
            List<GenreSummaryDTO> genres
    ) {}

    public record GenreSummaryDTO(
            Long id,
            String name,
            String slug
    ) {}
    public record PlatformWrapperDTO(
            PlatformDTO platform
    ) {}

    public record PlatformDTO(
            Long id,
            String name,
            String slug
    ) {}

    // Getters
    public List<GameSummaryDTO> getResults() { return results; }
}
