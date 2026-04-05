package com.danieldiego.trackMaintenance.application.service.manutencao;

import com.danieldiego.trackMaintenance.application.Interface.manutencao.ManutencaoRepositoryPort;
import com.danieldiego.trackMaintenance.application.Interface.veiculo.VeiculoRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.manutencao.CreateManutencaoCommand;
import com.danieldiego.trackMaintenance.application.dto.manutencao.ManutencaoOutput;
import com.danieldiego.trackMaintenance.application.dto.manutencao.UpdateManutencaoCommand;
import com.danieldiego.trackMaintenance.domain.model.Manutencao;
import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;
import com.danieldiego.trackMaintenance.domain.enums.TipoVeiculo;
import com.danieldiego.trackMaintenance.domain.model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManutencaoServiceImplTest {

    @Mock
    private ManutencaoRepositoryPort manutencaoRepository;

    @Mock
    private VeiculoRepositoryPort veiculoRepository;

    private ManutencaoServiceImpl service;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        service = new ManutencaoServiceImpl(manutencaoRepository, veiculoRepository);
        veiculo = Veiculo.reconstitute(1L, "ABC-1234", "Fiat Uno", TipoVeiculo.LEVE, 2020);
    }

    @Nested
    @DisplayName("Manutenção Sobreposta")
    class ManutencaoSobrepostaTests {

        @Test
        @DisplayName("deve impedir manutenção simultânea para o mesmo veículo")
        void deve_impedir_manutencao_simultanea() {
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(manutencaoRepository.existsActiveByVeiculoId(1L)).thenReturn(true);

            CreateManutencaoCommand command = new CreateManutencaoCommand(
                    1L, LocalDate.now(), LocalDate.now().plusDays(3),
                    "Troca de Óleo", new BigDecimal("300.00"));

            IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                    service.createManutencao(command)
            );
            assertTrue(ex.getMessage().contains("manutenção ativa"));
            verify(manutencaoRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve permitir criar manutenção quando não há ativa")
        void deve_permitir_quando_nao_ha_ativa() {
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(manutencaoRepository.existsActiveByVeiculoId(1L)).thenReturn(false);

            Manutencao saved = Manutencao.reconstitute(1L, 1L, LocalDate.now(),
                    LocalDate.now().plusDays(3), "Troca de Óleo",
                    new BigDecimal("300.00"), StatusManutencao.PENDENTE);
            when(manutencaoRepository.save(any())).thenReturn(saved);

            CreateManutencaoCommand command = new CreateManutencaoCommand(
                    1L, LocalDate.now(), LocalDate.now().plusDays(3),
                    "Troca de Óleo", new BigDecimal("300.00"));

            ManutencaoOutput output = service.createManutencao(command);
            assertNotNull(output);
            assertEquals(StatusManutencao.PENDENTE, output.status());
            verify(manutencaoRepository).save(any());
        }
    }

    @Nested
    @DisplayName("Veículo Bloqueado em Manutenção")
    class VeiculoBloqueadoTests {

        @Test
        @DisplayName("deve bloquear veículo em manutenção (não pode iniciar outra)")
        void deve_bloquear_veiculo_em_manutencao() {
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(manutencaoRepository.existsActiveByVeiculoId(1L)).thenReturn(true);

            CreateManutencaoCommand command = new CreateManutencaoCommand(
                    1L, LocalDate.now(), LocalDate.now().plusDays(2),
                    "Alinhamento", new BigDecimal("150.00"));

            IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                    service.createManutencao(command)
            );
            assertTrue(ex.getMessage().contains("manutenção ativa"));
        }
    }

    @Nested
    @DisplayName("Fluxo de Status via Service")
    class StatusFlowTests {

        @Test
        @DisplayName("deve permitir atualizar status no fluxo correto via service")
        void deve_permitir_fluxo_valido_via_service() {
            Manutencao pendente = Manutencao.reconstitute(1L, 1L, LocalDate.now(),
                    LocalDate.now().plusDays(5), "Troca de Óleo",
                    new BigDecimal("300.00"), StatusManutencao.PENDENTE);

            Manutencao emRealizacao = Manutencao.reconstitute(1L, 1L, LocalDate.now(),
                    LocalDate.now().plusDays(5), "Troca de Óleo",
                    new BigDecimal("300.00"), StatusManutencao.EM_REALIZACAO);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(pendente));
            when(manutencaoRepository.save(any())).thenReturn(emRealizacao);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

            ManutencaoOutput output = service.updateStatus(1L, StatusManutencao.EM_REALIZACAO);
            assertEquals(StatusManutencao.EM_REALIZACAO, output.status());
        }

        @Test
        @DisplayName("deve impedir pular de PENDENTE para CONCLUIDA via service")
        void deve_impedir_pular_status_via_service() {
            Manutencao pendente = Manutencao.reconstitute(1L, 1L, LocalDate.now(),
                    LocalDate.now().plusDays(5), "Troca de Óleo",
                    new BigDecimal("300.00"), StatusManutencao.PENDENTE);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(pendente));

            assertThrows(IllegalStateException.class, () ->
                    service.updateStatus(1L, StatusManutencao.CONCLUIDA)
            );
            verify(manutencaoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Validações no Update")
    class UpdateTests {

        @Test
        @DisplayName("deve lançar exceção para custo negativo no update")
        void deve_lancar_excecao_custo_negativo_update() {
            Manutencao existing = Manutencao.reconstitute(1L, 1L, LocalDate.now(),
                    LocalDate.now().plusDays(5), "Revisão",
                    new BigDecimal("200.00"), StatusManutencao.PENDENTE);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(existing));

            UpdateManutencaoCommand command = new UpdateManutencaoCommand(
                    LocalDate.now(), LocalDate.now().plusDays(5),
                    "Revisão", new BigDecimal("-50.00"), StatusManutencao.PENDENTE);

            assertThrows(IllegalArgumentException.class, () ->
                    service.updateManutencao(1L, command)
            );
            verify(manutencaoRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar exceção quando data final é anterior à data início no update")
        void deve_lancar_excecao_data_invalida_update() {
            Manutencao existing = Manutencao.reconstitute(1L, 1L, LocalDate.now(),
                    LocalDate.now().plusDays(5), "Revisão",
                    new BigDecimal("200.00"), StatusManutencao.PENDENTE);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(existing));

            UpdateManutencaoCommand command = new UpdateManutencaoCommand(
                    LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 10),
                    "Revisão", new BigDecimal("200.00"), StatusManutencao.PENDENTE);

            assertThrows(IllegalArgumentException.class, () ->
                    service.updateManutencao(1L, command)
            );
            verify(manutencaoRepository, never()).save(any());
        }
    }
}
