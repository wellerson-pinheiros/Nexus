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
}
