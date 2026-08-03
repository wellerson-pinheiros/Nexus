package nexus.com.br.game_store.dto;

// Record interno para facilitar o transporte dos dados
public record LocalizacaoDTO(String pais, String estado, String cidade) {

    public LocalizacaoDTO(String pais, String estado, String cidade) {
        this.pais = pais;
        this.estado = estado;
        this.cidade = cidade;
    }
}