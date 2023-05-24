package com.b2.reservation.service;

import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.repository.KuponRepository;
import com.b2.reservation.repository.LapanganRepository;
import com.b2.reservation.repository.OperasionalLapanganRepository;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.util.TambahanUtils;
import com.b2.reservation.util.TimeValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservasiServiceImplTests {

    @InjectMocks
    private ReservasiServiceImpl service;

    @Mock
    private ReservasiRepository repository;

    @Mock
    private LapanganRepository lapanganRepository;

    @Mock
    private TambahanUtils tambahanUtils;

    @Mock
    private OperasionalLapanganRepository operasionalLapanganRepository;

    @Mock
    private KuponRepository kuponRepository;

    Reservasi tempReservasi;
    Reservasi reservasi;
    Reservasi newReservasi1;
    Reservasi newReservasi2;
    ReservasiRequest createRequest;
    ReservasiRequest updateRequest;
    Map<String, Integer> tambahanQty;
    Lapangan lap1;
    Lapangan lap2;
    Integer id;

    @BeforeEach
    void setUp() {
        lap1 = new Lapangan();
        lap1.setId(1);
        lap2 = new Lapangan();
        lap2.setId(2);
        lapanganRepository.save(lap1);
        lapanganRepository.save(lap2);

        createRequest = ReservasiRequest.builder()
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .waktuMulai("14-05-2023 19:00")
                .waktuBerakhir("14-05-2023 21:00")
                .build();

        updateRequest = ReservasiRequest.builder()
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
                .build();

        LocalDateTime sc = LocalDateTime.of(2023,5,14,19,0,0);
        LocalDateTime ec = LocalDateTime.of(2023,5,14,21,0,0);

        reservasi = Reservasi.builder()
                .id(0)
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .idLapangan(lap1.getId())
                .waktuMulai(sc)
                .waktuBerakhir(ec)
                .build();

        newReservasi1 = Reservasi.builder()
                .id(0)
                .emailUser("test1@email.com")
                .idLapangan(lap1.getId())
                .waktuMulai(sc)
                .waktuBerakhir(ec)
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
                .build();

        newReservasi2 = Reservasi.builder()
                .id(0)
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
                .buktiTransfer("https://drive.google.com/file/d/1tSy_MqFKbe8VoFjyy-QqAsyZh9UvAv39/view?usp=share_link")
                .idLapangan(lap1.getId())
                .waktuMulai(sc)
                .waktuBerakhir(ec)
                .build();



        tempReservasi = Reservasi.builder()
                .id(0)
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .idLapangan(1)
                .waktuMulai(LocalDateTime.now().plusDays(1))
                .waktuBerakhir(LocalDateTime.now().plusDays(1))
                .kuponId(0)
                .buktiTransfer(null)
                .build();

        tambahanQty = new HashMap<>();
        tambahanQty.put("AIR_MINERAL", 0);
        tambahanQty.put("RAKET", 1);
        tambahanQty.put("SHUTTLECOCK", 1);
    }

    @Test
    void whenFindAllReservationShouldReturnListOfReservation() {
        List<Reservasi> allReservasi = List.of(reservasi);

        when(repository.findAll()).thenReturn(allReservasi);

        List<Reservasi> result = service.findAll();
        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(allReservasi, result);
    }

    @Test
    void whenFindAllReservatioByUserShouldReturnListOfReservation() {
        List<Reservasi> allUserReservasi = List.of(reservasi);

        when(repository.findAllByEmailUser(any(String.class))).thenReturn(allUserReservasi);

        List<Reservasi> result = service.findAllByEmailUser("test1@email.com");
        verify(repository, atLeastOnce()).findAllByEmailUser(any(String.class));
        Assertions.assertEquals(allUserReservasi, result);
    }
    @Test
    void whenFindByIdAndFoundShouldReturnReservation() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(reservasi));

        Reservasi result = service.findById(0);
        verify(repository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(reservasi, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(ReservasiDoesNotExistException.class, () -> {
            service.findById(1000);
        });
    }

    @Test
    void whenUpdateReservastionAndFoundShouldReturnTheUpdatedReservation() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(reservasi));
        when(repository.save(any(Reservasi.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Reservasi.class));

        Reservasi result = service.updateStatus(0, updateRequest);
        verify(repository, atLeastOnce()).save(any(Reservasi.class));
        Assertions.assertEquals(newReservasi1, result);
    }


    @Test
    void whenUpdateReservationAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(ReservasiDoesNotExistException.class, () -> {
            service.updateStatus(0, createRequest);
        });
    }

    @Test
    void whenDeleteReservationAndFoundShouldCallDeleteByIdOnRepo() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(reservasi));

        service.delete(0);
        verify(repository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteReservationAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(ReservasiDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

    @Test
    void whenAddPaymentProofShouldReturnTheUpdatedReservation() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(reservasi));
        when(repository.save(any(Reservasi.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Reservasi.class));

        String linkExample = "https://drive.google.com/file/d/1tSy_MqFKbe8VoFjyy-QqAsyZh9UvAv39/view?usp=share_link";
        Reservasi result = service.addPaymentProof(0, linkExample);

        verify(repository, atLeastOnce()).save(any(Reservasi.class));
        Assertions.assertEquals(newReservasi2, result);
    }

    @Test
    void testGetReservasiCost(){
        Instant instant = Instant.now();
        Date date = Date.from(instant);
        OperasionalLapangan operasionalLapangan = OperasionalLapangan.builder()
                .id(1)
                .idLapangan(1)
                .tanggalLibur(date)
                .build();
        when(operasionalLapanganRepository.findAll()).thenReturn(List.of(operasionalLapangan));
        when(lapanganRepository.findById(any())).thenReturn(Optional.of(lap1));
        when(lapanganRepository.findAll()).thenReturn(List.of(lap1, lap2));
        when(repository.save(any(Reservasi.class))).thenAnswer(invocation ->{
            Reservasi reservasi1 = (Reservasi)(invocation.getArgument(0, Reservasi.class));
            reservasi1.setId(1);
            return reservasi1;
        });
        when(repository.findAll()).thenReturn(List.of());
        when(tambahanUtils.calculateTambahanCost(any(Reservasi.class))).thenReturn(20000);
        when(repository.findById(1)).thenReturn(Optional.of(tempReservasi));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        LocalDate date1 = dateTime.toLocalDate();
        String dateString = date1.format(dateTimeFormatter);

        ReservasiRequest request = ReservasiRequest.builder()
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .waktuMulai(dateString + " 19:00")
                .waktuBerakhir(dateString + " 21:00")
                .tambahanQuantity(tambahanQty)
                .buktiTransfer(null)
                .kuponId(0)
                .build();


        Reservasi reservasi = service.create(request);
        verify(tambahanUtils, times(1)).createTambahanForReservasi(any(Reservasi.class), any());
        assertEquals("test1@email.com", reservasi.getEmailUser());
        assertTrue(reservasi.getWaktuMulai().toLocalDate().isEqual(date1));
        assertEquals(0, reservasi.getKuponId());
        assertEquals(StatusPembayaran.MENUNGGU_PEMBAYARAN, reservasi.getStatusPembayaran());

    }

}

