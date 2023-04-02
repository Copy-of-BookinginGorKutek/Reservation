package com.b2.reservation.service;

import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.request.ReservasiRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    Reservasi reservasi;
    Reservasi newReservasi;
    ReservasiRequest createRequest;
    ReservasiRequest updateRequest;

    Integer id;

    @BeforeEach
    void setUp() {
        createRequest = ReservasiRequest.builder()
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .build();

        updateRequest = ReservasiRequest.builder()
                .emailUser("test2@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
                .build();


        reservasi = Reservasi.builder()
                .id(0)
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .build();

        newReservasi = Reservasi.builder()
                .id(0)
                .emailUser("test2@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
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
    void whenCreateReservationShouldReturnTheCreatedReservation() {
        when(repository.save(any(Reservasi.class))).thenAnswer(invocation -> {
            var reservasi = invocation.getArgument(0, Reservasi.class);
            reservasi.setId(0);
            return reservasi;
        });

        Reservasi result = service.create(createRequest);
        verify(repository, atLeastOnce()).save(any(Reservasi.class));
        Assertions.assertEquals(reservasi, result);
    }

    @Test
    void whenUpdateReservastionAndFoundShouldReturnTheUpdatedReservation() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(reservasi));
        when(repository.save(any(Reservasi.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Reservasi.class));

        Reservasi result = service.update(0, updateRequest);
        verify(repository, atLeastOnce()).save(any(Reservasi.class));
        Assertions.assertEquals(newReservasi, result);
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

}

