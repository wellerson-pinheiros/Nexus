package nexus.com.br.game_store.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_jogo_erro")
public class JogoErro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rawg_id")
    private Long rawgId;

    private String titulo;

    @Column(name = "numero_pagina")
    private Integer numeroPagina;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT", nullable = false)
    private String mensagemErro;

    @Column(name = "data_erro", nullable = false)
    private LocalDateTime dataErro = LocalDateTime.now();

    public JogoErro() {}

    public JogoErro(Long rawgId, String titulo, Integer numeroPagina, String mensagemErro) {
        this.rawgId = rawgId;
        this.titulo = titulo;
        this.numeroPagina = numeroPagina;
        this.mensagemErro = mensagemErro;
        this.dataErro = LocalDateTime.now();
    }

    // Getters e Setters
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public LocalDateTime getDataErro() {
        return dataErro;
    }

    public void setDataErro(LocalDateTime dataErro) {
        this.dataErro = dataErro;
    }
}
