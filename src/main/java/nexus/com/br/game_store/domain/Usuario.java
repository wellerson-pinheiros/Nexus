package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    Long usuarioID;

    @NotBlank(message = "O nome do usúario não pode estár vazio")
    String nome;

    @NotBlank(message = "O E-mail não pode estár vazio")
    @Column(unique = true, nullable = false)
    @Email
    String email;

    @NotBlank(message = "A senha é obrigatória.")
    @NotNull
    String senha;

    @Column(length = 300, nullable = true)
    String fotoPerfil;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime dataCadastro;


    LocalDateTime ultimoLogin;

    //    HistoricoAcesso HistoricoAcesso;
    //    Biblioteca biblioteca;
    //    ListaDesejos listaDesejos;
    //    NivelConta nivelConta;

    // Contructor

    Usuario() {}

    Usuario(Long usuarioID,String nome, String email, String senha, String fotoPerfil) {
        this.usuarioID = usuarioID;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
    }

    //Getter & Setter

    public Long getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Long usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }



}
