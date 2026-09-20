package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @NotBlank(message = "O nome do usuário não pode estar vazio")
    private String nome;

    @NotBlank(message = "O E-mail não pode estar vazio")
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    @Column(length = 300)
    private String fotoPerfil;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    private LocalDateTime ultimoLogin;

    @Enumerated(EnumType.STRING)
    private NivelConta nivelConta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HistoricoAcesso> historicosAcesso = new ArrayList<>();

    public Usuario() {}

    public Usuario(Long id, String nome, String email, String senha, String fotoPerfil, LocalDateTime dataCadastro, LocalDateTime ultimoLogin, NivelConta nivelConta) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
        this.dataCadastro = dataCadastro;
        this.ultimoLogin = ultimoLogin;
        this.nivelConta = nivelConta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.historicosAcesso.add(historico);
        historico.setUsuario(this);
    }

    public void removeHistorico(HistoricoAcesso historico) {
        this.historicosAcesso.remove(historico);
        historico.setUsuario(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.getSenha();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}