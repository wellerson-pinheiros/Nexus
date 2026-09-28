package nexus.com.br.game_store.controller;

import tools.jackson.databind.ObjectMapper;
import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.dto.UsuarioCadastroDTO;
import nexus.com.br.game_store.dto.UsuarioPerfilUpdateDTO;
import nexus.com.br.game_store.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

// Import corrigido para usar "authentication"

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UsuarioControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioSalvoNoBanco;

    // Variável para guardar o Token de teste
    private UsernamePasswordAuthenticationToken tokenAutenticacao;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario u = new Usuario();
        u.setNome("Wellerson");
        u.setEmail("wellerson@nexus.com");
        u.setSenha(passwordEncoder.encode("senha123"));
        u.setFotoPerfil("foto.png");

        usuarioSalvoNoBanco = usuarioRepository.save(u);

        // Gera o token de segurança "fake" mas validado pelo Spring
        // Como você usa uma entidade customizada, injetamos ela aqui
        tokenAutenticacao = new UsernamePasswordAuthenticationToken(
                usuarioSalvoNoBanco,
                null,
                List.of() // <- Se no futuro você adicionar Roles/Nivel de Acesso, substitua List.of() pelo getAuthorities()
        );
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário e retornar 201 Created")
    void cadastrar_DeveRetornar201_QuandoDadosValidos() throws Exception {
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO("Novo User", "novo@nexus.com", "Senha@123", null);
        String jsonRequest = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Novo User"))
                .andExpect(jsonPath("$.email").value("novo@nexus.com"));
    }

    @Test
    @DisplayName("Deve retornar a lista de usuários (200 OK)")
    void listarTodos_DeveRetornarListaE200() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .with(authentication(tokenAutenticacao)) // Adicione esta linha!
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("wellerson@nexus.com"));
    }

    @Test
    @DisplayName("Deve buscar o perfil do usuário logado via @AuthenticationPrincipal")
    void buscarMeuPerfil_DeveRetornar200_QuandoAutenticado() throws Exception {
        mockMvc.perform(get("/usuarios/me")
                        .with(authentication(tokenAutenticacao))) // O Spring injeta a classe correta na Controller
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioSalvoNoBanco.getId()))
                .andExpect(jsonPath("$.nome").value("Wellerson"));
    }



    @Test
    @DisplayName("Deve atualizar o perfil do usuário logado e refletir no banco de dados")
    void atualizarPerfil_DeveRetornar200EAtualizarNoBanco() throws Exception {
        UsuarioPerfilUpdateDTO updateDTO = new UsuarioPerfilUpdateDTO("Nome Atualizado", "foto_nova.png");
        String jsonRequest = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/usuarios/atualizar/perfil")
                        .with(authentication(tokenAutenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.fotoPerfil").value("foto_nova.png"));
    }

    @Test
    @DisplayName("Deve deletar a própria conta e retornar 204 No Content")
    void deletarPorId_DeveRetornar204() throws Exception {
        mockMvc.perform(delete("/usuarios/{id}", usuarioSalvoNoBanco.getId())
                        .with(authentication(tokenAutenticacao)))
                .andExpect(status().isNoContent());

        // Confirma remoção via Hard Delete
        boolean existe = usuarioRepository.findById(usuarioSalvoNoBanco.getId()).isPresent();
        assert(!existe);
    }
}