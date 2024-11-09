package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EtudiantServiceImplTest {

    @InjectMocks
    private EtudiantService etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void testAddOrUpdateEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiant.setPrenomEt("Doe");
        etudiant.setDateNaissance(LocalDate.of(1995, 5, 15));
        etudiant.setCin(12345678L);

        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.addOrUpdate(etudiant);
        System.out.println("Added or Updated Etudiant: " + result);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getNomEt());
        assertEquals("Doe", result.getPrenomEt());
        verify(etudiantRepository).save(any(Etudiant.class));
    }

    @Test
    @Order(2)
    void testFindAllEtudiants() {
        // Arrange
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant(1L, "Alice", "Smith", 88L, "School A", LocalDate.of(1997, 4, 3), null));
        etudiants.add(new Etudiant(2L, "Bob", "Brown", 888L, "School B", LocalDate.of(1998, 8, 20), null));

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Act
        List<Etudiant> result = etudiantService.findAll();
        System.out.println("Retrieved Etudiants: " + result);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getNomEt());
        assertEquals("Brown", result.get(1).getPrenomEt());
        verify(etudiantRepository).findAll();
    }

    @Test
    @Order(3)
    void testFindById() {
        // Arrange
        Long etudiantId = 1L;
        Etudiant etudiant = new Etudiant(etudiantId, "John", "Doe", 12345678L, "School A", LocalDate.of(1995, 5, 15), null);

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));

        // Act
        Etudiant result = etudiantService.findById(etudiantId);
        System.out.println("Retrieved Etudiant: " + result);

        // Assert
        assertNotNull(result);
        assertEquals(etudiantId, result.getIdEtudiant());
        verify(etudiantRepository).findById(etudiantId);
    }

    @Test
    @Order(4)
    void testDeleteById() {
        // Arrange
        Long etudiantId = 1L;
        doNothing().when(etudiantRepository).deleteById(etudiantId);

        // Act
        etudiantService.deleteById(etudiantId);
        System.out.println("Removed Etudiant with ID: " + etudiantId);

        // Assert
        verify(etudiantRepository, times(1)).deleteById(etudiantId);
    }
}
