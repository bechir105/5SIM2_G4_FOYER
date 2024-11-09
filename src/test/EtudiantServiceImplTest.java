package tn.esprit.spring.Services.Etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {

    @Moc
    private EtudiantRepository repo;

    @InjectMocks
    private EtudiantService service;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("John")
                .prenomEt("Doe")
                .cin(12345678L)
                .ecole("ESPRIT")
                .build();
    }

    @Test
    void addOrUpdate_shouldReturnSavedEtudiant() {
        when(repo.save(any(Etudiant.class))).thenReturn(etudiant);
        Etudiant savedEtudiant = service.addOrUpdate(etudiant);
        assertNotNull(savedEtudiant);
        assertEquals(etudiant.getNomEt(), savedEtudiant.getNomEt());
        verify(repo, times(1)).save(etudiant);
    }

    @Test
    void findAll_shouldReturnListOfEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);
        when(repo.findAll()).thenReturn(etudiants);

        List<Etudiant> result = service.findAll();
        assertEquals(1, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnEtudiant_whenIdExists() {
        when(repo.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = service.findById(1L);
        assertNotNull(foundEtudiant);
        assertEquals(etudiant.getIdEtudiant(), foundEtudiant.getIdEtudiant());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(1L));
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepositoryDeleteById() {
        service.deleteById(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        service.delete(etudiant);
        verify(repo, times(1)).delete(etudiant);
    }
}
