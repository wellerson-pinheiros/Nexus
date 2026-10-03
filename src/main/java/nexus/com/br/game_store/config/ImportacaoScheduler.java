package nexus.com.br.game_store.config;

import nexus.com.br.game_store.service.RawgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ImportacaoScheduler {

        private static final Logger log = LoggerFactory.getLogger(ImportacaoScheduler.class);

        private final RawgService rawgService;

        public ImportacaoScheduler(RawgService rawgService) {
            this.rawgService = rawgService;
        }

        /**
         * Roda 10 segundos após a aplicação subir (initialDelay = 10000 ms)
         * e se repete a cada 2 horas (PT2H ou 7200000 ms).
         */
        @Scheduled(fixedDelayString = "PT2H", initialDelay = 10000)
        public void executarAgendamentoImportacao() {
            log.info("==========================================");
            log.info("Iniciando rotina agendada de importação...");
            log.info("==========================================");

            // Processa 10 páginas por execução (~400 jogos) para economizar ainda mais no ambiente de teste
            rawgService.executarLoteImportacao(10);

            log.info("==========================================");
            log.info("Rotina agendada finalizada. Próxima execução em 2 horas.");
            log.info("==========================================");
        }

}
