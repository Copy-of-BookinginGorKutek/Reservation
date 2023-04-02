package com.b2.reservation.controller;
import com.b2.reservation.Util;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.service.ReservasiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@WebMvcTest(controllers = ReservationController.class)
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservasiServiceImpl service;
    Reservasi reservasi;
    Object bodyContent;

    @BeforeEach
    void setUp() {
        reservasi = Reservasi.builder()
                .id(1)
                .emailUser("test@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .build();
        bodyContent = new Object() {
            public final Integer id = 1;
            public final String emailUser = "test@email.com";
            public final StatusPembayaran statusPembayaran= StatusPembayaran.MENUNGGU_PEMBAYARAN;
            public final String buktiTransfer = null;
            public final String tambahanList=null;
        };

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllReservation() throws Exception {
        List<Reservasi> allReservation = List.of(reservasi);

        when(service.findAll()).thenReturn(allReservation);

        mvc.perform(get("/reservation/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllReservation"))
                .andExpect(jsonPath("$[0].id").value(reservasi.getId()));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateOrder() throws Exception {
        when(service.create(any(ReservasiRequest.class))).thenReturn(reservasi);

        mvc.perform(post("/reservation/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createReservation"))
                .andExpect(jsonPath("$.id").value(String.valueOf(reservasi.getId())));

        verify(service, atLeastOnce()).create(any(ReservasiRequest.class));
    }


   @Test
    @WithMockUser(roles = "USER")
    void testGetReservationByEmail() throws Exception {
        when(service.findAllByEmailUser(any(String.class))).thenReturn(List.of(reservasi));

        mvc.perform(get("/reservation/get-self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllReservationByUser"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(reservasi.getId())))
                .andExpect(jsonPath("$[0].emailUser").value(reservasi.getEmailUser()));

        verify(service, atLeastOnce()).findAllByEmailUser(any(String.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMedicine() throws Exception {
        mvc.perform(delete("/reservation/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteReservation"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }
}

