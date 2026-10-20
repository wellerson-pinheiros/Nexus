package nexus.com.br.game_store.dto;

import nexus.com.br.game_store.domain.JogoPlataforma;
import nexus.com.br.game_store.domain.Plataforma;

public record PlataformaResumoDTO(
        Long plataformaId,
        String name
) {
    public PlataformaResumoDTO(JogoPlataforma jogoPlataforma) {
        this(
                jogoPlataforma.getPlataforma().getPlataformaId(),
                jogoPlataforma.getPlataforma().getName()
        );
    }
}
