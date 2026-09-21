package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_jogo_plataforma")
public class JogoPlataforma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    @ManyToOne
    @JoinColumn(name = "plataforma_id")
    private Plataforma plataforma;

    private LocalDate releasedAt;

    @Column(columnDefinition = "TEXT")
    private String requisitosMinimos;

    @Column(columnDefinition = "TEXT")
    private String requisitosRecomendados;

    public JogoPlataforma() {}

    public JogoPlataforma(Long id, Jogo jogo, Plataforma plataforma, LocalDate releasedAt, String requisitosMinimos, String requisitosRecomendados) {
        this.id = id;
        this.jogo = jogo;
        this.plataforma = plataforma;
        this.releasedAt = releasedAt;
        this.requisitosMinimos = requisitosMinimos;
        this.requisitosRecomendados = requisitosRecomendados;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }
    public Plataforma getPlataforma() { return plataforma; }
    public void setPlataforma(Plataforma plataforma) { this.plataforma = plataforma; }
    public LocalDate getReleasedAt() { return releasedAt; }
    public void setReleasedAt(LocalDate releasedAt) { this.releasedAt = releasedAt; }
    public String getRequisitosMinimos() { return requisitosMinimos; }
    public void setRequisitosMinimos(String requisitosMinimos) { this.requisitosMinimos = requisitosMinimos; }
    public String getRequisitosRecomendados() { return requisitosRecomendados; }
    public void setRequisitosRecomendados(String requisitosRecomendados) { this.requisitosRecomendados = requisitosRecomendados; }
}