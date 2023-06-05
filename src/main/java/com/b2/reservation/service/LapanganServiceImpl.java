package com.b2.reservation.service;

import com.b2.reservation.exceptions.LapanganDoesNotExistException;
import com.b2.reservation.model.User;
import com.b2.reservation.repository.LapanganRepository;
import com.b2.reservation.repository.OperasionalLapanganRepository;
import com.b2.reservation.request.NotificationRequest;
import com.b2.reservation.request.OperasionalLapanganRequest;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LapanganServiceImpl implements LapanganService{

    private final LapanganRepository lapanganRepository;

    private final OperasionalLapanganRepository operasionalLapanganRepository;

    private final UserService userService;

    private final RestTemplate restTemplate;
    @Override
    public Lapangan create() {
        Lapangan lapangan = Lapangan.builder().build();
        lapangan = lapanganRepository.save(lapangan);
        return lapangan;
    }

    @Override
    public OperasionalLapangan createCloseDate(OperasionalLapanganRequest request, String token) {
        Integer idLapangan = request.getIdLapangan();
        Date tanggalLibur = request.getTanggalLibur();
        if (isLapanganDoesNotExist(idLapangan)) {
            throw new LapanganDoesNotExistException(idLapangan);
        }
        OperasionalLapangan operasionalLapangan = OperasionalLapangan.builder()
                .idLapangan(idLapangan)
                .tanggalLibur(tanggalLibur)
                .build();
        operasionalLapangan = operasionalLapanganRepository.save(operasionalLapangan);
        sendNotificationToAllUsers(operasionalLapangan, token);
        return operasionalLapangan;
    }

    public void sendNotificationToAllUsers(OperasionalLapangan operasionalLapangan, String token){
        Date tanggalLibur = operasionalLapangan.getTanggalLibur();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalLiburString = format.format(tanggalLibur);
        List<User> listOfUsers = userService.getAllUser(token);
        String message = "Lapangan " + operasionalLapangan.getIdLapangan() + " ditutup pada " + tanggalLiburString;
        for (User user: listOfUsers){
            NotificationRequest request = NotificationRequest.builder()
                    .emailUser(user.getEmail())
                    .statusId(null)
                    .message(message)
                    .build();
            postBroadcastNotification(request, token);
        }
    }

    private void postBroadcastNotification(NotificationRequest notificationRequest, String token){
        HttpHeaders headers = getJSONHttpHeaders(token);
        String url = "http://34.142.212.224:40/api/v1/notification/send";
        HttpEntity<NotificationRequest> http = new HttpEntity<>(notificationRequest, headers);
        restTemplate.exchange(url, HttpMethod.POST, http, Object.class);
    }

    @Generated
    private HttpHeaders getJSONHttpHeaders(String token){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

    private boolean isLapanganDoesNotExist(Integer idLapangan) {
        return lapanganRepository.findById(idLapangan).isEmpty();
    }

    public List<OperasionalLapangan> getAllClosedLapanganByDate(Date date){
        List<OperasionalLapangan> operasionalLapanganList = operasionalLapanganRepository.findAll();
        List<OperasionalLapangan> closedLapanganByDateList = new ArrayList<>();
        for (OperasionalLapangan operasionalLapangan: operasionalLapanganList){
            Calendar tanggalLibur = Calendar.getInstance();
            tanggalLibur.setTime(operasionalLapangan.getTanggalLibur());
            Calendar tanggalInput = Calendar.getInstance();
            tanggalInput.setTime(date);

            if (tanggalLibur.get(Calendar.DAY_OF_MONTH) == tanggalInput.get(Calendar.DAY_OF_MONTH) &&
                    tanggalLibur.get(Calendar.YEAR) == tanggalInput.get(Calendar.YEAR) &&
                    tanggalLibur.get(Calendar.MONTH) == tanggalInput.get(Calendar.MONTH)){
                closedLapanganByDateList.add(operasionalLapangan);
            }
        }
        return closedLapanganByDateList;
    }
    @Generated
    public List<OperasionalLapangan> getAllClosedLapangan() {
        return operasionalLapanganRepository.findAll();
    }
    @Generated
    public void deleteOperasionalLapangan(Integer id){
        operasionalLapanganRepository.deleteOperasionalLapanganById(id);
    }
}
