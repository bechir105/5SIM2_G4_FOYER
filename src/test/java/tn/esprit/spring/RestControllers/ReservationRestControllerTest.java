package tn.esprit.spring.RestControllers;  // The same root package or a subpackage

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.Services.Reservation.IReservationService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationRestController.class)
@AutoConfigureMockMvc
class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    @Test
    void testFindAllReservations() throws Exception {
        Reservation reservation = new Reservation("2023/2024-Bloc A-1-123456789", LocalDate.now(), true, Collections.emptyList());
        when(reservationService.findAll()).thenReturn(Collections.singletonList(reservation));

        mockMvc.perform(get("/reservation/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value(reservation.getIdReservation()))
                .andExpect(jsonPath("$[0].anneeUniversitaire").value(reservation.getAnneeUniversitaire().toString()))
                .andExpect(jsonPath("$[0].estValide").value(reservation.isEstValide()));
    }

    @Test
    void testFindReservationById() throws Exception {
        Reservation reservation = new Reservation("2023/2024-Bloc A-1-123456789", LocalDate.now(), true, Collections.emptyList());
        when(reservationService.findById("2023/2024-Bloc A-1-123456789")).thenReturn(reservation);

        mockMvc.perform(get("/reservation/findById")
                        .param("id", "2023/2024-Bloc A-1-123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value(reservation.getIdReservation()))
                .andExpect(jsonPath("$.anneeUniversitaire").value(reservation.getAnneeUniversitaire().toString()))
                .andExpect(jsonPath("$.estValide").value(reservation.isEstValide()));
    }

    /*@Test
    void testGetReservationByUniversityYear() throws Exception {
        long totalReservations = 5L;
        when(reservationService.getReservationParAnneeUniversitaire(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(totalReservations);

        mockMvc.perform(get("/reservation/getByUniversityYear")
                        .param("startYear", "2024-09-01")
                        .param("endYear", "2025-05-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(totalReservations));
    }
    */
    /*@Test
    void testCancelReservation() throws Exception {
        when(reservationService.annulerReservation(12345L)).thenReturn("Reservation cancelled successfully");

        mockMvc.perform(delete("/reservation/cancel/{cin}", 12345L))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation cancelled successfully"));
    }*/

    /*@Test
    void testCancelAllReservations() throws Exception {
        doNothing().when(reservationService).annulerReservations();

        mockMvc.perform(delete("/reservation/cancelAll"))
                .andExpect(status().isOk());
    }*/
}
