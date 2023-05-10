package com.b2.reservation.service;

import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.repository.LapanganRepository;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.request.ReservasiRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    Reservasi reservasi;
    Reservasi newReservasi1;
    Reservasi newReservasi2;
    ReservasiRequest createRequest;
    ReservasiRequest updateRequest;

    Integer id;

    @BeforeEach
    void setUp() {
        Lapangan lap1 = new Lapangan();
        lap1.setId(1);
        Lapangan lap2 = new Lapangan();
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
                .emailUser("test2@email.com")
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
                .emailUser("test2@email.com")
                .idLapangan(lap1.getId())
                .waktuMulai(sc)
                .waktuBerakhir(ec)
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
                .build();

        newReservasi2 = Reservasi.builder()
                .id(0)
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .buktiTransfer("https://drive.google.com/file/d/1tSy_MqFKbe8VoFjyy-QqAsyZh9UvAv39/view?usp=share_link")
                .idLapangan(lap1.getId())
                .waktuMulai(sc)
                .waktuBerakhir(ec)
                .build();
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

        Assertions.assertThrows(ReservasiDoesNotExistException.class, () -> {
            service.findById(1000);
        });
    }

    @Test
    void whenUpdateReservastionAndFoundShouldReturnTheUpdatedReservation() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(reservasi));
        when(repository.save(any(Reservasi.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Reservasi.class));

        Reservasi result = service.update(0, updateRequest);
        verify(repository, atLeastOnce()).save(any(Reservasi.class));
        Assertions.assertEquals(newReservasi1, result);
    }


    @Test
    void whenUpdateReservationAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(ReservasiDoesNotExistException.class, () -> {
            service.update(0, createRequest);
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
        Assertions.assertThrows(ReservasiDoesNotExistException.class, () -> {
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

}

