package nexus.com.br.game_store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioPerfilUpdateDTO(
        @NotBlank(message = "O nome não pode ficar em branco.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        String fotoPerfil

        // Campo para futuras preferências (ex: receber newsletters, tema escuro/claro, etc.)
        //Boolean recebeNotificacoes
) {}