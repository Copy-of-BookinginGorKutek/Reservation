package com.b2.reservation.controller;
import com.b2.reservation.Util;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.request.OperasionalLapanganRequest;
import com.b2.reservation.service.LapanganServiceImpl;
import com.b2.reservation.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.b2.reservation.model.lapangan.Lapangan;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = LapanganController.class)
@AutoConfigureMockMvc(addFilters = false)
class LapanganControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private LapanganServiceImpl service;

    @MockBean
    private JwtUtils utils;

    Lapangan lapangan;

    OperasionalLapangan operasionalLapangan;

    Object bodyContentLapangan;
    Object bodyContentOperasionalLapangan;

    @BeforeEach
    void setUp() {
        Date date = new Date();

        lapangan = Lapangan.builder()
                .id(0)
                .build();

        operasionalLapangan = OperasionalLapangan.builder()
                .idLapangan(0)
                .tanggalLibur(date)
                .build();

        bodyContentLapangan = new Object() {
            public final Integer id = 0;


        };
        bodyContentOperasionalLapangan = new Object() {
            public final Integer idLapangan = 0;
            public final Date tanggalLibur = date;

        };

    }

    @Test
    void testCreateLapangan() throws Exception {
        when(service.create()).thenReturn(lapangan);

        mvc.perform(post("/api/v1/gor/create-lapangan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContentLapangan))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createLapangan"))
                .andExpect(jsonPath("$.id").value(String.valueOf(lapangan.getId())));

        verify(service, atLeastOnce()).create();
    }

    @Test
    void testCreateCloseDate() throws Exception {
        when(service.createCloseDate(any(OperasionalLapanganRequest.class), anyString())).thenReturn(operasionalLapangan);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth("controllertokentesting");
        mvc.perform(post("/api/v1/gor/close-lapangan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContentOperasionalLapangan))
                        .headers(requestHeaders)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createCloseDate"))
                .andExpect(jsonPath("$.idLapangan").value(operasionalLapangan.getIdLapangan()));

        verify(service, atLeastOnce()).createCloseDate(any(OperasionalLapanganRequest.class), anyString());
    }

    @Test
    void testFindClosedLapangan() throws Exception{
        when(service.getAllClosedLapangan()).thenReturn(List.of(operasionalLapangan));

        mvc.perform(get("/api/v1/gor/closed-lapangan"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("findClosedLapangan"));

        verify(service, times(1)).getAllClosedLapangan();
    }

    @Test
    void testFindClosedLapanganByDate() throws Exception{
        when(service.getAllClosedLapanganByDate(any(Date.class))).thenReturn(null);
        mvc.perform(get("/api/v1/gor/closed-lapangan/by-date/2023-11-11"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("findClosedLapanganByDate"));

        verify(service, times(1)).getAllClosedLapanganByDate(any(Date.class));
    }

    @Test
    void testDeleteOperationalLapangan() throws Exception{
        mvc.perform(delete("/api/v1/gor/closed-lapangan/delete/1"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteOperasionalLapangan"));

        verify(service, times(1)).deleteOperasionalLapangan(1);
    }
}
