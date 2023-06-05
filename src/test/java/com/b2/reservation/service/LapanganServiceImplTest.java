package com.b2.reservation.service;

import com.b2.reservation.exceptions.LapanganDoesNotExistException;
import com.b2.reservation.model.User;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LapanganServiceImplTest {

    @Spy
    @InjectMocks
    private LapanganServiceImpl service;

    @Mock
    private LapanganRepository lapanganRepository;

    @Mock
    private OperasionalLapanganRepository operasionalLapanganRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    Lapangan lapangan;

    OperasionalLapanganRequest createRequest;
    OperasionalLapangan operasionalLapangan;
    OperasionalLapangan operasionalLapangan1;
    List<User> userList;



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
        User user1 = User.builder()
                .id(1)
                .email("test@email.com")
                .role("USER")
                .build();
        User user2 = User.builder()
                .id(2)
                .email("test2@email.com")
                .role("USER")
                .build();
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
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
        doNothing().when(service).sendNotificationToAllUsers(any(OperasionalLapangan.class), anyString());
        OperasionalLapangan result = service.createCloseDate(createRequest, "TEST");
        verify(operasionalLapanganRepository, atLeastOnce()).save(any(OperasionalLapangan.class));
        Assertions.assertEquals(operasionalLapangan, result);
    }

    @Test
    void whenCreateClosedDateWithInvalidLapanganIdShouldThrowsException() {
        when(lapanganRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(LapanganDoesNotExistException.class, () -> {
            service.createCloseDate(createRequest, "TEST");
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

    @Test
    void testSendNotificationToAllUsers(){
        when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenReturn(new ResponseEntity<>(userList, HttpStatusCode.valueOf(200)));
        when(userService.getAllUser(anyString())).thenReturn(userList);
        service.sendNotificationToAllUsers(operasionalLapangan, "TEST");
        verify(restTemplate, times(2)).exchange(anyString(), any(), any(), eq(Object.class));
        verify(userService, times(1)).getAllUser(anyString());
    }


}
