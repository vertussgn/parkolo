package com.example.parkolo.EntityTests;

import com.example.parkolo.controller.ViewController;
import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.service.ParkingSpotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ViewController.class)
class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingSpotService parkingSpotService;

    @Test
    void indexShouldReturnIndexView() throws Exception { // <-- Névkorrekció
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void statusShouldReturnStatusViewWithParkingSpots() throws Exception { // <-- Névkorrekció
        // Arrange
        ParkingSpot spot1 = new ParkingSpot(1);
        ParkingSpot spot2 = new ParkingSpot(2);
        List<ParkingSpot> mockSpots = Arrays.asList(spot1, spot2);

        when(parkingSpotService.getAllParkingSpots()).thenReturn(mockSpots);

        // Act & Assert
        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(view().name("status"))
                .andExpect(model().attributeExists("parkingSpots"))
                .andExpect(model().attribute("parkingSpots", mockSpots));
    }

    @Test
    void addCarShouldReturnAddCarViewWithCounts() throws Exception { // <-- Névkorrekció
        // Arrange
        ParkingSpot occupiedSpot = new ParkingSpot(1);
        occupiedSpot.setOccupied(true);

        ParkingSpot freeSpot = new ParkingSpot(2);
        List<ParkingSpot> mockSpots = Arrays.asList(occupiedSpot, freeSpot);

        when(parkingSpotService.getAllParkingSpots()).thenReturn(mockSpots);

        // Act & Assert
        mockMvc.perform(get("/addcar"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-car"))
                .andExpect(model().attributeExists("parkingSpots", "freeCount", "occupiedCount", "totalCount"))
                .andExpect(model().attribute("freeCount", 1L))
                .andExpect(model().attribute("occupiedCount", 1L))
                .andExpect(model().attribute("totalCount", 2L));
    }

    @Test
    void addCarShouldHandleEmptyParkingSpots() throws Exception { // <-- Névkorrekció
        // Arrange
        List<ParkingSpot> emptySpots = List.of();
        when(parkingSpotService.getAllParkingSpots()).thenReturn(emptySpots);

        // Act & Assert
        mockMvc.perform(get("/addcar"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("freeCount", 0L))
                .andExpect(model().attribute("occupiedCount", 0L))
                .andExpect(model().attribute("totalCount", 0L));
    }
}