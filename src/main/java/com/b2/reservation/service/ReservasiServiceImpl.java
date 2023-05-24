package com.b2.reservation.service;

import com.b2.reservation.exceptions.DateTimeIsNotValidException;
import com.b2.reservation.exceptions.KuponDoesNotExistException;
import com.b2.reservation.exceptions.LapanganIsNotAvailableException;
import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.repository.*;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.util.LapanganDipakai;
import com.b2.reservation.util.TambahanUtils;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.util.TimeValidation;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservasiServiceImpl implements ReservasiService {
    private final ReservasiRepository reservasiRepository;
    private final TambahanUtils tambahanUtils;
    private final TambahanRepository tambahanRepository;
    private final KuponRepository kuponRepository;
    private final LapanganRepository lapanganRepository;
    private final OperasionalLapanganRepository operasionalLapanganRepository;
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
                .kuponId(request.getKuponId())
                .build();
        reservasi = reservasiRepository.save(reservasi);
        tambahanUtils.createTambahanForReservasi(reservasi, request.getTambahanQuantity());
        Integer hargaBeforeKupon = getReservasiCost(reservasi.getId());
        // Id kupon 0 = not used
        if (request.getKuponId().equals(0)){
            reservasi.setHarga(hargaBeforeKupon);
        } else{
            Integer hargaAkhir = calculatePriceAfterDiscount(request.getKuponId(), hargaBeforeKupon);
            reservasi.setHarga(hargaAkhir);
        }
        return this.reservasiRepository.save(reservasi);
    }

    private Integer calculatePriceAfterDiscount(Integer kuponId, Integer basePrice){
        Optional<Kupon> kupon = kuponRepository.findById(kuponId);
        if (kupon.isEmpty()){
            throw new KuponDoesNotExistException(kuponId);
        }
        Integer discountPercentage = kupon.get().getPercentageDiscounted();
        Integer discountAmount = basePrice * discountPercentage / 100;
        return basePrice - discountAmount;
    }
    @Override
    public Reservasi updateStatus(Integer id, ReservasiRequest request) {
        Reservasi reservasi = findById(id);
        Reservasi newReservasi = Reservasi.builder()
                .id(id)
                .emailUser(reservasi.getEmailUser())
                .buktiTransfer(reservasi.getBuktiTransfer())
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
    @Generated
    public List<Reservasi> findAllByEmailUser(String email){
        return reservasiRepository.findAllByEmailUser(email);
    }

    @Generated
    private boolean isReservasiDoesNotExist(Integer id) {
        return reservasiRepository.findById(id).isEmpty();
    }

    @Generated
    private Integer getReservasiCost(Integer id){
        if (isReservasiDoesNotExist(id)){
            throw new ReservasiDoesNotExistException(id);
        }
        Reservasi reservasi = this.reservasiRepository.findById(id).orElseThrow();
        Double lapanganPrice = getHours(reservasi.getWaktuMulai(), reservasi.getWaktuBerakhir()) * Lapangan.getCost();
        System.out.println(lapanganPrice);
        return Math.toIntExact(Math.round(lapanganPrice) + tambahanUtils.calculateTambahanCost(reservasi));
    }

    @Generated
    private double getHours(LocalDateTime waktuMulai, LocalDateTime waktuSelesai){
        Duration duration = Duration.between(waktuMulai, waktuSelesai);
        return (duration.getSeconds()+1)/3600.0;
    }

    @Override
    public List<LapanganDipakai> createLapanganDipakaiList(){
        List<LapanganDipakai> lapanganDipakaiByReservasi = getLapanganDipakaiByReservasi();
        List<LapanganDipakai> lapanganDipakaiByOperasionalLapangan = getLapanganDipakaiByOperasionalLapangan();
        List<LapanganDipakai> result = new ArrayList<>();
        result.addAll(lapanganDipakaiByReservasi);
        result.addAll(lapanganDipakaiByOperasionalLapangan);
        return result;
    }

    private List<LapanganDipakai> getLapanganDipakaiByReservasi(){
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

    private List<LapanganDipakai> getLapanganDipakaiByOperasionalLapangan(){
        List<OperasionalLapangan> operasionalLapanganList = operasionalLapanganRepository.findAll();
        List<LapanganDipakai> lapanganDipakaiList = new ArrayList<>();
        for(OperasionalLapangan operasionalLapangan: operasionalLapanganList){
            Lapangan lapangan = lapanganRepository.findById(operasionalLapangan.getIdLapangan()).orElseThrow();
            Date fullDate = operasionalLapangan.getTanggalLibur();
            LocalDateTime fullDateStart = parseDateToLocalDateTime(fullDate, "00:00");
            LocalDateTime fullDateEnd = parseDateToLocalDateTime(fullDate, "23:59");
            lapanganDipakaiList.add(new LapanganDipakai(lapangan, fullDateStart, fullDateEnd));
        }
        return lapanganDipakaiList;
    }

    @Generated
    private LocalDateTime parseDateToLocalDateTime(Date date, String timeAsString){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        Integer year = calendar.get(Calendar.YEAR);
        String monthString = (month < 10)? "0" + month : Integer.toString(month);
        String dayString = (day < 10)? "0" + day : Integer.toString(day);
        String fullDateAsString = year + "-" + monthString + "-" + dayString + " " + timeAsString;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(fullDateAsString, formatter);
    }

    @Override
    public Reservasi addPaymentProof(Integer id, String paymentProof) {
        Reservasi before = findById(id);

        Reservasi after = Reservasi.builder()
                .id(id)
                .emailUser(before.getEmailUser())
                .statusPembayaran(StatusPembayaran.MENUNGGU_KONFIRMASI)
                .buktiTransfer(paymentProof)
                .harga(before.getHarga())
                .idLapangan(before.getIdLapangan())
                .waktuMulai(before.getWaktuMulai())
                .waktuBerakhir(before.getWaktuBerakhir())
                .build();

        return reservasiRepository.save(after);
    }

    @Generated
    private Date parseStringToDate(String dateAsString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(dateAsString);
    }

    @Override
    @Generated
    public List<Reservasi> findReservasiByDate(String dateAsString) throws ParseException {
        List<Reservasi> allReservasi = reservasiRepository.findAll();
        List<Reservasi> allReservasiByDate = new ArrayList<>();
        Date inputDate = parseStringToDate(dateAsString);
        for (Reservasi reservasi: allReservasi){
            if (compareDateAndDatetime(inputDate, reservasi.getWaktuMulai())){
                allReservasiByDate.add(reservasi);
            }
        }
        return allReservasiByDate;

    }

    @Generated
    private boolean compareDateAndDatetime(Date date, LocalDateTime datetime){
        return (date.getYear() + 1900) == datetime.getYear() &&
                (date.getMonth() + 1) == datetime.getMonthValue() &&
                date.getDate() == datetime.getDayOfMonth();
    }
}
