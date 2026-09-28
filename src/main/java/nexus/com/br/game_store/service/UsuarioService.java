package nexus.com.br.game_store.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.dto.AlterarSenhaDTO;
import nexus.com.br.game_store.dto.UsuarioCadastroDTO;
import nexus.com.br.game_store.dto.UsuarioPerfilUpdateDTO;
import nexus.com.br.game_store.dto.UsuarioResponseDTO;
import nexus.com.br.game_store.excecoes.ResourceAlreadyRegistered;
import nexus.com.br.game_store.excecoes.ResourceNotFoundException;
import nexus.com.br.game_store.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

}
