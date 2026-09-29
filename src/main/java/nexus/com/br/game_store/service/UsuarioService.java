package nexus.com.br.game_store.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenResetSenhaRepository tokenResetSenhaRepository;

    @Autowired
    private  EmailService emailService;

    public List<UsuarioResponseDTO> buscarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> new UsuarioResponseDTO(usuario)) // Converte entidade para DTO
                .toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado"));
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional()
    public UsuarioResponseDTO buscarPerfilPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional() // vai ser liberado somente para conta Adimin
    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado.") );
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuaario(UsuarioCadastroDTO usuarioDTO) {

        if (usuarioRepository.findByEmail(usuarioDTO.email()).isPresent()) {
            throw new ResourceAlreadyRegistered("E-mail já cadastrado no sistema.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.nome());
        usuario.setEmail(usuarioDTO.email());

        // 3. Criptografa a senha ANTES de salvar
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.senha()));

        usuario.setFotoPerfil(usuarioDTO.fotoPerfil());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizarPerfil(Long usuarioId, UsuarioPerfilUpdateDTO dto) {
        // 1. Busca o usuário no banco de dados para garantir que ele existe e está anexado ao JPA
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        // 2. Atualiza apenas os campos permitidos
        usuario.setNome(dto.nome());
        if (dto.fotoPerfil() != null && !dto.fotoPerfil().isBlank()) {
            usuario.setFotoPerfil(dto.fotoPerfil());
        }

        // 3. Salva as alterações (o Spring Data JPA atualiza o registro existente)
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        // 4. Retorna o DTO com os dados atualizados
        return new UsuarioResponseDTO(usuarioAtualizado);
    }

    @Transactional
    public void deletarPropriaConta(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));


        usuarioRepository.delete(usuario);
    }

    @Transactional
    public void alterarSenha(Long usuarioId, AlterarSenhaDTO dto) {
        // 1. Busca o usuário no banco
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // 2. Verifica se a senha atual informada bate com a do banco
        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        // 3. Verifica se a nova senha é igual à antiga (opcional, mas recomendado)
        if (passwordEncoder.matches(dto.novaSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha não pode ser igual à senha atual.");
        }

        // 4. Encripta a nova senha e salva
        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void solicitarRecuperacaoDeSenha(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        // Se o usuário não existir, retornamos silenciosamente por segurança
        if (usuarioOpt.isEmpty()) {
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // 1. Reutiliza o registro no banco se já existir um token para este usuário, ou cria uma nova entidade
        TokenResetSenha tokenEntidade = tokenResetSenhaRepository.findByUsuario(usuario)
                .orElseGet(TokenResetSenha::new);

        // 2. Gera o novo token e atualiza os dados do registro
        String token = UUID.randomUUID().toString();
        tokenEntidade.setToken(token);
        tokenEntidade.setUsuario(usuario);
        tokenEntidade.setDataExpiracao(LocalDateTime.now().plusHours(15)); // Ajustado para 15 minutos para alinhar com o texto do e-mail

        // 3. Salva no banco (O Spring executará UPDATE se já existia ou INSERT se for o primeiro pedido)
        tokenResetSenhaRepository.save(tokenEntidade);

        // 4. Monta o e-mail personalizado
        String linkFrontEnd = "http://localhost:3000/redefinir-senha?token=" + token;

        String assunto = "Recuperação de Senha - Nexus Game Store";
        String mensagem = "Olá, " + usuario.getNome() + "!\n\n" +
                "Você solicitou a recuperação de sua senha.\n" +
                "Clique no link abaixo para criar uma nova senha:\n" +
                linkFrontEnd + "\n\n" +
                "Este link é válido por 15 minutos.\n" +
                "Se você não solicitou essa alteração, apenas ignore este e-mail.";

        // 5. Chama o seu EmailService para disparar
        emailService.enviarEmail(usuario.getEmail(), assunto, mensagem);
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        // Busca o token no banco de dados
        TokenResetSenha tokenEntidade = tokenResetSenhaRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido ou não encontrado."));

        // Verifica se o token já passou da validade
        if (tokenEntidade.getDataExpiracao().isBefore(LocalDateTime.now())) {
            tokenResetSenhaRepository.delete(tokenEntidade); // Apaga o token vencido para limpar o banco
            throw new RuntimeException("O link de recuperação expirou. Solicite um novo.");
        }

        // Se chegou aqui, o token é válido! Vamos pegar o usuário associado a ele
        Usuario usuario = tokenEntidade.getUsuario();

        // Criptografa a nova senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(novaSenha));

        // Salva o usuário com a senha nova
        usuarioRepository.save(usuario);

        // DELETA o token do banco para que o link não possa ser usado novamente
        tokenResetSenhaRepository.delete(tokenEntidade);

        emailService.enviarEmailSenhaAlterada(usuario.getEmail());
    }

}
