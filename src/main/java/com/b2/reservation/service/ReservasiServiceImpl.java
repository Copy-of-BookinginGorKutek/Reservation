package com.b2.reservation.service;

import com.b2.reservation.exceptions.DateTimeIsNotValidException;
import com.b2.reservation.exceptions.LapanganIsNotAvailableException;
import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.repository.LapanganRepository;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.repository.TambahanRepository;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.util.LapanganDipakai;
import com.b2.reservation.util.TambahanUtils;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.util.TimeValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservasiServiceImpl implements ReservasiService {
    private final ReservasiRepository reservasiRepository;
    private final TambahanUtils tambahanUtils;
    private final TambahanRepository tambahanRepository;
    private final LapanganRepository lapanganRepository;
    @Override
    public List<Reservasi> findAll() {
        return reservasiRepository.findAll();
    }
    @Override
    public Reservasi findById(Integer id) {
        Reservasi reservasi = reservasiRepository.findById(id).orElse(null);
        if (reservasi == null) {
            throw new ReservasiDoesNotExistException(id);
        }
        return reservasi;
    }

    @Override
    public Reservasi create(ReservasiRequest request) {
        TimeValidation timeValidation = new TimeValidation(createLapanganDipakaiList(),
                lapanganRepository.findAll());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime waktuMulai = LocalDateTime.parse(request.getWaktuMulai(), formatter);
        LocalDateTime waktuBerakhir = LocalDateTime.parse(request.getWaktuBerakhir(), formatter);
        if (Boolean.FALSE.equals(timeValidation.isDateTimeValid(waktuMulai, waktuBerakhir))){
            throw new DateTimeIsNotValidException();
        }
        if (Boolean.FALSE.equals(timeValidation.isLapanganAvailable(waktuMulai, waktuBerakhir))){
            throw new LapanganIsNotAvailableException();
        }
        Lapangan lapangan = timeValidation.findEmptyLapangan(waktuMulai, waktuBerakhir);
        Reservasi reservasi = Reservasi.builder()
                .emailUser(request.getEmailUser())
                .statusPembayaran(request.getStatusPembayaran())
                .buktiTransfer(request.getBuktiTransfer())
                .waktuMulai(waktuMulai)
                .waktuBerakhir(waktuBerakhir)
                .idLapangan(lapangan.getId())
                .build();
        reservasi = reservasiRepository.save(reservasi);
        tambahanUtils.createTambahanForReservasi(reservasi, request.getTambahanQuantity());
        Integer harga = getReservasiCost(reservasi.getId());
        reservasi.setHarga(harga);
        return this.reservasiRepository.save(reservasi);
    }

    @Override
    public Reservasi update(Integer id, ReservasiRequest request) {
        if (isReservasiDoesNotExist(id)) {
            throw new ReservasiDoesNotExistException(id);
        }
        Reservasi reservasi = this.reservasiRepository.findById(id).orElseThrow();
        Reservasi newReservasi = Reservasi.builder()
                .id(id)
                .emailUser(request.getEmailUser())
                .buktiTransfer(request.getBuktiTransfer())
                .statusPembayaran(request.getStatusPembayaran())
                .harga(reservasi.getHarga())
                .waktuMulai(reservasi.getWaktuMulai())
                .waktuBerakhir(reservasi.getWaktuBerakhir())
                .idLapangan(reservasi.getIdLapangan())
                .build();
        return this.reservasiRepository.save(newReservasi);
    }

    @Override
    public void delete(Integer id) {
        if (isReservasiDoesNotExist(id)) {
            throw new ReservasiDoesNotExistException(id);
        }
        this.reservasiRepository.deleteById(id);
    }

    @Override
    public List<Reservasi> findAllByEmailUser(String email){
        return reservasiRepository.findAllByEmailUser(email);
    }

    private boolean isReservasiDoesNotExist(Integer id) {
        return reservasiRepository.findById(id).isEmpty();
    }

    private Integer getReservasiCost(Integer id){
        if (isReservasiDoesNotExist(id)){
            throw new ReservasiDoesNotExistException(id);
        }
        Reservasi reservasi = this.reservasiRepository.findById(id).orElseThrow();
        return Lapangan.getCost() + tambahanUtils.calculateTambahanCost(reservasi);
    }

    @Override
    public List<LapanganDipakai> createLapanganDipakaiList(){
        List<Reservasi> reservasiList = reservasiRepository.findAll();
        List<LapanganDipakai> lapanganDipakaiList = new ArrayList<>();
        for(Reservasi reservasi:reservasiList){
            Lapangan lapangan = lapanganRepository.findById(reservasi.getIdLapangan()).orElseThrow();
            LocalDateTime waktuMulai = reservasi.getWaktuMulai();
            LocalDateTime waktuBerakhir = reservasi.getWaktuBerakhir();
            lapanganDipakaiList.add(new LapanganDipakai(lapangan, waktuMulai, waktuBerakhir));
        }
        return lapanganDipakaiList;
    }

    @Override
    public Reservasi addPaymentProof(Integer id, String paymentProof) {
        Reservasi before = findById(id);

        Reservasi after = Reservasi.builder()
                .id(id)
                .emailUser(before.getEmailUser())
                .statusPembayaran(before.getStatusPembayaran())
                .buktiTransfer(paymentProof)
                .harga(before.getHarga())
                .idLapangan(before.getIdLapangan())
                .waktuMulai(before.getWaktuMulai())
                .waktuBerakhir(before.getWaktuBerakhir())
                .build();

        return reservasiRepository.save(after);
    }

}
