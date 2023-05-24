package com.b2.reservation.service;

import com.b2.reservation.exceptions.LapanganDoesNotExistException;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.repository.LapanganRepository;
import com.b2.reservation.repository.OperasionalLapanganRepository;
import com.b2.reservation.request.OperasionalLapanganRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LapanganServiceImplTest {

    @InjectMocks
    private LapanganServiceImpl service;

    @Mock
    private LapanganRepository lapanganRepository;

    @Mock
    private OperasionalLapanganRepository operasionalLapanganRepository;

    Lapangan lapangan;

    OperasionalLapanganRequest createRequest;
    OperasionalLapangan operasionalLapangan;
    OperasionalLapangan operasionalLapangan1;



    @BeforeEach
    void setUp() {
        Date date = new Date();

        lapangan = Lapangan.builder().id(0).build();

        createRequest = OperasionalLapanganRequest.builder()
                .idLapangan(0)
                .tanggalLibur(date)
                .build();

        operasionalLapangan = OperasionalLapangan.builder()
                .id(0)
                .idLapangan(0)
                .tanggalLibur(date)
                .build();
        operasionalLapangan1 = OperasionalLapangan.builder()
                .id(1)
                .idLapangan(1)
                .tanggalLibur(new Date(2020, 10, 10))
                .build();
    }

    @Test
    void whenCreateLapanganShouldReturnTheCreatedLapangan() {
        when(lapanganRepository.save(any(Lapangan.class))).thenAnswer(invocation -> {
            var lapangan = invocation.getArgument(0, Lapangan.class);
            lapangan.setId(0);
            return lapangan;
        });

        Lapangan result = service.create();
        verify(lapanganRepository, atLeastOnce()).save(any(Lapangan.class));
        Assertions.assertEquals(lapangan, result);
    }

    @Test
    void whenCreateClosedDateShouldReturnTheCreatedOperasionalLapangan() {
        when(lapanganRepository.findById(any(Integer.class))).thenReturn(Optional.of(lapangan));
        when(operasionalLapanganRepository.save(any(OperasionalLapangan.class))).thenAnswer(invocation -> {
            var operasional = invocation.getArgument(0, OperasionalLapangan.class);
            operasional.setId(0);
            return operasional;
        });

        OperasionalLapangan result = service.createCloseDate(createRequest);
        verify(operasionalLapanganRepository, atLeastOnce()).save(any(OperasionalLapangan.class));
        Assertions.assertEquals(operasionalLapangan, result);
    }

    @Test
    void whenCreateClosedDateWithInvalidLapanganIdShouldThrowsException() {
        when(lapanganRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(LapanganDoesNotExistException.class, () -> {
            service.createCloseDate(createRequest);
        });

    }

    @Test
    void testGetAllClosedLapanganByDate(){
        when(operasionalLapanganRepository.findAll()).thenReturn(List.of(operasionalLapangan, operasionalLapangan1));
        Date date = new Date(2020, 10, 10);
        List<OperasionalLapangan> operasionalLapanganList = service.getAllClosedLapanganByDate(date);
        Assertions.assertEquals(1, operasionalLapanganList.size());
        Assertions.assertEquals(operasionalLapangan1, operasionalLapanganList.get(0));
        verify(operasionalLapanganRepository, times(1)).findAll();
    }


}
