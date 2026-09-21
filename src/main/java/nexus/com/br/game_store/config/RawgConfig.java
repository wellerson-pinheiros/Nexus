package nexus.com.br.game_store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RawgConfig {
    @Bean
    public RestClient rawgRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.rawg.io/api")
                .build();
    }
}
