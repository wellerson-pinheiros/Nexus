package nexus.com.br.game_store.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "tb_token_reset_senha")
public class TokenResetSenha {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String token;

        @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, name = "usuario_id")
        private Usuario usuario;

        private LocalDateTime dataExpiracao;

        public TokenResetSenha() {}

    public TokenResetSenha(Long id, String token, LocalDateTime dataExpiracao, Usuario usuario) {
        this.id = id;
        this.token = token;
        this.dataExpiracao = dataExpiracao;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

