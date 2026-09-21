package nexus.com.br.game_store.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_plataforma")
public class Plataforma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PlataformaId;

    @Column(name = "rawg_id", unique = true)
    private Long rawgId;

    private String name;
    private String slug;

    public Plataforma() {}

    public Plataforma(Long plataformaId, Long rawgId, String name, String slug) {
        PlataformaId = plataformaId;
        this.rawgId = rawgId;
        this.name = name;
        this.slug = slug;
    }

    // Getters e Setters

    public Long getPlataformaId() {
        return PlataformaId;
    }

    public void setPlataformaId(Long plataformaId) {
        PlataformaId = plataformaId;
    }

    public Long getRawgId() {
        return rawgId;
    }

    public void setRawgId(Long rawgId) {
        this.rawgId = rawgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}