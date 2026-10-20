package nexus.com.br.game_store.excecoes;

public class EmailEnvioException extends RuntimeException {
    public EmailEnvioException(String message, Throwable cause) {
        super(message, cause);
    }
}
