package com.b2.reservation.service;
import com.b2.reservation.exceptions.KuponDoesNotExistException;
import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.repository.KuponRepository;
import com.b2.reservation.request.KuponRequest;
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
class KuponServiceImpTest {
    @InjectMocks
    private KuponServiceImpl service;

    @Mock
    private KuponRepository kuponRepository;


    Kupon kupon;

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

    }


    @Test
    void whenCreateKuponShouldReturnTheCreatedKupon() {
        when(kuponRepository.save(any(Kupon.class))).thenAnswer(invocation -> {
            var kupon = invocation.getArgument(0, Kupon.class);
            kupon.setId(1);
            kupon.setName("diskon1");
            kupon.setPercentageDiscounted(50);
            return kupon;
        });

        Kupon result = service.create(createRequest);
        verify(kuponRepository, atLeastOnce()).save(any(Kupon.class));
        Assertions.assertEquals(kupon, result);
    }


    @Test
    void whenFindAllKuponShouldReturnListOfKupon() {
        List<Kupon> allKupon = List.of(kupon);

        when(kuponRepository.findAll()).thenReturn(allKupon);

        List<Kupon> result = service.findAll();
        verify(kuponRepository, atLeastOnce()).findAll();
        Assertions.assertEquals(allKupon, result);
    }


    @Test
    void whenDeleteWithInvalidKuponIdShouldThrowsException() {
        when(kuponRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(KuponDoesNotExistException.class, () -> {
            service.delete(2);
        });
    }

    @Test
    void whenDeleteWithValidKuponIdShouldDeleteOnRepository() {
        when(kuponRepository.findById(any(Integer.class))).thenReturn(Optional.of(kupon));
        service.delete(0);
        verify(kuponRepository, atLeastOnce()).deleteById(any(Integer.class));

    }

}
