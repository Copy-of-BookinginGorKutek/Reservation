package com.b2.reservation.service;

import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.repository.TambahanRepository;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.util.LapanganDipakai;
import com.b2.reservation.util.TambahanUtils;
import com.b2.reservation.model.lapangan.Lapangan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservasiServiceImpl implements ReservasiService {
    private final ReservasiRepository reservasiRepository;
    private final TambahanUtils tambahanUtils;
    private final TambahanRepository tambahanRepository;
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
        Reservasi reservasi = Reservasi.builder()
                .emailUser(request.getEmailUser())
                .statusPembayaran(request.getStatusPembayaran())
                .buktiTransfer(request.getBuktiTransfer())
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

    public Integer getReservasiCost(Integer id){
        if (isReservasiDoesNotExist(id)){
            throw new ReservasiDoesNotExistException(id);
        }
        Reservasi reservasi = this.reservasiRepository.findById(id).orElseThrow();
        return Lapangan.getCost() + tambahanUtils.calculateTambahanCost(reservasi);
    }

    public List<LapanganDipakai> createLapanganDipakaiList(){
        List<Reservasi> reservasiList = reservasiRepository.findAll();
        List<LapanganDipakai> lapanganDipakaiList = new ArrayList<>();
        for(Reservasi reservasi:reservasiList){
            Lapangan lapangan = reservasi.getLapangan();
            LocalDateTime waktuMulai = reservasi.getWaktuMulai();
            LocalDateTime waktuBerakhir = reservasi.getWaktuBerakhir();
            lapanganDipakaiList.add(new LapanganDipakai(lapangan, waktuMulai, waktuBerakhir));
        }
        return lapanganDipakaiList;
    }
}
