package nexus.com.br.game_store.dto;

import nexus.com.br.game_store.domain.Genero;

public record GeneroResumoDTO(
        Long id,
        String nome
) {
    public GeneroResumoDTO(Genero genero) {
        this(genero.getId(), genero.getNome());
    }
}
