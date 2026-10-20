package nexus.com.br.game_store.excecoes;

public class TokenJwtException extends RuntimeException {
    public TokenJwtException(String message) {
        super(message);
    }
    public TokenJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
