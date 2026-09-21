package nexus.com.br.game_store.service;

import nexus.com.br.game_store.dto.RawgListJogosRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RawgService {

    private final RestClient restClient;

    @Value("${secretRAWG}")
    private String apiKey;

    public RawgService(RestClient restClient) {
        this.restClient = restClient;
    }


    public RawgListJogosRequestDTO listarJogosDaApi(Integer page) {
        int paginaAtual = (page != null) ? page : 1;

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", apiKey)
                        .queryParam("page", paginaAtual)
                        .build())
                .retrieve()
                .body(RawgListJogosRequestDTO.class);
    }

    // Sobrecarga: Se chamarem sem argumentos, assume a página 1 automaticamente
    public RawgListJogosRequestDTO listarJogosDaApi() {
        return listarJogosDaApi(1);
    }
}

