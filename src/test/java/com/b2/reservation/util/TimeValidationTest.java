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

        LocalDateTime s1 = LocalDateTime.of(2024,5,14,19,0,0);
        LocalDateTime e1 = LocalDateTime.of(2024,5,14,20,0,0);
        LapanganDipakai lapd1 = new LapanganDipakai(lap1, s1, e1);

        LocalDateTime s2 = LocalDateTime.of(2024,5,14,10,0,0);
        LocalDateTime e2 = LocalDateTime.of(2024,5,14,19,0,0);
        LapanganDipakai lapd2 = new LapanganDipakai(lap2, s2, e2);

        LocalDateTime s3 = LocalDateTime.of(2024,5,14,17,0,0);
        LocalDateTime e3 = LocalDateTime.of(2024,5,14,20,0,0);
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
    void findEmptyLapanganIfLapanganIsAvailable() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,18,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,18,30,0);
        assertEquals(lap1.getId(), tv.findEmptyLapangan(sc, ec).getId());
    }

    @Test
    void findEmptyLapanganIfLapanganIsNotAvailable() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,17,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,21,30,0);
        assertEquals(-1, tv.findEmptyLapangan(sc, ec).getId());
    }

    @Test
    void dateTimeValidationWithValidDateTime() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,18,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,18,30,0);
        assertEquals(true, tv.isDateTimeValid(sc, ec));
    }

    @Test
    void dateTimeValidationWithFinishTimeBeforeStartTime() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,18,50,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,18,30,0);
        assertEquals(false, tv.isDateTimeValid(sc, ec));
    }

    @Test
    void dateTimeValidationWithTimeBeforeCurrentTime() {
        LocalDateTime sc = LocalDateTime.of(2020,5,14,18,0,0);
        LocalDateTime ec = LocalDateTime.of(2020,5,14,18,30,0);
        assertEquals(false, tv.isDateTimeValid(sc, ec));
    }

    @Test
    void dateTimeValidationWithDurationMoreThanOneDay() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,18,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,15,18,30,0);
        assertEquals(false, tv.isDateTimeValid(sc, ec));
    }

    @Test
    void isLapanganAvailableIfLapanganIsAvailable() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,19,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,21,0,0);
        assertEquals(true, tv.isLapanganAvailable(sc, ec));
    }

    @Test
    void isLapanganAvailableIfLapanganIsNotAvailable() {
        LocalDateTime sc = LocalDateTime.of(2024,5,14,17,0,0);
        LocalDateTime ec = LocalDateTime.of(2024,5,14,21,30,0);
        assertEquals(false, tv.isLapanganAvailable(sc, ec));
    }
}