package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_lista_item_biblioteca")
public class ListaItemBiblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária desta tabela

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_id", nullable = false)
    private Biblioteca biblioteca;

    @CreationTimestamp
    private LocalDateTime dataCompra;


    public ListaItemBiblioteca(Long id, Jogo jogo, Biblioteca biblioteca, LocalDateTime dataCompra) {
        this.id = id;
        this.jogo = jogo;
        this.biblioteca = biblioteca;
        this.dataCompra = dataCompra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Biblioteca getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }
}
