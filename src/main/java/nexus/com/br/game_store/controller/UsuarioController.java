package nexus.com.br.game_store.controller;

import jakarta.validation.Valid;
import nexus.com.br.game_store.domain.Usuario;
import nexus.com.br.game_store.dto.*;
import nexus.com.br.game_store.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping(value = "/{id}") // metodo somente para adiministradores do eccomerce
    public ResponseEntity <UsuarioResponseDTO> buscarpeloId(@PathVariable Long id) {
        UsuarioResponseDTO usuarioResponseDTO = service.findById(id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<UsuarioResponseDTO> buscarMeuPerfil(@AuthenticationPrincipal Usuario usuarioLogado) {
        // O Spring Security já injeta o usuário autenticado aqui automaticamente!
        // Você não precisa nem bater no banco de dados se não quiser.
        UsuarioResponseDTO dto = new UsuarioResponseDTO(usuarioLogado);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/email/{email}") // metodo somente para adiministrador do eccomerce
    public ResponseEntity <UsuarioResponseDTO> buscarPeloEmail(@PathVariable String email) {
        UsuarioResponseDTO usuarioResponseDTO = service.buscarPorEmail(email);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PostMapping("/cadastrar") // Ou apenas @PostMapping se preferir POST /api/usuarios
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @RequestBody @Valid UsuarioCadastroDTO dto,
            UriComponentsBuilder uriBuilder) {

        UsuarioResponseDTO response = service.cadastrarUsuaario(dto);

        // Gera a URL do novo recurso criado: /api/usuarios/{id}
        URI uri = uriBuilder.path("/api/usuarios/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/atualizar/perfil")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestBody @Valid UsuarioPerfilUpdateDTO dto) {

        // Passamos o ID e a Service busca do banco de dados atualizado
        UsuarioResponseDTO response = service.atualizarPerfil(usuarioLogado.getId(), dto);

        // Retorna HTTP 200 com os dados novos
        return ResponseEntity.ok(response);
    }

    // 2. EXCLUSÃO ADMINISTRATIVA (Apenas ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        // Bloqueia se não for ADMIN
        //if (usuarioLogado.getNivelConta() != NivelConta.ADMIN) {
          //  return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        //}

        service.deletarPropriaConta(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(
            @RequestBody @Valid AlterarSenhaDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        service.alterarSenha(usuarioLogado.getId(), dto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/esqueci-minha-senha")
    public ResponseEntity<String> solicitarRecuperacao(@Valid @RequestBody EsqueciMinhaSenhaDTO request) {

        // Chamaremos a service aqui:
        service.solicitarRecuperacaoDeSenha(request.email());

        // Lembre-se da regra de segurança: sempre retornar sucesso mesmo se o e-mail não existir
        return ResponseEntity.ok("Se o e-mail estiver cadastrado, um link de recuperação foi enviado.");
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {

        // Passando os dados do record para a Service
        service.redefinirSenha(request.token(), request.novaSenha());

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}
