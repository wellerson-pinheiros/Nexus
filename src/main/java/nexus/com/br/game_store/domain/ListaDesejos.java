package nexus.com.br.game_store.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_lista_desejos")
public class ListaDesejos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listaDesejosId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "listaDesejos", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ListaItemDesejo> listaItemDesejos = new HashSet<>();

}
