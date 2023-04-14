package com.b2.reservation.util;

import com.b2.reservation.model.lapangan.Lapangan;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeValidation {
    private List<LapanganDipakai> lapanganDipakaiList;
    private List<Lapangan> lapanganList;
    private Map<Lapangan, Boolean> mapLapanganDipakai = new HashMap<>();

    public TimeValidation(List<LapanganDipakai> lapanganDipakaiList,
                          List<Lapangan> lapanganList){
        this.lapanganDipakaiList = lapanganDipakaiList;
        this.lapanganList = lapanganList;
        for (Lapangan lapangan: this.lapanganList){
            mapLapanganDipakai.put(lapangan, false);
        }
    }

    public Lapangan findEmptyLapangan(LocalDateTime start, LocalDateTime end){
        for (LapanganDipakai lapanganDipakai: lapanganDipakaiList){
            System.out.println(lapanganDipakai.getWaktuMulai().toString() + " ke " + lapanganDipakai.getWaktuSelesai().toString() + " di " + lapanganDipakai.getLapangan().getId());
            if (((start.isAfter(lapanganDipakai.getWaktuMulai()) || (start.isEqual(lapanganDipakai.getWaktuMulai()))) &&
                    ((end.isBefore(lapanganDipakai.getWaktuSelesai())) || (end.isEqual(lapanganDipakai.getWaktuSelesai())))) ||
                    (start.isBefore(lapanganDipakai.getWaktuMulai()) && end.isAfter(lapanganDipakai.getWaktuMulai())) ||
                    (start.isBefore(lapanganDipakai.getWaktuSelesai()) && end.isAfter(lapanganDipakai.getWaktuSelesai()))){
                mapLapanganDipakai.replace(lapanganDipakai.getLapangan(), true);
                System.out.println("Lapangan " + lapanganDipakai.getLapangan().getId() + " dipakai");
            }
        }

        for (Map.Entry<Lapangan, Boolean> entry: mapLapanganDipakai.entrySet()){
            if (entry.getValue().equals(false)){
                return entry.getKey();
            }
        }
        System.out.println(mapLapanganDipakai);
        Lapangan lap = new Lapangan();
        lap.setId(-1);
        return lap;
    }

    public Boolean isDateTimeValid(LocalDateTime start, LocalDateTime end){
        return start.isBefore(end) &&
                (start.isEqual(LocalDateTime.now()) || start.isAfter(LocalDateTime.now())) &&
                (start.toLocalDate().equals(end.toLocalDate()));
    }

    public Boolean isLapanganAvailable(LocalDateTime start, LocalDateTime end){
        return findEmptyLapangan(start, end).getId() != -1;
    }
}
