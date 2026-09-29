package nexus.com.br.game_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciMinhaSenhaDTO (

        @NotBlank
        @Email
        String email
){
}
