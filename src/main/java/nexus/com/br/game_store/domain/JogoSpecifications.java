package nexus.com.br.game_store.domain;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class JogoSpecifications {
    public static Specification<Jogo> comTitulo(String titulo) {
        return (root, query, cb) ->
                titulo == null || titulo.isEmpty() ? null :
                        cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
    }

    public static Specification<Jogo> comGenero(String generoNome) {
        return (root, query, cb) -> {
            if (generoNome == null || generoNome.isEmpty()) return null;
            Join<Object, Object> generos = root.join("generos", JoinType.INNER);
            return cb.equal(cb.lower(generos.get("nome")), generoNome.toLowerCase());
        };
    }

    public static Specification<Jogo> comPlataforma(String plataformaNome) {
        return (root, query, cb) -> {
            if (plataformaNome == null || plataformaNome.isEmpty()) return null;
            Join<Object, Object> plataformas = root.join("plataformas", JoinType.INNER);
            // Ajuste "nome" para o atributo real da plataforma se necessário
            return cb.equal(cb.lower(plataformas.get("nomePlataforma")), plataformaNome.toLowerCase());
        };
    }

    public static Specification<Jogo> precoMaiorOuIgual(BigDecimal precoMin) {
        return (root, query, cb) ->
                precoMin == null ? null :
                        cb.greaterThanOrEqualTo(root.get("preco"), precoMin); // Ajuste para "preco" ou "precoPromocional" quando criar
    }

    public static Specification<Jogo> precoMenorOuIgual(BigDecimal precoMax) {
        return (root, query, cb) ->
                precoMax == null ? null :
                        cb.lessThanOrEqualTo(root.get("preco"), precoMax);
    }
}
