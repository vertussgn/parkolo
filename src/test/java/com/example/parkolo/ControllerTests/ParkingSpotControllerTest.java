package com.example.parkolo.ControllerTests;

import com.example.parkolo.controller.ParkingSpotController;
import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.service.ParkingSpotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ParkingSpotController.class)
@SuppressWarnings("deprecation")
class ParkingSpotControllerTest {

    private static final long EXISTING_ID = 1L;
    private static final long NON_EXISTING_ID = 99L;
    private static final int VALID_SPOT_NUMBER = 5;
    private static final int INVALID_SPOT_NUMBER_LOW = 0;
    private static final int INVALID_SPOT_NUMBER_HIGH = 21;
    private static final String LICENSE_PLATE_ABC123 = "ABC123";
    private static final String LICENSE_PLATE_NEW123 = "NEW123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingSpotService parkingSpotService;

    @Test
    void getAllParkingSpotsReturnsAllSpots() throws Exception {
        ParkingSpot spot1 = createTestSpot(EXISTING_ID, 1, LICENSE_PLATE_ABC123);
        ParkingSpot spot2 = createTestSpot(2L, 2, "DEF456");
        List<ParkingSpot> spots = Arrays.asList(spot1, spot2);

        when(parkingSpotService.getAllParkingSpots()).thenReturn(spots);

        mockMvc.perform(get("/api/spots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(EXISTING_ID))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getParkingSpotByIdWhenExistsReturnsSpot() throws Exception {
        ParkingSpot spot = createTestSpot(EXISTING_ID, 1, LICENSE_PLATE_ABC123);
        when(parkingSpotService.getParkingSpotById(EXISTING_ID)).thenReturn(Optional.of(spot));

        mockMvc.perform(get("/api/spots/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_ID))
                .andExpect(jsonPath("$.licensePlate").value(LICENSE_PLATE_ABC123));
    }

    @Test
    void getParkingSpotByIdWhenNotExistsReturnsNotFound() throws Exception {
        when(parkingSpotService.getParkingSpotById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/spots/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addCarValidRequestReturnsCreatedSpot() throws Exception {
        ParkingSpot newSpot = createTestSpot(EXISTING_ID, VALID_SPOT_NUMBER, LICENSE_PLATE_NEW123);
        when(parkingSpotService.addCar(VALID_SPOT_NUMBER, LICENSE_PLATE_NEW123, null, null))
                .thenReturn(newSpot);

        mockMvc.perform(post("/api/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"spotNumber\":5, \"licensePlate\":\"NEW123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spotNumber").value(VALID_SPOT_NUMBER))
                .andExpect(jsonPath("$.licensePlate").value(LICENSE_PLATE_NEW123));
    }

    @Test
    void addCarInvalidSpotNumberReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"spotNumber\":0, \"licensePlate\":\"ABC123\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"spotNumber\":21, \"licensePlate\":\"ABC123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCarMissingLicensePlateReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"spotNumber\":5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteParkingSpotExistingIdReturnsOk() throws Exception {
        doNothing().when(parkingSpotService).deleteParkingSpot(EXISTING_ID);

        mockMvc.perform(delete("/api/spots/1"))
                .andExpect(status().isOk());

        verify(parkingSpotService, times(1)).deleteParkingSpot(EXISTING_ID);
    }

    @Test
    void deleteParkingSpotNonExistingIdReturnsNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Spot not found"))
                .when(parkingSpotService).deleteParkingSpot(NON_EXISTING_ID);

        mockMvc.perform(delete("/api/spots/99"))
                .andExpect(status().isNotFound());
    }

    private ParkingSpot createTestSpot(Long id, int spotNumber, String licensePlate) {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(id);
        spot.setSpotNumber(spotNumber);
        spot.setLicensePlate(licensePlate);
        return spot;
    }
}