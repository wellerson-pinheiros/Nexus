package nexus.com.br.game_store.controller;

import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers

public class UsuarioLogadoTest {


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 3. Este método roda ANTES de cada @Test
    @BeforeEach
    void setUp() {
        // Limpa a tabela para garantir que um teste não interfira no outro
        usuarioRepository.deleteAll();

        // Cria o usuário de teste
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@nexus.com.br");
        usuario.setNome("Admin Nexus");

        // Criptografa a senha antes de salvar no banco!
        usuario.setSenha(passwordEncoder.encode("Senha123@"));

        // Se a sua entidade exigir outros campos obrigatórios (nome, roles, etc), preencha aqui
        // usuario.setNome("Admin Nexus");

        // Salva no banco de dados do Testcontainers
        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve retornar 200 e o Token JWT quando as credenciais forem válidas")
    void deveRetornarStatusOkQuandoUsuarioLogadoComSucesso() throws Exception {

        String jsonLogin = """
                {
                    "email": "admin@nexus.com.br",
                    "senha": "Senha123@"
                }
                """;

        mockMvc.perform(post("/login") // Troque para a sua rota exata, ex: /auth/login
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                // Espera que o status seja 200 OK
                .andExpect(status().isOk())
                // Espera que o corpo da resposta tenha um campo "token"
                // (Mude "token" para o nome do campo que sua API devolve)
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());

    }

    @Test
    @DisplayName("Deve retornar 403 para credenciais inválidas como senha")
        void deveRetornarCodigo403ComCredenciasInvalidas() throws Exception {

        String jsonLogin = """
                {
                    "email": "admin@nexus.com.br",
                    "senha": "Senha13@"
                }
                """;

        mockMvc.perform(post("/login") // Troque para a sua rota exata, ex: /auth/login
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                // Espera que o status seja 200 OK
                // Espera que o corpo da resposta tenha um campo "token"
                // (Mude "token" para o nome do campo que sua API devolve)
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Erro de validação caso esteja null email ou senha")
    void deveRetornarCodigo403ComCredenciasvazia() throws Exception {

        String jsonLogin = """
                {
                    "email": "",
                    "senha": "Senha13@"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                // Espera que o status seja 200 OK
                // Espera que o corpo da resposta tenha um campo "token"
                // (Mude "token" para o nome do campo que sua API devolve)

                .andExpect(status().is(400));;
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar logar com um e-mail que não existe")
    void deveRetornarErroQuandoUsuarioNaoExiste() throws Exception {
        String jsonLogin = """
                {
                    "email": "fantasma@nexus.com.br",
                    "senha": "Senha123@"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isForbidden()); // Ajuste para 401 se sua API retornar Unauthorized
    }

    @Test
    @DisplayName("Deve retornar erro de validação quando o formato do e-mail for inválido")
    void deveRetornarErroQuandoFormatoEmailInvalido() throws Exception {
        String jsonLogin = """
                {
                    "email": "email-invalido-sem-formato",
                    "senha": "Senha123@"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isBadRequest()); // Espera 400 por conta do @Email no DTO
    }

}
