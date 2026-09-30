package nexus.com.br.game_store.service;

import jakarta.persistence.EntityNotFoundException;
import nexus.com.br.game_store.domain.TokenResetSenha;
import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.dto.AlterarSenhaDTO;
import nexus.com.br.game_store.dto.UsuarioCadastroDTO;
import nexus.com.br.game_store.dto.UsuarioPerfilUpdateDTO;
import nexus.com.br.game_store.dto.UsuarioResponseDTO;
import nexus.com.br.game_store.excecoes.ResourceAlreadyRegistered;
import nexus.com.br.game_store.excecoes.ResourceNotFoundException;
import nexus.com.br.game_store.repository.TokenResetSenhaRepository;
import nexus.com.br.game_store.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Mock
    private TokenResetSenhaRepository tokenResetSenhaRepository;

    @Mock
    private EmailService emailService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Wellerson Santos");
        usuario.setEmail("wellerson@nexus.com");
        usuario.setSenha("encoded_password");
        usuario.setFotoPerfil("https://avatar.png");
    }

    @Nested
    @DisplayName("Testes de Busca de Usuários")
    class BuscaTestes {

        @Test
        @DisplayName("Deve retornar lista de DTOs ao buscar todos os usuários")
        void buscarTodos_DeveRetornarListaDeUsuariosResponseDTO() {
            when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

            List<UsuarioResponseDTO> resultado = usuarioService.buscarTodos();

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(usuario.getNome(), resultado.get(0).nome());
            verify(usuarioRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("findById - Deve retornar UsuarioResponseDTO quando o ID existir")
        void findById_DeveRetornarDTO_QuandoIdExiste() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            UsuarioResponseDTO resultado = usuarioService.findById(1L);

            assertNotNull(resultado);
            assertEquals(usuario.getId(), resultado.id());
            verify(usuarioRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("findById - Deve lançar ResourceNotFoundException quando o ID não existir")
        void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> usuarioService.findById(99L)
            );

            assertEquals("Usuário não encontrado", exception.getMessage());
            verify(usuarioRepository, times(1)).findById(99L);
        }

        @Test
        @DisplayName("buscarPerfilPorId - Deve retornar UsuarioResponseDTO quando o ID existir")
        void buscarPerfilPorId_DeveRetornarDTO_QuandoIdExiste() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            UsuarioResponseDTO resultado = usuarioService.buscarPerfilPorId(1L);

            assertNotNull(resultado);
            assertEquals(usuario.getEmail(), resultado.email());
            verify(usuarioRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("buscarPerfilPorId - Deve lançar EntityNotFoundException quando o ID não existir")
        void buscarPerfilPorId_DeveLancarExcecao_QuandoIdNaoExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> usuarioService.buscarPerfilPorId(99L)
            );

            assertEquals("Usuário não encontrado.", exception.getMessage());
        }

        @Test
        @DisplayName("buscarPorEmail - Deve retornar UsuarioResponseDTO quando e-mail existir")
        void buscarPorEmail_DeveRetornarDTO_QuandoEmailExiste() {
            when(usuarioRepository.findByEmail("wellerson@nexus.com")).thenReturn(Optional.of(usuario));

            UsuarioResponseDTO resultado = usuarioService.buscarPorEmail("wellerson@nexus.com");

            assertNotNull(resultado);
            assertEquals("wellerson@nexus.com", resultado.email());
            verify(usuarioRepository, times(1)).findByEmail("wellerson@nexus.com");
        }

        @Test
        @DisplayName("buscarPorEmail - Deve lançar EntityNotFoundException quando e-mail não existir")
        void buscarPorEmail_DeveLancarExcecao_QuandoEmailNaoExiste() {
            when(usuarioRepository.findByEmail("inexistente@nexus.com")).thenReturn(Optional.empty());

            assertThrows(
                    EntityNotFoundException.class,
                    () -> usuarioService.buscarPorEmail("inexistente@nexus.com")
            );
        }
    }

    @Nested
    @DisplayName("Testes de Cadastro de Usuário")
    class CadastroTestes {



        @Test
        @DisplayName("Deve cadastrar usuário com sucesso e criptografar a senha")
        void cadastrarUsuario_DeveCadastrarComSucesso_QuandoEmailNaoExistir() {
            UsuarioCadastroDTO dto = new UsuarioCadastroDTO("Novo Usuário", "novo@nexus.com", "Senha@1234", "foto.jpg");

            when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(dto.senha())).thenReturn("senha_criptografada");

            // Simplificando o mock do save
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UsuarioResponseDTO resultado = usuarioService.cadastrarUsuaario(dto);

            // 1. Validações do DTO retornado
            assertNotNull(resultado);
            assertEquals("Novo Usuário", resultado.nome());

            // 2. CAPTURA O OBJETO ENVIADO PARA O BANCO E VALIDA O INTERIOR DELE
            verify(usuarioRepository).save(usuarioCaptor.capture());
            Usuario usuarioSalvoNoBanco = usuarioCaptor.getValue();

            assertEquals("senha_criptografada", usuarioSalvoNoBanco.getSenha(), "A senha salva no banco DEVE estar criptografada");
            assertEquals("novo@nexus.com", usuarioSalvoNoBanco.getEmail());
        }

        @Test
        @DisplayName("Deve lançar ResourceAlreadyRegistered quando o e-mail já estiver em uso")
        void cadastrarUsuario_DeveLancarExcecao_QuandoEmailJaExistir() {
            UsuarioCadastroDTO dto = new UsuarioCadastroDTO("Wellerson", "wellerson@nexus.com", "Senha@1234", null);

            when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.of(usuario));

            ResourceAlreadyRegistered exception = assertThrows(
                    ResourceAlreadyRegistered.class,
                    () -> usuarioService.cadastrarUsuaario(dto)
            );

            assertEquals("E-mail já cadastrado no sistema.", exception.getMessage());
            verify(usuarioRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(anyString());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Perfil")
    class AtualizacaoTestes {

        @Test
        @DisplayName("Deve atualizar o nome e a foto de perfil com sucesso")
        void atualizarPerfil_DeveAtualizarNomeEFoto_QuandoDadosValidos() {
            UsuarioPerfilUpdateDTO dto = new UsuarioPerfilUpdateDTO("Nome Atualizado", "https://nova-foto.png");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UsuarioResponseDTO resultado = usuarioService.atualizarPerfil(1L, dto);

            assertNotNull(resultado);
            assertEquals("Nome Atualizado", resultado.nome());
            assertEquals("https://nova-foto.png", resultado.fotoPerfil());
            verify(usuarioRepository, times(1)).save(usuario);
        }

        @Test
        @DisplayName("Deve manter a foto antiga se a foto do DTO for nula ou em branco")
        void atualizarPerfil_DeveManterFotoAntiga_QuandoFotoNoDtoForNulaOuBranca() {
            UsuarioPerfilUpdateDTO dto = new UsuarioPerfilUpdateDTO("Nome Novo", "   ");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UsuarioResponseDTO resultado = usuarioService.atualizarPerfil(1L, dto);

            assertEquals("Nome Novo", resultado.nome());
            assertEquals("https://avatar.png", resultado.fotoPerfil()); // Manteve a foto original
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar usuário inexistente")
        void atualizarPerfil_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
            UsuarioPerfilUpdateDTO dto = new UsuarioPerfilUpdateDTO("Nome", "foto.png");
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(
                    EntityNotFoundException.class,
                    () -> usuarioService.atualizarPerfil(99L, dto)
            );
            verify(usuarioRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Deleção de Conta")
    class DelecaoTestes {

        @Test
        @DisplayName("Deve deletar o usuário com sucesso quando o ID existir")
        void deletarPropriaConta_DeveDeletar_QuandoIdExiste() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            doNothing().when(usuarioRepository).delete(usuario);

            assertDoesNotThrow(() -> usuarioService.deletarPropriaConta(1L));

            verify(usuarioRepository, times(1)).findById(1L);
            verify(usuarioRepository, times(1)).delete(usuario);
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException ao tentar deletar usuário inexistente")
        void deletarPropriaConta_DeveLancarExcecao_QuandoIdNaoExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(
                    EntityNotFoundException.class,
                    () -> usuarioService.deletarPropriaConta(99L)
            );

            verify(usuarioRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Testes de Alteração de Senha")
    class AlteracaoSenhaTestes {

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando usuário não existir")
        void alterarSenha_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
            var dto = new AlterarSenhaDTO("senhaAntiga", "novaSenha");
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> usuarioService.alterarSenha(99L, dto)
            );

            assertEquals("Usuário não encontrado.", exception.getMessage());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando a senha atual for incorreta")
        void alterarSenha_DeveLancarExcecao_QuandoSenhaAtualIncorreta() {
            AlterarSenhaDTO dto = new AlterarSenhaDTO("senhaErrada", "novaSenha");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senhaErrada", usuario.getSenha())).thenReturn(false);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> usuarioService.alterarSenha(1L, dto)
            );

            assertEquals("A senha atual informada está incorreta.", exception.getMessage());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando nova senha for igual à atual")
        void alterarSenha_DeveLancarExcecao_QuandoNovaSenhaIgualAtual() {
            AlterarSenhaDTO dto = new AlterarSenhaDTO("senhaCorreta", "senhaCorreta");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            // Simula que a senha atual bateu
            when(passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())).thenReturn(true);
            // Simula que a nova senha também bate com o hash antigo (ou seja, são iguais)
            when(passwordEncoder.matches(dto.novaSenha(), usuario.getSenha())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> usuarioService.alterarSenha(1L, dto)
            );

            assertEquals("A nova senha não pode ser igual à senha atual.", exception.getMessage());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve alterar a senha com sucesso")
        void alterarSenha_DeveAlterarComSucesso_QuandoDadosValidos() {
            AlterarSenhaDTO dto = new AlterarSenhaDTO("senhaCorreta", "novaSenhaSegura");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            when(passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())).thenReturn(true);
            when(passwordEncoder.matches(dto.novaSenha(), usuario.getSenha())).thenReturn(false);
            when(passwordEncoder.encode(dto.novaSenha())).thenReturn("nova_senha_hash");

            assertDoesNotThrow(() -> usuarioService.alterarSenha(1L, dto));

            verify(usuarioRepository, times(1)).save(usuario);
            assertEquals("nova_senha_hash", usuario.getSenha());
        }
    }

    @Nested
    @DisplayName("Testes de Solicitação de Recuperação de Senha")
    class SolicitacaoRecuperacaoSenhaTestes {

        @Test
        @DisplayName("Deve retornar silenciosamente se o e-mail não existir (Segurança)")
        void solicitarRecuperacao_DeveRetornarSilenciosamente_QuandoEmailNaoExiste() {
            when(usuarioRepository.findByEmail("naoexiste@nexus.com")).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> usuarioService.solicitarRecuperacaoDeSenha("naoexiste@nexus.com"));

            verify(tokenResetSenhaRepository, never()).findByUsuario(any());
            verify(tokenResetSenhaRepository, never()).save(any());
            verify(emailService, never()).enviarEmail(anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("Deve criar novo token e enviar e-mail se o usuário existir")
        void solicitarRecuperacao_DeveCriarTokenEnviarEmail_QuandoUsuarioExiste() {
            when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
            when(tokenResetSenhaRepository.findByUsuario(usuario)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> usuarioService.solicitarRecuperacaoDeSenha(usuario.getEmail()));

            verify(tokenResetSenhaRepository, times(1)).save(any(nexus.com.br.game_store.domain.TokenResetSenha.class));
            verify(emailService, times(1)).enviarEmail(eq(usuario.getEmail()), contains("Recuperação de Senha"), anyString());
        }
    }

    @Nested
    @DisplayName("Testes de Redefinição de Senha")
    class RedefinicaoSenhaTestes {

        @Test
        @DisplayName("Deve lançar RuntimeException quando o token não for encontrado")
        void redefinirSenha_DeveLancarExcecao_QuandoTokenInvalido() {
            when(tokenResetSenhaRepository.findByToken("token_invalido")).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> usuarioService.redefinirSenha("token_invalido", "novaSenha")
            );

            assertEquals("Token inválido ou não encontrado.", exception.getMessage());
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException e deletar token quando estiver expirado")
        void redefinirSenha_DeveLancarExcecao_QuandoTokenExpirado() {
            nexus.com.br.game_store.domain.TokenResetSenha tokenEntidade = new nexus.com.br.game_store.domain.TokenResetSenha();
            tokenEntidade.setUsuario(usuario);
            tokenEntidade.setDataExpiracao(LocalDateTime.now().minusMinutes(5)); // Token expirado há 5 minutos

            when(tokenResetSenhaRepository.findByToken("token_expirado")).thenReturn(Optional.of(tokenEntidade));

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> usuarioService.redefinirSenha("token_expirado", "novaSenha")
            );

            assertEquals("O link de recuperação expirou. Solicite um novo.", exception.getMessage());

            // Verifica se o token vencido foi deletado do banco
            verify(tokenResetSenhaRepository, times(1)).delete(tokenEntidade);
            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve redefinir senha com sucesso quando token é válido")
        void redefinirSenha_DeveRedefinirComSucesso_QuandoTokenValido() {

            TokenResetSenha tokenEntidade = new TokenResetSenha();
            tokenEntidade.setUsuario(usuario);
            tokenEntidade.setDataExpiracao(LocalDateTime.now().plusMinutes(10));

            when(tokenResetSenhaRepository.findByToken("token_valido"))
                    .thenReturn(Optional.of(tokenEntidade));

            when(passwordEncoder.encode("novaSenha123"))
                    .thenReturn("nova_senha_hash");

            assertDoesNotThrow(() ->
                    usuarioService.redefinirSenha("token_valido", "novaSenha123")
            );

            assertEquals("nova_senha_hash", usuario.getSenha());

            assertNull(usuario.getTokenResetSenha());

            verify(tokenResetSenhaRepository, times(1))
                    .findByToken("token_valido");

            verify(emailService, times(1))
                    .enviarEmailSenhaAlterada(usuario.getEmail());

            verifyNoMoreInteractions(
                    tokenResetSenhaRepository,
                    usuarioRepository,
                    emailService,
                    passwordEncoder
            );
        }
    }
}