package nexus.com.br.game_store.excecoes;

public class ResourceAlreadyRegistered extends RuntimeException {
    public ResourceAlreadyRegistered(String message) {
        super(message);
    }
}
