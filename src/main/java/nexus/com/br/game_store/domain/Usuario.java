package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_usuarios")
public class Usuario implements UserDetails {

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

    @Enumerated(EnumType.STRING)
    NivelConta nivelConta;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<HistoricoAcesso> historicosAcesso = new ArrayList<>();
    //    Biblioteca biblioteca;
    //    ListaDesejos listaDesejos;


    // Contructor

    Usuario() {}

    public Usuario(Long usuarioID, NivelConta nivelConta, LocalDateTime ultimoLogin, LocalDateTime dataCadastro, String fotoPerfil, String senha, String email, String nome) {
        this.usuarioID = usuarioID;
        this.nivelConta = nivelConta;
        this.ultimoLogin = ultimoLogin;
        this.dataCadastro = dataCadastro;
        this.fotoPerfil = fotoPerfil;
        this.senha = senha;
        this.email = email;
        this.nome = nome;
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

    public NivelConta getNivelConta() {
        return nivelConta;
    }

    public void setNivelConta(NivelConta nivelConta) {
        this.nivelConta = nivelConta;
    }

    public List<HistoricoAcesso> getHistoricosAcesso() {
        return historicosAcesso;
    }

    public void addHistorico(HistoricoAcesso historico) {
        this.historicosAcesso.add(historico); // Adiciona na lista do usuário
        historico.setUsuario(this);           // SETA O USUÁRIO no histórico (Sincroniza o outro lado!)
    }

    // HELPER METHOD para Remover
    public void removeHistorico(HistoricoAcesso historico) {
        this.historicosAcesso.remove(historico); // Remove da lista
        historico.setUsuario(null);              // Tira a referência do histórico
    }

    // Spring security metodos


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.getSenha();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
