import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdateReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        assertNotNull(savedReservation);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindById() {
        String reservationId = "2023/2024-Bloc A-1-123456789";
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById(reservationId);

        assertNotNull(foundReservation);
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testDeleteById() {
        String reservationId = "2023/2024-Bloc A-1-123456789";

        reservationService.deleteById(reservationId);

        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() {
        Long numChambre = 1L;
        long cin = 123456789L;

        // Create a Bloc object and initialize it
        Bloc mockBloc = new Bloc();
        mockBloc.setNomBloc("Bloc A");

        // Create and set up the Chambre object
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(numChambre);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setBloc(mockBloc); // Assign the Bloc to the Chambre

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(cin);

        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(chambre);
        when(etudiantRepository.findByCin(cin)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(anyLong(), any(), any())).thenReturn(0);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        assertNotNull(reservation);
        // Ensure that the reservation ID is generated correctly
        assertEquals("2024/2025-Bloc A-1-" + cin, reservation.getIdReservation()); // Updated expected format

        assertTrue(reservation.isEstValide());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(chambreRepository, times(1)).save(any(Chambre.class));
    }



    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 123456789L;
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1-Bloc A-123456789");
        reservation.setEstValide(false);

        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        when(reservationRepository.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        String result = reservationService.annulerReservation(cinEtudiant);

        assertEquals("La réservation " + reservation.getIdReservation() + " est annulée avec succés", result);
        assertFalse(reservation.isEstValide());
        verify(reservationRepository, times(1)).delete(reservation);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testAffectReservationAChambre() {
        String idRes = "2023/2024-Bloc A-1-123456789";
        long idChambre = 1L;

        Reservation reservation = new Reservation();
        Chambre chambre = new Chambre();
        chambre.setIdChambre(idChambre);

        when(reservationRepository.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        reservationService.affectReservationAChambre(idRes, idChambre);

        verify(chambreRepository, times(1)).save(chambre);
        assertTrue(chambre.getReservations().contains(reservation));
    }
}
