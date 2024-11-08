package tn.esprit.spring.Services.Universite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setNomUniversite("Esprit");

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.addOrUpdate(universite);

        assertNotNull(result);
        assertEquals("Esprit", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testFindAll() {
        List<Universite> universiteList = new ArrayList<>();
        universiteList.add(new Universite());

        when(universiteRepository.findAll()).thenReturn(universiteList);

        List<Universite> result = universiteService.findAll();

        assertEquals(1, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        long id = 1L;
        doNothing().when(universiteRepository).deleteById(id);

        universiteService.deleteById(id);

        verify(universiteRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);

        doNothing().when(universiteRepository).delete(universite);

        universiteService.delete(universite);

        verify(universiteRepository, times(1)).delete(universite);
    }
}
