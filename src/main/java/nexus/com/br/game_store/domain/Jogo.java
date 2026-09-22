package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String slugRawg;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    private String imagemCapa;
    private LocalDateTime dataLancamento;
    private Double rating;
    private Double ratingTop;
    private Integer ratingsCount;
    private Integer metacritic;
    private Integer playtime;
    private String esrbRating; // Nome da classificação (ex: "Mature")

    @ManyToMany
    @JoinTable(
            name = "tb_jogo_genero",
            joinColumns = @JoinColumn(name = "jogo_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JogoPlataforma> plataformas = new HashSet<>();

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ListaItemDesejo> listaItemDesejo = new HashSet<>();

    public Jogo() {}


    public Jogo(Long id, Long rawgId, String slugRawg, String titulo, String descricao, String imagemCapa, LocalDateTime dataLancamento, Double rating, Double ratingTop, Integer ratingsCount, Integer metacritic, Integer playtime, String esrbRating, Set<Genero> generos, Set<JogoPlataforma> plataformas) {
        this.id = id;
        this.rawgId = rawgId;
        this.slugRawg = slugRawg;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagemCapa = imagemCapa;
        this.dataLancamento = dataLancamento;
        this.rating = rating;
        this.ratingTop = ratingTop;
        this.ratingsCount = ratingsCount;
        this.metacritic = metacritic;
        this.playtime = playtime;
        this.esrbRating = esrbRating;
        this.generos = generos;
        this.plataformas = plataformas;
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

    public LocalDateTime getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDateTime dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRatingTop() {
        return ratingTop;
    }

    public void setRatingTop(Double ratingTop) {
        this.ratingTop = ratingTop;
    }

    public Integer getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public Integer getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(Integer metacritic) {
        this.metacritic = metacritic;
    }

    public Integer getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }

    public String getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(String esrbRating) {
        this.esrbRating = esrbRating;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }

    public Set<JogoPlataforma> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(Set<JogoPlataforma> plataformas) {
        this.plataformas = plataformas;
    }
}