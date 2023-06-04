package com.b2.reservation.controller;

import com.b2.reservation.Util;
import com.b2.reservation.config.SecurityConfiguration;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.service.ReservasiServiceImpl;
import com.b2.reservation.util.JwtUtils;
import com.b2.reservation.util.LapanganDipakai;
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
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Import(SecurityConfiguration.class)
@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservasiServiceImpl service;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private JwtUtils utils;

    Lapangan lapangan;
    LapanganDipakai lapanganDipakai;
    Reservasi reservasi;
    Object bodyContent;

    String email;

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

        };

        LocalDateTime sc = LocalDateTime.of(2023,5,14,19,0,0);
        LocalDateTime ec = LocalDateTime.of(2023,5,14,21,0,0);

        email =  "test@email.com";

        lapangan = Lapangan
                .builder()
                .id(1)
                .build();
        lapanganDipakai = new LapanganDipakai(lapangan, sc, ec);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllReservation() throws Exception {
        List<Reservasi> allReservation = List.of(reservasi);

        when(service.findAll()).thenReturn(allReservation);

        mvc.perform(get("/api/v1/reservation/get-all")
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

        mvc.perform(post("/api/v1/reservation/create")
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

        mvc.perform(get("/api/v1/reservation/get-self")
                        .param("emailUser", email)
                        )
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllReservationByUser"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(reservasi.getId())))
                .andExpect(jsonPath("$[0].emailUser").value(reservasi.getEmailUser()));

        verify(service, atLeastOnce()).findAllByEmailUser(any(String.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMedicine() throws Exception {
        mvc.perform(delete("/api/v1/reservation/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteReservation"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateReservationAdmin() throws Exception {
        when(service.updateStatus(any(Integer.class), any(ReservasiRequest.class))).thenReturn(reservasi);

        mvc.perform(put("/api/v1/reservation/stat-update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateReservation"))
                .andExpect(jsonPath("$.id").value(reservasi.getId()))
                .andExpect(jsonPath("$.emailUser").value(reservasi.getEmailUser()));

        verify(service, atLeastOnce()).updateStatus(any(Integer.class), any(ReservasiRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdatePaymentProof() throws Exception {
        String linkExample = "{\"url\":\"https://drive.google.com/file/d/1tSy_MqFKbe8VoFjyy-QqAsyZh9UvAv39/view?usp=share_link\"}";

        mvc.perform(put("/api/v1/reservation/bukti-bayar/1")
                        .content(linkExample)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("putProofOfPayment"));

        verify(service, atLeastOnce()).addPaymentProof(any(Integer.class), any(String.class));
    }

    @Test
    @WithMockUser(roles="USER")
    void testGetReservationById() throws Exception{
        when(service.findById(1)).thenReturn(reservasi);
        mvc.perform(get("/api/v1/reservation/get/1"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getReservationById"));
        verify(service, times(1)).findById(1);
    }
    @Test
    @WithMockUser(roles="USER")
    void testGetReservasiByDateSuccess() throws Exception{
        when(service.findReservasiByDate(anyString())).thenReturn(List.of(reservasi));
        mvc.perform(get("/api/v1/reservation/get-reservasi-by-date/2023-05-14"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().methodName("getReservasiByDate"));
        verify(service, times(1)).findReservasiByDate(anyString());
    }

    @Test
    @WithMockUser(roles="USER")
    void testGetReservasiByDateFail() throws Exception{
        when(service.findReservasiByDate(anyString())).thenThrow(ParseException.class);
        mvc.perform(get("/api/v1/reservation/get-reservasi-by-date/2023-05-20"))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("getReservasiByDate"));
        verify(service, times(1)).findReservasiByDate(anyString());
    }

}

