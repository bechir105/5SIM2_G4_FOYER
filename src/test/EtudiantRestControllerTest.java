package tn.esprit.spring.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class EtudiantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IEtudiantService etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("John");
        etudiant.setPrenom("Doe");
        // Set other fields as needed
    }

    @Test
    void testAddOrUpdate() throws Exception {
        // Mock the service layer
        when(etudiantService.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);

        // Perform POST request to add or update
        mockMvc.perform(MockMvcRequestBuilders.post("/etudiant/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(etudiant))) // Convert Etudiant to JSON
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prenom").value("Doe"));

        // Verify that the service method was called
        verify(etudiantService, times(1)).addOrUpdate(any(Etudiant.class));
    }

    @Test
    void testFindAll() throws Exception {
        List<Etudiant> etudiants = Arrays.asList(etudiant);
        when(etudiantService.findAll()).thenReturn(etudiants);

        // Perform GET request to fetch all Etudiants
        mockMvc.perform(MockMvcRequestBuilders.get("/etudiant/findAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prenom").value("Doe"));

        // Verify that the service method was called
        verify(etudiantService, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception {
        when(etudiantService.findById(1L)).thenReturn(etudiant);

        // Perform GET request to fetch Etudiant by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/etudiant/findById")
                        .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prenom").value("Doe"));

        // Verify that the service method was called
        verify(etudiantService, times(1)).findById(1L);
    }

    @Test
    void testDelete() throws Exception {
        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/etudiant/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(etudiant)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the service method was called
        verify(etudiantService, times(1)).delete(etudiant);
    }

    @Test
    void testDeleteById() throws Exception {
        // Perform DELETE request by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/etudiant/deleteById")
                        .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the service method was called
        verify(etudiantService, times(1)).deleteById(1L);
    }

    // Helper method to convert an object to JSON
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
