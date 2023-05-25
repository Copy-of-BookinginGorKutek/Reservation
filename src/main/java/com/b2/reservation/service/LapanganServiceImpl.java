package com.b2.reservation.service;

import com.b2.reservation.exceptions.LapanganDoesNotExistException;
import com.b2.reservation.model.User;
import com.b2.reservation.repository.LapanganRepository;
import com.b2.reservation.repository.OperasionalLapanganRepository;
import com.b2.reservation.request.NotificationRequest;
import com.b2.reservation.request.OperasionalLapanganRequest;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LapanganServiceImpl implements LapanganService{

    private final LapanganRepository lapanganRepository;

    private final OperasionalLapanganRepository operasionalLapanganRepository;

    private final UserService userService;
    @Override
    public Lapangan create() {
        Lapangan lapangan = Lapangan.builder().build();
        lapangan = lapanganRepository.save(lapangan);
        return lapangan;
    }

    @Override
    public OperasionalLapangan createCloseDate(OperasionalLapanganRequest request) {
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
        return operasionalLapangan;
    }

    private void sendNotificationToAllUsers(OperasionalLapangan operasionalLapangan, String token){
        List<User> listOfUsers = userService.getAllUser(token);
        String message = "Lapangan " + operasionalLapangan.getIdLapangan() + " ditutup pada " + operasionalLapangan.getTanggalLibur();
        for (User user: listOfUsers){
            NotificationRequest request = NotificationRequest.builder()
                    .emailUser(user.getEmail())
                    .statusId(null)
                    .message(message)
                    .build();
            // TODO: finish
        }
    }

    private boolean isLapanganDoesNotExist(Integer idLapangan) {
        return lapanganRepository.findById(idLapangan).isEmpty();
    }

    public List<OperasionalLapangan> getAllClosedLapanganByDate(Date date){
        List<OperasionalLapangan> operasionalLapanganList = operasionalLapanganRepository.findAll();
        List<OperasionalLapangan> closedLapanganByDateList = new ArrayList<>();
        for (OperasionalLapangan operasionalLapangan: operasionalLapanganList){
            if (operasionalLapangan.getTanggalLibur().getDate() == date.getDate() &&
                    operasionalLapangan.getTanggalLibur().getYear() == date.getYear() &&
                    operasionalLapangan.getTanggalLibur().getMonth() == date.getMonth()){
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
