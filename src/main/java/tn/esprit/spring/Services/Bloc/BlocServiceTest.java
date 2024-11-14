package tn.esprit.spring.Services.Bloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");

        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addOrUpdate(bloc);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testFindAll() {
        List<Bloc> blocList = new ArrayList<>();
        blocList.add(new Bloc());

        when(blocRepository.findAll()).thenReturn(blocList);

        List<Bloc> result = blocService.findAll();

        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        long id = 1L;

        doNothing().when(blocRepository).deleteById(id);

        blocService.deleteById(id);

        verify(blocRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        doNothing().when(blocRepository).delete(bloc);

        blocService.delete(bloc);

        verify(blocRepository, times(1)).delete(bloc);
    }



}
