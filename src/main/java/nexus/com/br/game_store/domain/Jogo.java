package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jogo_id")
    private Long id;

    @Column(name = "rawg_id", unique = true)
    private Long rawgId;

    @Column(unique = true)
    private String slugRawg; // Corrigido de slugRaweg para slugRawg

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    private String imagemCapa;
    private Instant dataLancamento;
    private Double rating;
    private String requisitosMinimos;
    private String requisitosRecomendados;

    @ManyToMany
    @JoinTable(
            name = "tb_jogo_genero",
            joinColumns = @JoinColumn(name = "jogo_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    public Jogo() {}

    public Jogo(Long id, Long rawgId, String slugRawg, String titulo, String descricao, String imagemCapa, Instant dataLancamento, Double rating, String requisitosMinimos, String requisitosRecomendados) {
        this.id = id;
        this.rawgId = rawgId;
        this.slugRawg = slugRawg;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagemCapa = imagemCapa;
        this.dataLancamento = dataLancamento;
        this.rating = rating;
        this.requisitosMinimos = requisitosMinimos;
        this.requisitosRecomendados = requisitosRecomendados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRawgId() {
        return rawgId;
    }

    public void setRawgId(Long rawgId) {
        this.rawgId = rawgId;
    }

    public String getSlugRawg() {
        return slugRawg;
    }

    public void setSlugRawg(String slugRawg) {
        this.slugRawg = slugRawg;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public Instant getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Instant dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRequisitosMinimos() {
        return requisitosMinimos;
    }

    public void setRequisitosMinimos(String requisitosMinimos) {
        this.requisitosMinimos = requisitosMinimos;
    }

    public String getRequisitosRecomendados() {
        return requisitosRecomendados;
    }

    public void setRequisitosRecomendados(String requisitosRecomendados) {
        this.requisitosRecomendados = requisitosRecomendados;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }
}