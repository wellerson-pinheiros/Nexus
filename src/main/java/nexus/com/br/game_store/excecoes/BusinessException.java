package nexus.com.br.game_store.excecoes;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
