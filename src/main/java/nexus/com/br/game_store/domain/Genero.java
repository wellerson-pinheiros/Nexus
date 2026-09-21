package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_genero")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genero_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    @Column(unique = true)
    private Long rawgId;

    @Column(unique = true)
    private String slug;

    @ManyToMany(mappedBy = "generos")
    private Set<Jogo> jogos = new HashSet<>();

    public Genero() {}

    public Genero(Long id, String nome, Long rawgId, String slug) {
        this.id = id;
        this.nome = nome;
        this.rawgId = rawgId;
        this.slug = slug;
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

    public Long getRawgId() {
        return rawgId;
    }

    public void setRawgId(Long rawgId) {
        this.rawgId = rawgId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Set<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(Set<Jogo> jogos) {
        this.jogos = jogos;
    }
}