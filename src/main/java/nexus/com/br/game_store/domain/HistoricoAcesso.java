package nexus.com.br.game_store.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "tb_historico_de_acesso")
public class HistoricoAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historico_acesso_id;

    @CreationTimestamp
    private Date dataHoraAcesso;


    private String ip;

    private String dispositivo;

    private String localizacaoAproximada;

    private String navegador;

    private String sistemaOperacional;

    private String cidade;

    private String estado;

    private String pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id") // Nome da coluna de chave estrangeira no banco
    private Usuario usuario;

    public HistoricoAcesso() {
    }

    public HistoricoAcesso(Long historico_acesso_id, Usuario usuario, String pais, String estado, String cidade, String sistemaOperacional, String navegador, String localizacaoAproximada, String dispositivo, String ip, Date dataHoraAcesso) {
        this.historico_acesso_id = historico_acesso_id;
        this.usuario = usuario;
        this.pais = pais;
        this.estado = estado;
        this.cidade = cidade;
        this.sistemaOperacional = sistemaOperacional;
        this.navegador = navegador;
        this.localizacaoAproximada = localizacaoAproximada;
        this.dispositivo = dispositivo;
        this.ip = ip;
        this.dataHoraAcesso = dataHoraAcesso;
    }

    public Long getHistorico_acesso_id() {
        return historico_acesso_id;
    }

    public void setHistorico_acesso_id(Long historico_acesso_id) {
        this.historico_acesso_id = historico_acesso_id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    public String getLocalizacaoAproximada() {
        return localizacaoAproximada;
    }

    public void setLocalizacaoAproximada(String localizacaoAproximada) {
        this.localizacaoAproximada = localizacaoAproximada;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDataHoraAcesso() {
        return dataHoraAcesso;
    }

    public void setDataHoraAcesso(Date dataHoraAcesso) {
        this.dataHoraAcesso = dataHoraAcesso;
    }
}
