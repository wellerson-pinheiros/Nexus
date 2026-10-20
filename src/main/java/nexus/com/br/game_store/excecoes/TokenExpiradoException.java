package nexus.com.br.game_store.excecoes;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException(String message) {
        super(message);
    }
}
