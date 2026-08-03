package nexus.com.br.game_store.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;

import nexus.com.br.game_store.dto.LocalizacaoDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.InetAddress;

@Service
public class LocalizacaoService {

    private DatabaseReader dbReader;

    // Carrega o arquivo .mmdb na inicialização da aplicação
    @PostConstruct
    public void init() throws Exception {
        InputStream database = new ClassPathResource("GeoLite2-City.mmdb").getInputStream();
        dbReader = new DatabaseReader.Builder(database).build();
    }

    public LocalizacaoDTO obterLocalizacaoPorIp(String ip) {
        try {
            // Ignora IPs locais/de desenvolvimento
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                return new LocalizacaoDTO("Localhost", "Localhost", "Localhost");
            }

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            String pais = response.getCountry().getName();
            String estado = response.getMostSpecificSubdivision().getName();
            String cidade = response.getCity().getName();

            return new LocalizacaoDTO(pais, estado, cidade);

        } catch (Exception e) {
            // Caso o IP não seja encontrado ou ocorra um erro, retorna valores padrão
            return new LocalizacaoDTO("Desconhecido", "Desconhecido", "Desconhecido");
        }
    }


}

