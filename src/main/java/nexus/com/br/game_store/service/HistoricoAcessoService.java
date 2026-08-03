package nexus.com.br.game_store.service;

import jakarta.servlet.http.HttpServletRequest;
import nexus.com.br.game_store.domain.HistoricoAcesso;
import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.repository.HistoricoAcessoRepository;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricoAcessoService {

    @Autowired
    private UserAgentAnalyzer userAgentAnalyzer;

    @Autowired
    private LocalizacaoService geoIpService; // Injetando nosso novo serviço

    @Autowired
    private HistoricoAcessoRepository historicoRepository;

    public String registrar(HttpServletRequest request, Usuario usuario) {

        String userAgentString = request.getHeader("User-Agent");
        String ipOrigem = extrairIp(request);

        UserAgent agent = userAgentAnalyzer.parse(userAgentString);

        // Buscando a geolocalização a partir do IP
        var localizacao = geoIpService.obterLocalizacaoPorIp(ipOrigem);

        HistoricoAcesso historico = new HistoricoAcesso();
        // Lembrete: se o fluxo envolver logins anônimos, garanta que o ID (como o id 100)
        // ou a referência de usuário anônimo seja tratada aqui caso 'usuario' venha nulo.
        historico.setUsuario(usuario);
        historico.setIp(ipOrigem);

        // Dados do YAUAA
        historico.setSistemaOperacional(agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION));
        historico.setNavegador(agent.getValue(UserAgent.AGENT_NAME_VERSION));
        historico.setDispositivo(agent.getValue(UserAgent.DEVICE_CLASS));

        // Dados do MaxMind GeoLite2
        historico.setPais(localizacao.pais());
        historico.setEstado(localizacao.estado());
        historico.setCidade(localizacao.cidade());
        historico.setLocalizacaoAproximada(localizacao.cidade() + ", " + localizacao.estado() + " - " + localizacao.pais());

        historicoRepository.save(historico);

        return "Histórico registrado com sucesso.";
    }

    private String extrairIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
