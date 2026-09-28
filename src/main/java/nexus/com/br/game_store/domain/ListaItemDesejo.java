package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_lista_item_desejo")
public class ListaItemDesejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listaItemDesejoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_desejos_id", nullable = false)
    private ListaDesejos listaDesejos;

    @CreationTimestamp
    private LocalDateTime dataAdicao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    public ListaItemDesejo() {}

    public ListaItemDesejo(Long listaItemDesejoId, Jogo jogo, LocalDateTime dataAdicao, ListaDesejos listaDesejos, Prioridade prioridade) {
        this.listaItemDesejoId = listaItemDesejoId;
        this.jogo = jogo;
        this.dataAdicao = dataAdicao;
        this.listaDesejos = listaDesejos;
        this.prioridade = prioridade;
    }

    public Long getListaItemDesejoId() {
        return listaItemDesejoId;
    }

    public void setListaItemDesejoId(Long listaItemDesejoId) {
        this.listaItemDesejoId = listaItemDesejoId;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public ListaDesejos getListaDesejos() {
        return listaDesejos;
    }

    public void setListaDesejos(ListaDesejos listaDesejos) {
        this.listaDesejos = listaDesejos;
    }

    public LocalDateTime getDataAdicao() {
        return dataAdicao;
    }

    public void setDataAdicao(LocalDateTime dataAdicao) {
        this.dataAdicao = dataAdicao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
}
