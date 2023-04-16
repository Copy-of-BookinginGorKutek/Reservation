package com.b2.reservation.util;

import com.b2.reservation.model.lapangan.Lapangan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeValidationTest {
    TimeValidation tv;
    Lapangan lap1, lap2, lap3;
    @BeforeEach
    void setUp(){
        lap1 = new Lapangan();
        lap1.setId(1);
        lap2 = new Lapangan();
        lap2.setId(2);
        lap3 = new Lapangan();
        lap3.setId(3);

        LocalDateTime s1 = LocalDateTime.of(2023,5,14,19,0,0);
        LocalDateTime e1 = LocalDateTime.of(2023,5,14,20,0,0);
        LapanganDipakai lapd1 = new LapanganDipakai(lap1, s1, e1);

        LocalDateTime s2 = LocalDateTime.of(2023,5,14,10,0,0);
        LocalDateTime e2 = LocalDateTime.of(2023,5,14,19,0,0);
        LapanganDipakai lapd2 = new LapanganDipakai(lap2, s2, e2);

        LocalDateTime s3 = LocalDateTime.of(2023,5,14,17,0,0);
        LocalDateTime e3 = LocalDateTime.of(2023,5,14,20,0,0);
        LapanganDipakai lapd3 = new LapanganDipakai(lap3, s3, e3);

        List<LapanganDipakai> lapanganDipakaiList = new ArrayList<>();
        lapanganDipakaiList.add(lapd1);
        lapanganDipakaiList.add(lapd2);
        lapanganDipakaiList.add(lapd3);

        List<Lapangan> lapanganList = new ArrayList<>();
        lapanganList.add(lap1);
        lapanganList.add(lap2);
        lapanganList.add(lap3);

        tv = new TimeValidation(lapanganDipakaiList, lapanganList);
    }
    @Test
    void findEmptyLapangan() {
        LocalDateTime sc = LocalDateTime.of(2023,5,14,18,0,0);
        LocalDateTime ec = LocalDateTime.of(2023,5,14,18,30,0);
        assertEquals(lap1.getId(), tv.findEmptyLapangan(sc, ec).getId());
    }

    @Test
    void isDateTimeValid() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,18,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,18,30,0);
        assertEquals(true, tv.isDateTimeValid(sc, ec));
    }

    @Test
    void isLapanganAvailable() {
        LocalDateTime sc = LocalDateTime.of(2023,5,14,19,0,0);
        LocalDateTime ec = LocalDateTime.of(2023,5,14,21,0,0);
        assertEquals(true, tv.isLapanganAvailable(sc, ec));
    }
}