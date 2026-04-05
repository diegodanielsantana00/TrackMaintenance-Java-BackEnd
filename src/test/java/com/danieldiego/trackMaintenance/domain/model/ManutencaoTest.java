package com.danieldiego.trackMaintenance.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ManutencaoTest {

    @Nested
    @DisplayName("Validação de Custo")
    class CustoTests {

        @Test
        @DisplayName("deve lançar exceção para custo negativo")
        void deve_lancar_excecao_para_custo_negativo() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(1),
                            "Troca de Óleo", new BigDecimal("-100.00"))
            );
            assertEquals("Custo estimado não pode ser negativo", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção para custo acima do teto (1 milhão)")
        void deve_lancar_excecao_para_custo_acima_do_teto() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(1),
                            "Revisão Completa", new BigDecimal("1000001.00"))
            );
            assertEquals("Custo estimado não pode exceder R$ 1.000.000,00", ex.getMessage());
        }

        @Test
        @DisplayName("deve aceitar custo zero")
        void deve_aceitar_custo_zero() {
            assertDoesNotThrow(() ->
                    Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(1),
                            "Troca de Óleo", BigDecimal.ZERO)
            );
        }

        @Test
        @DisplayName("deve aceitar custo nulo")
        void deve_aceitar_custo_nulo() {
            assertDoesNotThrow(() ->
                    Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(1),
                            "Troca de Óleo", null)
            );
        }

        @Test
        @DisplayName("deve aceitar custo válido")
        void deve_aceitar_custo_valido() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(1),
                    "Troca de Óleo", new BigDecimal("500.00"));
            assertEquals(new BigDecimal("500.00"), m.getCustoEstimado());
        }
    }

    @Nested
    @DisplayName("Validação de Datas")
    class DatasTests {

        @Test
        @DisplayName("deve lançar exceção quando data final menor que data início")
        void deve_lancar_excecao_quando_data_final_menor() {
            LocalDate inicio = LocalDate.of(2025, 6, 15);
            LocalDate fim = LocalDate.of(2025, 6, 10);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Manutencao.create(1L, inicio, fim, "Revisão", new BigDecimal("200.00"))
            );
            assertEquals("Data de finalização não pode ser anterior à data de início", ex.getMessage());
        }

        @Test
        @DisplayName("deve aceitar data final igual à data início")
        void deve_aceitar_data_final_igual_inicio() {
            LocalDate data = LocalDate.of(2025, 6, 15);
            assertDoesNotThrow(() ->
                    Manutencao.create(1L, data, data, "Revisão", new BigDecimal("200.00"))
            );
        }

        @Test
        @DisplayName("deve aceitar data final nula")
        void deve_aceitar_data_final_nula() {
            assertDoesNotThrow(() ->
                    Manutencao.create(1L, LocalDate.now(), null, "Revisão", new BigDecimal("200.00"))
            );
        }
    }

    @Nested
    @DisplayName("Fluxo de Status")
    class StatusTests {

        @Test
        @DisplayName("deve permitir fluxo válido PENDENTE → EM_REALIZACAO → CONCLUIDA")
        void deve_permitir_fluxo_valido() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(5),
                    "Troca de Óleo", new BigDecimal("300.00"));

            assertEquals(StatusManutencao.PENDENTE, m.getStatus());

            m.atualizarStatus(StatusManutencao.EM_REALIZACAO);
            assertEquals(StatusManutencao.EM_REALIZACAO, m.getStatus());

            m.atualizarStatus(StatusManutencao.CONCLUIDA);
            assertEquals(StatusManutencao.CONCLUIDA, m.getStatus());
        }

        @Test
        @DisplayName("deve impedir pular de PENDENTE para CONCLUIDA")
        void deve_impedir_pular_para_concluida() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(5),
                    "Troca de Óleo", new BigDecimal("300.00"));

            IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                    m.atualizarStatus(StatusManutencao.CONCLUIDA)
            );
            assertTrue(ex.getMessage().contains("Transição de status inválida"));
            assertTrue(ex.getMessage().contains("PENDENTE → CONCLUIDA"));
        }

        @Test
        @DisplayName("deve impedir voltar de CONCLUIDA para qualquer status")
        void deve_impedir_voltar_de_concluida() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(5),
                    "Troca de Óleo", new BigDecimal("300.00"));
            m.atualizarStatus(StatusManutencao.EM_REALIZACAO);
            m.atualizarStatus(StatusManutencao.CONCLUIDA);

            assertThrows(IllegalStateException.class, () ->
                    m.atualizarStatus(StatusManutencao.PENDENTE)
            );
            assertThrows(IllegalStateException.class, () ->
                    m.atualizarStatus(StatusManutencao.EM_REALIZACAO)
            );
        }

        @Test
        @DisplayName("deve impedir voltar de EM_REALIZACAO para PENDENTE")
        void deve_impedir_voltar_para_pendente() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(5),
                    "Troca de Óleo", new BigDecimal("300.00"));
            m.atualizarStatus(StatusManutencao.EM_REALIZACAO);

            assertThrows(IllegalStateException.class, () ->
                    m.atualizarStatus(StatusManutencao.PENDENTE)
            );
        }

        @Test
        @DisplayName("deve permitir manter o mesmo status sem erro")
        void deve_permitir_mesmo_status() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(5),
                    "Troca de Óleo", new BigDecimal("300.00"));

            assertDoesNotThrow(() -> m.atualizarStatus(StatusManutencao.PENDENTE));
        }
    }

    @Nested
    @DisplayName("Manutenção Ativa")
    class AtivaTests {

        @Test
        @DisplayName("PENDENTE deve ser considerada ativa")
        void pendente_deve_ser_ativa() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), null, "Revisão", null);
            assertTrue(m.isAtiva());
        }

        @Test
        @DisplayName("EM_REALIZACAO deve ser considerada ativa")
        void em_realizacao_deve_ser_ativa() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), null, "Revisão", null);
            m.atualizarStatus(StatusManutencao.EM_REALIZACAO);
            assertTrue(m.isAtiva());
        }

        @Test
        @DisplayName("CONCLUIDA não deve ser considerada ativa")
        void concluida_nao_deve_ser_ativa() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), null, "Revisão", null);
            m.atualizarStatus(StatusManutencao.EM_REALIZACAO);
            m.atualizarStatus(StatusManutencao.CONCLUIDA);
            assertFalse(m.isAtiva());
        }
    }

    @Nested
    @DisplayName("Criação da Manutenção")
    class CriacaoTests {

        @Test
        @DisplayName("deve criar manutenção com status PENDENTE")
        void deve_criar_com_status_pendente() {
            Manutencao m = Manutencao.create(1L, LocalDate.now(), LocalDate.now().plusDays(3),
                    "Alinhamento", new BigDecimal("150.00"));
            assertEquals(StatusManutencao.PENDENTE, m.getStatus());
            assertEquals(1L, m.getVeiculoId());
            assertEquals("Alinhamento", m.getTipoServico());
        }
    }
}
