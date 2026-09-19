package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity(name = "tb_jogos")
public class Jogos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJogo;

    //o id da biblioteca do jogo
    @Column(name = "rawg_id", unique = true)
    private Long rawgId;

    @Column(unique = true)
    private String slugRaweg;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = true)
    private String imagemCapa;

    @Column(nullable = true)
    private Instant dataLancamento;

    @Column(nullable = true)
    private Double rating;

    @Column(nullable = true)
    private String requisitosMinimos;

    @Column(nullable = true)
    private String requisitosRecomendados;

    public Jogos() {}

    public Jogos(Long idJogo, String requisitosRecomendados, String requisitosMinimos, Double rating, Instant dataLancamento, String imagemCapa, String descricao, String titulo, String slugRaweg, Long rawegID) {
        this.idJogo = idJogo;
        this.requisitosRecomendados = requisitosRecomendados;
        this.requisitosMinimos = requisitosMinimos;
        this.rating = rating;
        this.dataLancamento = dataLancamento;
        this.imagemCapa = imagemCapa;
        this.descricao = descricao;
        this.titulo = titulo;
        this.slugRaweg = slugRaweg;
        this.rawgId = rawegID;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Instant getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Instant dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSlugRaweg() {
        return slugRaweg;
    }

    public void setSlugRaweg(String slugRaweg) {
        this.slugRaweg = slugRaweg;
    }

    public Long getRawegID() {
        return rawgId;
    }

    public void setRawegID(Long rawgId) { // Ajustado o parâmetro e o this
        this.rawgId = rawgId;
    }

    public Long getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(Long idJogo) {
        this.idJogo = idJogo;
    }
}
