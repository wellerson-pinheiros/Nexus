package nexus.com.br.game_store.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_controle_importacao")
public class ControleImportacao {

    @Id
    private Long id = 1L; // Mantemos sempre o ID 1 para garantir uma linha única de controle

    @Column(name = "ultima_pagina_processada", nullable = false)
    private Integer ultimaPaginaProcessada = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusImportacao status = StatusImportacao.EM_ANDAMENTO;

    @Column(name = "total_jogos_importados")
    private Integer totalJogosImportados = 0;

    @Column(name = "data_ultima_execucao")
    private LocalDateTime dataUltimaExecucao;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;

    public ControleImportacao() {}

    public ControleImportacao(Integer ultimaPaginaProcessada, StatusImportacao status) {
        this.id = 1L;
        this.ultimaPaginaProcessada = ultimaPaginaProcessada;
        this.status = status;
        this.dataUltimaExecucao = LocalDateTime.now();
    }

    // Métodos utilitários para atualizar estado rapidamente
    public void avancarPagina() {
        this.ultimaPaginaProcessada++;
        this.dataUltimaExecucao = LocalDateTime.now();
    }

    public void incrementarJogos(int quantidade) {
        this.totalJogosImportados += quantidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUltimaPaginaProcessada() {
        return ultimaPaginaProcessada;
    }

    public void setUltimaPaginaProcessada(Integer ultimaPaginaProcessada) {
        this.ultimaPaginaProcessada = ultimaPaginaProcessada;
    }

    public StatusImportacao getStatus() {
        return status;
    }

    public void setStatus(StatusImportacao status) {
        this.status = status;
    }

    public Integer getTotalJogosImportados() {
        return totalJogosImportados;
    }

    public void setTotalJogosImportados(Integer totalJogosImportados) {
        this.totalJogosImportados = totalJogosImportados;
    }

    public LocalDateTime getDataUltimaExecucao() {
        return dataUltimaExecucao;
    }

    public void setDataUltimaExecucao(LocalDateTime dataUltimaExecucao) {
        this.dataUltimaExecucao = dataUltimaExecucao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
}
