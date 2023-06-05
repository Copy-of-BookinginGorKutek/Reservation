package com.b2.reservation.controller;
import com.b2.reservation.Util;
import com.b2.reservation.config.SecurityConfiguration;
import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.request.KuponRequest;
import com.b2.reservation.request.OperasionalLapanganRequest;
import com.b2.reservation.service.KuponServiceImpl;
import com.b2.reservation.service.LapanganServiceImpl;
import com.b2.reservation.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

@Import(SecurityConfiguration.class)
@WebMvcTest(controllers = KuponController.class)
class KuponControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private KuponServiceImpl service;

    @MockBean
    private JwtUtils utils;

    Kupon kupon;

    Object bodyContent;

    KuponRequest createRequest;

    @BeforeEach
    void setUp() {
        kupon = Kupon.builder().id(1)
                .name("diskon1")
                .percentageDiscounted(50)
                .build();

        createRequest = KuponRequest.builder()
                .name("diskon1")
                .percentageDiscounted(50)
                .build();

        bodyContent= new Object() {
            public final Integer id = 0;
            public final String name = "diskon1";
            public final Integer percentageDiscounted = 50;


        };

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateKupon() throws Exception {
        when(service.create(createRequest)).thenReturn(kupon);

        mvc.perform(post("/api/v1/gor/create-kupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createKupon"))
                .andExpect(jsonPath("$.id").value(String.valueOf(kupon.getId())));

        verify(service, atLeastOnce()).create(createRequest);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetKuponAdmin() throws Exception {
        when(service.findAll()).thenReturn(List.of(kupon));

        mvc.perform(get("/api/v1/gor/get-all-kupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllKupon"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(kupon.getId())));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetKuponUser() throws Exception {
        when(service.findAll()).thenReturn(List.of(kupon));

        mvc.perform(get("/api/v1/gor/get-all-kupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllKupon"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(kupon.getId())));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteKupon() throws Exception {
        mvc.perform(delete("/api/v1/gor/delete-kupon/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteKuponById"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }


}
