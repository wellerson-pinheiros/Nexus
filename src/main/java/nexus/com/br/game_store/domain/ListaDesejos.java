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

    public ListaDesejos() {}

    public ListaDesejos(Long listaDesejosId, Usuario usuario, Set<ListaItemDesejo> listaItemDesejos) {
        this.listaDesejosId = listaDesejosId;
        this.usuario = usuario;
        this.listaItemDesejos = listaItemDesejos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<ListaItemDesejo> getListaItemDesejos() {
        return listaItemDesejos;
    }

    public void setListaItemDesejos(Set<ListaItemDesejo> listaItemDesejos) {
        this.listaItemDesejos = listaItemDesejos;
    }

    public Long getListaDesejosId() {
        return listaDesejosId;
    }

    public void setListaDesejosId(Long listaDesejosId) {
        this.listaDesejosId = listaDesejosId;
    }

    public void adicionarItem(ListaItemDesejo item) {
        // 1. Adiciona o item na lista do Java
        this.listaItemDesejos.add(item);

        // 2. Faz o vínculo bidirecional (diz para o item quem é a lista dele)
        item.setListaDesejos(this);
    }

    public void removerItem(ListaItemDesejo item) {
        // 1. Remove da lista
        this.listaItemDesejos.remove(item);

        // 2. Desfaz o vínculo
        item.setListaDesejos(null);
    }
}
