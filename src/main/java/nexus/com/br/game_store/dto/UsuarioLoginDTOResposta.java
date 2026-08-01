package nexus.com.br.game_store.dto;

public record UsuarioLoginDTOResposta(

        Long id,
        String email,
        String fotoPerfil,
        String token
) {
    public UsuarioLoginDTOResposta(Long id, String email, String fotoPerfil, String token) {
        this.id = id;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.token = token;
    }
}
