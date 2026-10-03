package nexus.com.br.game_store.domain;

public enum StatusImportacao {
    EM_ANDAMENTO,         // A importação está ativa e processando paginas normalmente
    PAUSADO_RATE_LIMIT,   // A RAWG retornou HTTP 429; aguardando liberação da cota
    CONCLUIDO,            // Chegou ao fim do catálogo da RAWG (next == null)
    ERRO_AUTENTICACAO     // Chave da API inválida ou negada (HTTP 401/403)
}