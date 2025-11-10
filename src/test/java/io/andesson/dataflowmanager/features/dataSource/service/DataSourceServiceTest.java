package io.andesson.dataflowmanager.features.dataSource.service;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Rótulos (Anotações) de Teste
// Importa @Test, @BeforeEach, @AfterEach, @DisplayName, @Tag
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// Rótulos (Anotações) do Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.andesson.dataflowmanager.config.exceptions.ResourceNotFoundException;
import io.andesson.dataflowmanager.features.dataSource.domain.dto.request.DataSourceRequestDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.dto.response.DataSourceResponseDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;
import io.andesson.dataflowmanager.features.dataSource.domain.repository.IDataSourceRepository;
import io.andesson.dataflowmanager.features.dataSource.domain.service.DataSourceService;

@ExtendWith(MockitoExtension.class) // (Rótulo 1: Inicia o Mockito)
@Tag("service") // (Rótulo 2: Etiqueta o teste)
class DataSourceServiceTest {

    @Mock
    private IDataSourceRepository dataSourceRepository;

    @InjectMocks
    private DataSourceService dataSourceService;

    // --- Variáveis de setup ---
    private UUID existingId;
    private UUID nonExistingId;
    private DataSource sampleDataSource;
    private DataSourceRequestDTO createRequestDTO;

    @BeforeEach // (Rótulo 3: Roda ANTES de cada @Test)
    void setUp() {
        existingId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        nonExistingId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        createRequestDTO = new DataSourceRequestDTO(
                "Test DB",
                DataSourceType.DATABASE,
                "jdbc:testdb"
        );
        
        sampleDataSource = new DataSource(
                existingId,
                "Test DB",
                DataSourceType.DATABASE,
                "jdbc:testdb"
        );
    }
    
    @AfterEach // ( Roda DEPOIS de cada @Test)
    void tearDown() {
        // Limpa os mocks se necessário
        reset(dataSourceRepository);
    }

    // -----------------------------------------------------------------
    // TESTES DE CRIAÇÃO (CREATE)
    // -----------------------------------------------------------------
    
    @Test // (Rótulo 4: Marca como Teste)
    @DisplayName("✅ Deve criar um novo DataSource com sucesso") // (Rótulo 5: Nome legível)
    void create_ShouldReturnNewDataSourceDTO_WhenDataIsValid() {
        // --- Given (Dado) ---
        when(dataSourceRepository.save(any(DataSource.class))).thenReturn(sampleDataSource);

        // --- When (Quando) ---
        DataSourceResponseDTO result = dataSourceService.create(createRequestDTO);

        // --- Then (Então) ---
        
        // (Assert 1: assertNotNull)
        // Garante que o serviço não retornou um objeto nulo.
        assertNotNull(result, "O resultado não deveria ser nulo");
        
        // (Assert 2: assertAll)
        // Agrupa verificações. Se 'id' falhar, ele ainda checa 'name' e 'type'.
        assertAll("Propriedades do DataSource criado",
            
            // (Assert 3: assertEquals)
            // Checa se o valor esperado (esquerda) é igual ao valor real (direita).
            () -> assertEquals(existingId, result.id(), "O ID deve ser o mesmo retornado pelo repo"),
            () -> assertEquals("Test", result.name(), "O nome deve ser o do DTO"),
            () -> assertEquals(DataSourceType.DATABASE, result.type(), "O tipo deve ser o do DTO")
        );
        
        // Verifica se a interação com o mock (repositório) aconteceu
        verify(dataSourceRepository, times(1)).save(any(DataSource.class));
    }

    // -----------------------------------------------------------------
    // TESTES DE LEITURA (READ)
    // -----------------------------------------------------------------

    @Test
    @DisplayName("ℹ️ Deve encontrar um DataSource pelo ID com sucesso")
    void findById_ShouldReturnDataSourceDTO_WhenIdExists() {
        // --- Given ---
        when(dataSourceRepository.findById(existingId)).thenReturn(Optional.of(sampleDataSource));

        // --- When ---
        DataSourceResponseDTO result = dataSourceService.findById(existingId);

        // --- Then ---
        assertNotNull(result);
        assertEquals(existingId, result.id());
        verify(dataSourceRepository, times(1)).findById(existingId);
    }

    @Test
    @DisplayName("❌ Deve lançar ResourceNotFoundException ao buscar ID que não existe")
    void findById_ShouldThrowResourceNotFound_WhenIdDoesNotExist() {
        // --- Given ---
        when(dataSourceRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // --- When & Then ---
        
        // (Assert 4: assertThrows)
        // Verifica se a execução do lambda (o '() -> ...')
        // lança exatamente a exceção ResourceNotFoundException.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, 
                () -> {
                    // Este é o código que deve falhar
                    dataSourceService.findById(nonExistingId); 
                },
                "Deveria ter lançado ResourceNotFoundException"
        );
        
        assertEquals("DataSource não encontrado com o ID: " + nonExistingId, exception.getMessage());
        verify(dataSourceRepository, times(1)).findById(nonExistingId);
    }
    
    // -----------------------------------------------------------------
    // TESTES DE LISTAGEM (READ ALL)
    // -----------------------------------------------------------------

    @Test
    @DisplayName("📋 Deve retornar uma lista de DataSources")
    void findAll_ShouldReturnDataSourceDTOList_WhenDataSourcesExist() {
        // --- Given ---
        when(dataSourceRepository.findAll()).thenReturn(List.of(sampleDataSource));

        // --- When ---
        List<DataSourceResponseDTO> results = dataSourceService.findAll(null);

        // --- Then ---
        assertNotNull(results);
        
        // (Assert Bônus: assertFalse)
        // Verifica se a condição é falsa. (A lista NÃO está vazia)
        assertFalse(results.isEmpty(), "A lista não deveria estar vazia");
        
        assertEquals(1, results.size());
        verify(dataSourceRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("📭 Deve retornar lista vazia quando não há DataSources")
    void findAll_ShouldReturnEmptyList_WhenNoDataSourcesExist() {
        // --- Given ---
        when(dataSourceRepository.findAll()).thenReturn(List.of()); // Retorna lista vazia

        // --- When ---
        List<DataSourceResponseDTO> results = dataSourceService.findAll(null);

        // --- Then ---
        assertNotNull(results);
        
        // (Assert 5: assertTrue)
        // Verifica se a condição é verdadeira. (A lista ESTÁ vazia)
        assertTrue(results.isEmpty(), "A lista deveria estar vazia");
        
        verify(dataSourceRepository, times(1)).findAll();
    }

    // -----------------------------------------------------------------
    // TESTES DE EXCLUSÃO (DELETE)
    // -----------------------------------------------------------------
    
    @Test
    @DisplayName("🗑️ Deve deletar um DataSource com sucesso")
    void delete_ShouldCompleteSuccessfully_WhenIdExists() {
        // --- Given ---
        when(dataSourceRepository.findById(existingId)).thenReturn(Optional.of(sampleDataSource));
        doNothing().when(dataSourceRepository).delete(sampleDataSource);
        
        // --- When & Then ---
    
        assertDoesNotThrow(
                () -> {
                    dataSourceService.delete(existingId);
                },
                "Deletar um ID existente não deveria lançar exceção"
        );
        
        // Verifica se o 'delete' foi chamado
        verify(dataSourceRepository, times(1)).findById(existingId);
        verify(dataSourceRepository, times(1)).delete(sampleDataSource);
    }
}