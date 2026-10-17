package nexus.com.br.game_store.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PrecoCalculator {

    public static BigDecimal calcularPrecoBase(LocalDate dataLancamento, Integer metacritic) {
        BigDecimal precoBase = new BigDecimal("99.90"); // Preço coringa padrão

        if (dataLancamento == null) {
            return precoBase;
        }

        int anoLancamento = dataLancamento.getYear();
        int anoAtual = LocalDate.now().getYear();
        int idade = anoAtual - anoLancamento;

        // 1. Regra baseada na idade (Lançamentos vs Clássicos)
        if (idade <= 1) {
            precoBase = new BigDecimal("199.90"); // Lançamento recente
        } else if (idade <= 3) {
            precoBase = new BigDecimal("149.90"); // Jogo com 2-3 anos
        } else if (idade <= 6) {
            precoBase = new BigDecimal("99.90");  // Jogo intermediário
        } else {
            precoBase = new BigDecimal("49.90");  // Jogo mais antigo / retrô
        }

        // 2. Bônus ou Redução baseada na nota do Metacritic
        if (metacritic != null) {
            if (metacritic >= 85) {
                // Jogos aclamados pela crítica ganham um acréscimo de valor
                precoBase = precoBase.add(new BigDecimal("30.00"));
            } else if (metacritic < 60) {
                // Jogos com notas baixas perdem valor de mercado
                precoBase = precoBase.subtract(new BigDecimal("20.00"));
            }
        }

        // Garante que o preço nunca fique abaixo de R$ 19,90
        BigDecimal precoMinimo = new BigDecimal("19.90");
        if (precoBase.compareTo(precoMinimo) < 0) {
            precoBase = precoMinimo;
        }

        // Arredondamento final com terminação comercial .90
        return precoBase.setScale(2, RoundingMode.HALF_UP);
    }
}
