package nexus.com.br.game_store.excecoes;


public class GeolocalizacaoException extends RuntimeException {
    public GeolocalizacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}