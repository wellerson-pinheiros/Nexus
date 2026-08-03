package nexus.com.br.game_store.config;


import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YauaaConfig {

    @Bean
        public UserAgentAnalyzer userAgentAnalyzer() {
            return UserAgentAnalyzer
                    .newBuilder()
                    .withCache(10000)
                    .build();
        }

}
