package nexus.com.br.game_store.dto;

import nexus.com.br.game_store.domain.NivelConta;
import nexus.com.br.game_store.domain.Usuario;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String fotoPerfil,
        LocalDateTime dataCadastro,
        NivelConta nivelConta
) {

    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getFotoPerfil(),
                usuario.getDataCadastro(),
                usuario.getNivelConta()
        );
    }}
